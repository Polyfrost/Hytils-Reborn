package org.polyfrost.hytils.mixin.client.tab;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.handlers.general.TabChanger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerTabOverlay.class)
abstract class PlayerTabOverlayMixin_HideHeads {
    @Definition(id = "showHead", local = @Local(type = boolean.class))
    @Expression("@(showHead) != false")
    @ModifyExpressionValue(method = "render", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean hidePlayerHead(boolean showHead, @Local PlayerInfo playerInfo) {
        return showHead && (HytilsRebornConfig.isEnabled() && TabChanger.shouldRenderHead(playerInfo));
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;renderPingIcon(Lnet/minecraft/client/gui/GuiGraphics;IIILnet/minecraft/client/multiplayer/PlayerInfo;)V"))
    private void offsetPing(Args args, @Local(ordinal = 0) boolean showHead) {
        if (showHead && (HytilsRebornConfig.isEnabled() && !TabChanger.shouldRenderHead(args.get(4)))) {
            args.set(2, (int) args.get(2) + 9);
        }
    }
}
