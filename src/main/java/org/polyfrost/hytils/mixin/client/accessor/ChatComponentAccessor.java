package org.polyfrost.hytils.mixin.client.accessor;

import net.minecraft.client.gui.components.ChatComponent;
//~ if <26.1 'multiplayer.chat.GuiMessage' -> 'GuiMessage'
import net.minecraft.client.multiplayer.chat.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ChatComponent.class)
public interface ChatComponentAccessor {
    @Accessor
    List<GuiMessage> getAllMessages();

    @Invoker
    void invokeRefreshTrimmedMessages();

    @Accessor
    int getChatScrollbarPos();

    @Accessor
    void setChatScrollbarPos(int pos);

    @Accessor
    boolean getNewMessageSinceScroll();

    @Accessor
    void setNewMessageSinceScroll(boolean state);
}
