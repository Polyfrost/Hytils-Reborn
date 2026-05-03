package org.polyfrost.hytils.mixin.client.tab;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.handlers.general.TabChanger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin_HidePing {
    @Inject(method = "renderPingIcon", at = @At("HEAD"), cancellable = true)
    private void hidePingIcon(GuiGraphics guiGraphics, int i, int j, int k, PlayerInfo playerInfo, CallbackInfo ci) {
        if (HytilsRebornConfig.isEnabled() && TabChanger.hidePing(playerInfo)) {
            ci.cancel();
        }
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;renderTablistScore(Lnet/minecraft/world/scores/Objective;ILnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;IILjava/util/UUID;Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void offsetScore(Args args, @Local PlayerInfo playerInfo) {
        if (HytilsRebornConfig.isEnabled() && TabChanger.hidePing(playerInfo)) {
            args.set(3, (int) args.get(3) + 11);
            args.set(4, (int) args.get(4) + 11);
        }
    }
}
