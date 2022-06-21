/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
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

package cc.woverflow.hytils.util;

import cc.polyfrost.oneconfig.utils.color.ColorUtils;
import cc.woverflow.hytils.config.HytilsConfig;

import java.util.HashMap;
import java.util.Objects;

public class DarkColorUtils {
    private static final HashMap<Integer, Integer> cache = new HashMap<>();

    public static void invalidateCache() {
        cache.clear();
    }

    public static int getCachedDarkColor(int mapColor) {
        Integer color = cache.get(mapColor);
        if (color == null) {
            cache.put(mapColor, ((0xFF) << 24) |
                ((Math.round(Math.max((float) ColorUtils.getRed(mapColor) * ((float) (1000 - HytilsConfig.overlayAmount) / 1000), 0.0F)) & 0xFF) << 16) |
                ((Math.round(Math.max((float) ColorUtils.getGreen(mapColor) * ((float) (1000 - HytilsConfig.overlayAmount) / 1000), 0.0F)) & 0xFF) << 8) |
                ((Math.round(Math.max((float) ColorUtils.getBlue(mapColor) * ((float) (1000 - HytilsConfig.overlayAmount) / 1000), 0.0F)) & 0xFF)));
            return Objects.requireNonNull(cache.get(mapColor));
        } else {
            return color;
        }
    }
}
