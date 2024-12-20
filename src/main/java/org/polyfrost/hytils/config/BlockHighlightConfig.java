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

import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.polyui.color.PolyColor;
import net.minecraft.block.material.MapColor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

//TODO
public class BlockHighlightConfig {

    //@Color(
    //    title = "White",
    //    category = "Colors"
    //)
    //public static PolyColor white = ColorUtils.rgba(MapColor.snowColor.getMapColor(2));
//
    //@Color(
    //    title = "Orange",
    //    category = "Colors"
    //)
    //public static PolyColor orange = ColorUtils.rgba(MapColor.adobeColor.getMapColor(2));
//
    //@Color(
    //    title = "Magenta",
    //    category = "Colors"
    //)
    //public static PolyColor magenta = ColorUtils.rgba(MapColor.magentaColor.getMapColor(2));
//
    //@Color(
    //    title = "Light Blue",
    //    category = "Colors"
    //)
    //public static PolyColor lightBlue = ColorUtils.rgba(MapColor.lightBlueColor.getMapColor(2));
//
    //@Color(
    //    title = "Yellow",
    //    category = "Colors"
    //)
    //public static PolyColor yellow = ColorUtils.rgba(MapColor.yellowColor.getMapColor(2));
//
    //@Color(
    //    title = "Lime",
    //    category = "Colors"
    //)
    //public static PolyColor lime = ColorUtils.rgba(MapColor.limeColor.getMapColor(2));
//
    //@Color(
    //    title = "Pink",
    //    category = "Colors"
    //)
    //public static PolyColor pink = ColorUtils.rgba(MapColor.pinkColor.getMapColor(2));
//
    //@Color(
    //    title = "Gray",
    //    category = "Colors"
    //)
    //public static PolyColor gray = ColorUtils.rgba(MapColor.grayColor.getMapColor(2));
//
    //@Color(
    //    title = "Silver",
    //    category = "Colors"
    //)
    //public static PolyColor silver = ColorUtils.rgba(MapColor.silverColor.getMapColor(2));
//
    //@Color(
    //    title = "Cyan",
    //    category = "Colors"
    //)
    //public static PolyColor cyan = ColorUtils.rgba(MapColor.cyanColor.getMapColor(2));
//
    //@Color(
    //    title = "Purple",
    //    category = "Colors"
    //)
    //public static PolyColor purple = ColorUtils.rgba(MapColor.purpleColor.getMapColor(2));
//
    //@Color(
    //    title = "Blue",
    //    category = "Colors"
    //)
    //public static PolyColor blue = ColorUtils.rgba(MapColor.blueColor.getMapColor(2));
//
    //@Color(
    //    title = "Brown",
    //    category = "Colors"
    //)
    //public static PolyColor brown = ColorUtils.rgba(MapColor.brownColor.getMapColor(2));
//
    //@Color(
    //    title = "Green",
    //    category = "Colors"
    //)
    //public static PolyColor green = ColorUtils.rgba(MapColor.greenColor.getMapColor(2));
//
    //@Color(
    //    title = "Red",
    //    category = "Colors"
    //)
    //public static PolyColor red = ColorUtils.rgba(MapColor.redColor.getMapColor(2));
//
    //@Color(
    //    title = "Black",
    //    category = "Colors"
    //)
    //public static PolyColor black = ColorUtils.rgba(MapColor.blackColor.getMapColor(2));
//
    //public BlockHighlightConfig() {
    //    colorMap.putIfAbsent(MapColor.snowColor, () -> white);
    //    colorMap.putIfAbsent(MapColor.adobeColor, () -> orange);
    //    colorMap.putIfAbsent(MapColor.magentaColor, () -> magenta);
    //    colorMap.putIfAbsent(MapColor.lightBlueColor, () -> lightBlue);
    //    colorMap.putIfAbsent(MapColor.yellowColor, () -> yellow);
    //    colorMap.putIfAbsent(MapColor.limeColor, () -> lime);
    //    colorMap.putIfAbsent(MapColor.pinkColor, () -> pink);
    //    colorMap.putIfAbsent(MapColor.grayColor, () -> gray);
    //    colorMap.putIfAbsent(MapColor.silverColor, () -> silver);
    //    colorMap.putIfAbsent(MapColor.cyanColor, () -> cyan);
    //    colorMap.putIfAbsent(MapColor.purpleColor, () -> purple);
    //    colorMap.putIfAbsent(MapColor.blueColor, () -> blue);
    //    colorMap.putIfAbsent(MapColor.brownColor, () -> brown);
    //    colorMap.putIfAbsent(MapColor.greenColor, () -> green);
    //    colorMap.putIfAbsent(MapColor.redColor, () -> red);
    //    colorMap.putIfAbsent(MapColor.blackColor, () -> black);
    //}
//
    public static final Map<MapColor, Supplier<PolyColor>> colorMap = new HashMap<>();
}
