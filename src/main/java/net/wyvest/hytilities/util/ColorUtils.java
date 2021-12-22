/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wyvest.hytilities.util;

import gg.essential.lib.caffeine.cache.Cache;
import gg.essential.lib.caffeine.cache.Caffeine;
import net.minecraft.block.material.MapColor;
import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.config.HytilitiesConfig;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ColorUtils {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(
        50, 50,
        0L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(), (r) -> new Thread(
        r,
        String.format("%s Cache Thread %s", Hytilities.MOD_NAME, counter.incrementAndGet())
    )
    );

    private static final Cache<Integer, Integer> cache = Caffeine.newBuilder().executor(POOL).maximumSize(100).build();

    public static void invalidateCache() {
        cache.invalidateAll();
    }

    public static int getCachedDarkColor(MapColor mapColor) {
        Integer color = cache.getIfPresent(mapColor.colorIndex);
        if (color == null) {
            cache.put(mapColor.colorIndex, ((0xFF) << 24) |
                ((Math.round(Math.max((float) getRed(mapColor.colorValue) * (1F - HytilitiesConfig.overlayAmount), 0.0F)) & 0xFF) << 16) |
                ((Math.round(Math.max((float) getGreen(mapColor.colorValue) * (1F - HytilitiesConfig.overlayAmount), 0.0F)) & 0xFF) << 8) |
                ((Math.round(Math.max((float) getBlue(mapColor.colorValue) * (1F - HytilitiesConfig.overlayAmount), 0.0F)) & 0xFF)));
            return Objects.requireNonNull(cache.getIfPresent(mapColor.colorIndex));
        } else {
            return color;
        }
    }

    /**
     * @return The red value of the provided RGBA value.
     */
    public static int getRed(int rgba) {
        return (rgba >> 16) & 0xFF;
    }

    /**
     * @return The green value of the provided RGBA value.
     */
    public static int getGreen(int rgba) {
        return (rgba >> 8) & 0xFF;
    }

    /**
     * @return The blue value of the provided RGBA value.
     */
    public static int getBlue(int rgba) {
        return (rgba) & 0xFF;
    }
}
