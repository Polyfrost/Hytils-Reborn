package org.polyfrost.hytils.mixin.client.tab;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.features.general.TabChanger;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerTabOverlay.class)
abstract class PlayerTabOverlayMixin_HideHeads {
    @Definition(id = "showHead", local = @Local(type = boolean.class))
    @Expression("@(showHead) != false")
    //~ if <26.1 'extractRenderState' -> 'render'
    @ModifyExpressionValue(method = "extractRenderState", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean hidePlayerHead(boolean showHead, @Local PlayerInfo playerInfo) {
        return showHead && (!HytilsRebornConfig.isEnabled() || !HypixelUtils.isHypixel() || TabChanger.shouldRenderHead(playerInfo));
    }

    @ModifyArgs(
        //~ if <26.1 'extractRenderState' -> 'render'
        method = "extractRenderState",
        at = @At(
            value = "INVOKE",
            //~ if <26.1 'extractPingIcon' -> 'renderPingIcon'
            target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;extractPingIcon(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIILnet/minecraft/client/multiplayer/PlayerInfo;)V"
        )
    )
    private void offsetPing(Args args, @Local(ordinal = 0) boolean showHead) {
        if (showHead && (HytilsRebornConfig.isEnabled() && HypixelUtils.isHypixel() && !TabChanger.shouldRenderHead(args.get(4)))) {
            args.set(2, (int) args.get(2) + 9);
        }
    }
}
