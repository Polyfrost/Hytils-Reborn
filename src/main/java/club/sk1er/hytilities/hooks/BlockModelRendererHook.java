/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  Sk1er LLC
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

package club.sk1er.hytilities.hooks;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.cache.HeightHandler;
import club.sk1er.hytilities.util.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

public class BlockModelRendererHook {

    public static void handleHeightOverlay(Args args, IBlockAccess worldIn, IBlockState stateIn, BlockPos blockPosIn) {
        if (HytilitiesConfig.heightOverlay && stateIn.getBlock() instanceof BlockColored) {
            int height = HeightHandler.INSTANCE.height;
            if (height == -1) {
                return;
            }
            MapColor mapColor = stateIn.getBlock().getMapColor(worldIn.getBlockState(blockPosIn));
            if (blockPosIn.getY() == (height - 1) && mapColor != null && (!(stateIn.getBlock().getMaterial() == Material.rock) || check(mapColor.colorIndex))) {
                int color = ColorUtils.getCachedDarkColor(mapColor);
                args.set(0, (float) ColorUtils.getRed(color) / 255);
                args.set(1, (float) ColorUtils.getGreen(color) / 255);
                args.set(2, (float) ColorUtils.getBlue(color) / 255);
            }
        }
    }

    public static void handleHeightOverlay(Args args, IBlockAccess worldIn, Block stateIn, BlockPos blockPosIn) {
        if (HytilitiesConfig.heightOverlay && stateIn instanceof BlockColored) {
            int height = HeightHandler.INSTANCE.height;
            if (height == -1) {
                return;
            }
            MapColor mapColor = stateIn.getMapColor(worldIn.getBlockState(blockPosIn));
            if (blockPosIn.getY() == (height - 1) && mapColor != null && (!(stateIn.getMaterial() == Material.rock) || check(mapColor.colorIndex))) {
                int color = ColorUtils.getCachedDarkColor(mapColor);
                args.set(0, (float) ColorUtils.getRed(color) / 255);
                args.set(1, (float) ColorUtils.getGreen(color) / 255);
                args.set(2, (float) ColorUtils.getBlue(color) / 255);
            }
        }
    }

    private static boolean check(int color) {
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
