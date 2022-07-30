/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.hytils.mixin.overlay;

import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.util.DarkColorUtils;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.client.model.pipeline.BlockInfo;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import cc.woverflow.hytils.config.BlockHighlightConfig;
import cc.woverflow.hytils.handlers.cache.HeightHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = VertexLighterFlat.class, remap = false)
public class VertexLighterFlatMixin {
    @Shadow
    @Final
    protected BlockInfo blockInfo;

    @ModifyArgs(method = "processQuad", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/model/pipeline/VertexLighterFlat;updateColor([F[FFFFFI)V"))
    private void modifyArgs(Args args) {
        int height = HeightHandler.INSTANCE.getHeight();
        if (height == -1) return;
        if (HytilsConfig.heightOverlay && blockInfo.getBlockPos().getY() == (height - 1) && blockInfo.getBlock() instanceof BlockColored) {
            MapColor mapColor = blockInfo.getBlock().getMapColor(blockInfo.getWorld().getBlockState(blockInfo.getBlockPos()));
            boolean isClay = blockInfo.getBlock().getMaterial() == Material.rock;
            if (!isClay || check(mapColor.colorIndex)) {
                args.set(5, 1.0F);
                args.set(6, (HytilsConfig.manuallyEditHeightOverlay ? BlockHighlightConfig.colorMap.get(mapColor).get().getRGB() : DarkColorUtils.getCachedDarkColor(mapColor.colorValue)));
            }
        }
    }

    private boolean check(int color) {
        switch (color) {
            case 18:
            case 25:
            case 27:
            case 28:
                return true;
            default:
                return false;
        }
    }
}
