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

package org.polyfrost.hytils.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.polyui.color.ColorUtils;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DarkColorUtils {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(
        50, 50,
        0L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(), (r) -> new Thread(
        r,
        String.format("%s Cache Thread %s", HytilsReborn.NAME, counter.incrementAndGet())
    )
    );

    private static final Cache<Integer, Integer> cache = Caffeine.newBuilder().executor(POOL).maximumSize(100).build();

    public static void invalidateCache() {
        cache.invalidateAll();
    }

    public static int getCachedDarkColor(int mapColor) {
        Integer color = cache.getIfPresent(mapColor);
        if (color == null) {
            cache.put(mapColor, ((0xFF) << 24) |
                ((Math.round(Math.max((float) ColorUtils.getRed(mapColor) * ((float) (1000 - HytilsConfig.overlayAmount) / 1000), 0.0F)) & 0xFF) << 16) |
                ((Math.round(Math.max((float) ColorUtils.getGreen(mapColor) * ((float) (1000 - HytilsConfig.overlayAmount) / 1000), 0.0F)) & 0xFF) << 8) |
                ((Math.round(Math.max((float) ColorUtils.getBlue(mapColor) * ((float) (1000 - HytilsConfig.overlayAmount) / 1000), 0.0F)) & 0xFF)));
            return Objects.requireNonNull(cache.getIfPresent(mapColor));
        } else {
            return color;
        }
    }
}
