package org.polyfrost.hytils.mixin.client.tab;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.features.general.TabChanger;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTabOverlay.class)
abstract class PlayerTabOverlayMixin_HidePing {
    //~ if <26.1 'extractPingIcon' -> 'renderPingIcon'
    @Inject(method = "extractPingIcon", at = @At("HEAD"), cancellable = true)
    private void hidePingIcon(GuiGraphicsExtractor graphics, int slotWidth, int xo, int yo, PlayerInfo info, CallbackInfo ci) {
        if (HytilsRebornConfig.isEnabled() && HypixelUtils.isHypixel() && TabChanger.hidePing(info)) {
            ci.cancel();
        }
    }

    //~ if <26.1 'extractRenderState' -> 'render' {
    @ModifyArg(
        method = "extractRenderState",
        at = @At(
            value = "INVOKE",
            //~ if <26.1 'extractTablistScore' -> 'renderTablistScore'
            target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;extractTablistScore(Lnet/minecraft/world/scores/Objective;ILnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;IILjava/util/UUID;Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"
        ),
        index = 3
    )
    private int offsetScoreLeft(int left, @Local PlayerInfo info) {
        if (HytilsRebornConfig.isEnabled() && HypixelUtils.isHypixel() && TabChanger.hidePing(info)) {
            return left + 11;
        }

        return left;
    }

    @ModifyArg(
        method = "extractRenderState",
        at = @At(
            value = "INVOKE",
            //~ if <26.1 'extractTablistScore' -> 'renderTablistScore'
            target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;extractTablistScore(Lnet/minecraft/world/scores/Objective;ILnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;IILjava/util/UUID;Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"
        ),
        index = 4
    )
    private int offsetScoreRight(int right, @Local PlayerInfo info) {
        if (HytilsRebornConfig.isEnabled() && HypixelUtils.isHypixel() && TabChanger.hidePing(info)) {
            return right + 11;
        }

        return right;
    }
    //~}
}
