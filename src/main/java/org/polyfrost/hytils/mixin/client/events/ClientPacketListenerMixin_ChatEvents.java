package org.polyfrost.hytils.mixin.client.events;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.polyfrost.hytils.client.events.ChatReceiveEvent;
import org.polyfrost.hytils.client.events.ChatSendEvent;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPacketListener.class, priority = Integer.MAX_VALUE)
abstract class ClientPacketListenerMixin_ChatEvents {
    @Inject(method = "handleSystemChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handleSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"), cancellable = true)
    private void cancelSystemMessage(ClientboundSystemChatPacket packet, CallbackInfo ci, @Share("chatReceiveEvent") LocalRef<ChatReceiveEvent> chatReceiveEvent) {
        if (chatReceiveEvent.get().getCancelled()) {
            ci.cancel();
        }
    }

    @WrapOperation(method = "handleSystemChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundSystemChatPacket;content()Lnet/minecraft/network/chat/Component;"))
    private Component modifySystemMessage(ClientboundSystemChatPacket packet, Operation<Component> original, @Share("chatReceiveEvent") LocalRef<ChatReceiveEvent> chatReceiveEvent) {
        ChatReceiveEvent event = new ChatReceiveEvent(packet.content(), packet.overlay());
        EventManager.INSTANCE.post(event);
        chatReceiveEvent.set(event);
        return event.getMessage();
    }

    @Inject(method = "handlePlayerChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handlePlayerChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/network/chat/ChatType$Bound;)V"), cancellable = true)
    private void cancelPlayerMessage(ClientboundPlayerChatPacket clientboundPlayerChatPacket, CallbackInfo ci, @Share("chatReceiveEvent") LocalRef<ChatReceiveEvent> chatReceiveEvent) {
        if (chatReceiveEvent.get() != null && chatReceiveEvent.get().getCancelled()) {
            ci.cancel();
        }
    }

    @WrapOperation(method = "handlePlayerChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;unsignedContent()Lnet/minecraft/network/chat/Component;"))
    private Component modifyPlayerMessage(ClientboundPlayerChatPacket packet, Operation<Component> original, @Share("chatReceiveEvent") LocalRef<ChatReceiveEvent> chatReceiveEvent) {
        if (packet.unsignedContent() == null) return original.call(packet);
        ChatReceiveEvent event = new ChatReceiveEvent(packet.unsignedContent(), false);
        EventManager.INSTANCE.post(event);
        chatReceiveEvent.set(event);
        return event.getMessage();
    }

    @Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
    private void cancelSentMessage(String message, CallbackInfo ci, @Share("chatSendEvent") LocalRef<ChatSendEvent> chatSendEvent) {
        ChatSendEvent event = new ChatSendEvent(message);
        EventManager.INSTANCE.post(event);
        chatSendEvent.set(event);
        if (event.cancelled) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "sendChat", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String modifySentMessage(String message, @Share("chatSendEvent") LocalRef<ChatSendEvent> chatSendEvent) {
        return chatSendEvent.get().getMessage();
    }
}
