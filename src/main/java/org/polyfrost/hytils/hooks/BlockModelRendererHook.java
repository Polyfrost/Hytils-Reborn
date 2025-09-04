/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022, 2023  Polyfrost, Sk1er LLC and contributors
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

package org.polyfrost.hytils.hooks;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.WorldLoadEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.color.ColorUtils;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import org.polyfrost.hytils.util.DarkColorUtils;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import org.polyfrost.hytils.config.BlockHighlightConfig;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.HeightHandler;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

public class BlockModelRendererHook {
    private static BlockModelRendererHook INSTANCE = new BlockModelRendererHook();

    private static boolean isHypixel;

    private BlockModelRendererHook() {
    }

    @Subscribe
    private void onWorldLoad(WorldLoadEvent event) {
        isHypixel = HypixelUtils.INSTANCE.isHypixel();
    }

    public static void initialize() {
        EventManager.INSTANCE.register(INSTANCE);
    }

    public static void handleHeightOverlay(Args args, IBlockState stateIn, BlockPos blockPosIn) {
        if (isHypixel && HytilsConfig.heightOverlay && stateIn.getBlock() instanceof BlockColored) {
            int height = HeightHandler.INSTANCE.getHeight();
            if (height == -1) {
                return;
            }
            MapColor mapColor = stateIn.getBlock().getMapColor(stateIn);
            if (blockPosIn.getY() == (height - 1) && mapColor != null && (!(stateIn.getBlock().getMaterial() == Material.rock) || check(mapColor.colorIndex))) {
                int color = HytilsConfig.manuallyEditHeightOverlay ? BlockHighlightConfig.colorMap.get(mapColor).get().getRGB() : DarkColorUtils.getCachedDarkColor(mapColor.colorValue);
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
