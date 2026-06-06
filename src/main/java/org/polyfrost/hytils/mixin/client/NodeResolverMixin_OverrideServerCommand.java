package org.polyfrost.hytils.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.polyfrost.hytils.client.commands.impl.PlayCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.network.protocol.game.ClientboundCommandsPacket$NodeResolver")
abstract class NodeResolverMixin_OverrideServerCommand {
    @ModifyExpressionValue(method = "resolve", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$NodeResolver;resolve(I)Lcom/mojang/brigadier/tree/CommandNode;", ordinal = 1))
    public CommandNode<?> overrideServerSuggestions(CommandNode<?> original) {
        if (!(original instanceof LiteralCommandNode<?> literal)) return original;

        if (literal.getLiteral().equals("play")) {
            return PlayCommand.getCommandNode();
        }

        return literal;
    }
}
