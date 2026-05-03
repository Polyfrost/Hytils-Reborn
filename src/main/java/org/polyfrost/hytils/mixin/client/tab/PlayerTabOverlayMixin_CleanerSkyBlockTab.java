package org.polyfrost.hytils.mixin.client.tab;

import com.llamalad7.mixinextras.sugar.Local;
import net.hypixel.data.type.GameType;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin_CleanerSkyBlockTab {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V", ordinal = 2), index = 1)
    private int removeTabGaps(int original, @Local(ordinal = 15) int row) {
        if (HytilsRebornConfig.INSTANCE.getCleanerSkyblockTabInfo() && HypixelUtils.getLocation().getGameType().orElse(null) == GameType.SKYBLOCK) {
            return row != 0 ? original - 1 : original;
        }

        return original;
    }
}
