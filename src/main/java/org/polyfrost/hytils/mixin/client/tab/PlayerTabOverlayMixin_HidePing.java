package org.polyfrost.hytils.mixin.client.tab;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.features.general.TabChanger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerTabOverlay.class)
abstract class PlayerTabOverlayMixin_HidePing {
    //~ if <26.1 'extractPingIcon' -> 'renderPingIcon'
    @Inject(method = "extractPingIcon", at = @At("HEAD"), cancellable = true)
    private void hidePingIcon(GuiGraphicsExtractor graphics, int slotWidth, int xo, int yo, PlayerInfo info, CallbackInfo ci) {
        if (HytilsRebornConfig.isEnabled() && TabChanger.hidePing(info)) {
            ci.cancel();
        }
    }

    //~ if <26.1 'extractRenderState' -> 'render'
    @ModifyArgs(
        method = "extractRenderState",
        at = @At(
            value = "INVOKE",
            //~ if <26.1 'extractTablistScore' -> 'renderTablistScore'
            target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;extractTablistScore(Lnet/minecraft/world/scores/Objective;ILnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;IILjava/util/UUID;Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"
        )
    )
    private void offsetScore(Args args, @Local PlayerInfo playerInfo) {
        if (HytilsRebornConfig.isEnabled() && TabChanger.hidePing(playerInfo)) {
            args.set(3, (int) args.get(3) + 11);
            args.set(4, (int) args.get(4) + 11);
        }
    }
}
