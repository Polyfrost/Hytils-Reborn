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

package org.polyfrost.hytils.config;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.block.material.MapColor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockHighlightConfig {

    @Color(
        name = "White",
        category = "Colors"
    )
    public static OneColor white = new OneColor(MapColor.snowColor.getMapColor(2));

    @Color(
        name = "Orange",
        category = "Colors"
    )
    public static OneColor orange = new OneColor(MapColor.adobeColor.getMapColor(2));

    @Color(
        name = "Magenta",
        category = "Colors"
    )
    public static OneColor magenta = new OneColor(MapColor.magentaColor.getMapColor(2));

    @Color(
        name = "Light Blue",
        category = "Colors"
    )
    public static OneColor lightBlue = new OneColor(MapColor.lightBlueColor.getMapColor(2));

    @Color(
        name = "Yellow",
        category = "Colors"
    )
    public static OneColor yellow = new OneColor(MapColor.yellowColor.getMapColor(2));

    @Color(
        name = "Lime",
        category = "Colors"
    )
    public static OneColor lime = new OneColor(MapColor.limeColor.getMapColor(2));

    @Color(
        name = "Pink",
        category = "Colors"
    )
    public static OneColor pink = new OneColor(MapColor.pinkColor.getMapColor(2));

    @Color(
        name = "Gray",
        category = "Colors"
    )
    public static OneColor gray = new OneColor(MapColor.grayColor.getMapColor(2));

    @Color(
        name = "Silver",
        category = "Colors"
    )
    public static OneColor silver = new OneColor(MapColor.silverColor.getMapColor(2));

    @Color(
        name = "Cyan",
        category = "Colors"
    )
    public static OneColor cyan = new OneColor(MapColor.cyanColor.getMapColor(2));

    @Color(
        name = "Purple",
        category = "Colors"
    )
    public static OneColor purple = new OneColor(MapColor.purpleColor.getMapColor(2));

    @Color(
        name = "Blue",
        category = "Colors"
    )
    public static OneColor blue = new OneColor(MapColor.blueColor.getMapColor(2));

    @Color(
        name = "Brown",
        category = "Colors"
    )
    public static OneColor brown = new OneColor(MapColor.brownColor.getMapColor(2));

    @Color(
        name = "Green",
        category = "Colors"
    )
    public static OneColor green = new OneColor(MapColor.greenColor.getMapColor(2));

    @Color(
        name = "Red",
        category = "Colors"
    )
    public static OneColor red = new OneColor(MapColor.redColor.getMapColor(2));

    @Color(
        name = "Black",
        category = "Colors"
    )
    public static OneColor black = new OneColor(MapColor.blackColor.getMapColor(2));

    public BlockHighlightConfig() {
        colorMap.putIfAbsent(MapColor.snowColor, () -> white);
        colorMap.putIfAbsent(MapColor.adobeColor, () -> orange);
        colorMap.putIfAbsent(MapColor.magentaColor, () -> magenta);
        colorMap.putIfAbsent(MapColor.lightBlueColor, () -> lightBlue);
        colorMap.putIfAbsent(MapColor.yellowColor, () -> yellow);
        colorMap.putIfAbsent(MapColor.limeColor, () -> lime);
        colorMap.putIfAbsent(MapColor.pinkColor, () -> pink);
        colorMap.putIfAbsent(MapColor.grayColor, () -> gray);
        colorMap.putIfAbsent(MapColor.silverColor, () -> silver);
        colorMap.putIfAbsent(MapColor.cyanColor, () -> cyan);
        colorMap.putIfAbsent(MapColor.purpleColor, () -> purple);
        colorMap.putIfAbsent(MapColor.blueColor, () -> blue);
        colorMap.putIfAbsent(MapColor.brownColor, () -> brown);
        colorMap.putIfAbsent(MapColor.greenColor, () -> green);
        colorMap.putIfAbsent(MapColor.redColor, () -> red);
        colorMap.putIfAbsent(MapColor.blackColor, () -> black);
    }

    public static transient final Map<MapColor, Supplier<OneColor>> colorMap = new HashMap<>();
}
