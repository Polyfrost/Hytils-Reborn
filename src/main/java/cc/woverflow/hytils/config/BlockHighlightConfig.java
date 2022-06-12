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

package cc.woverflow.hytils.config;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.woverflow.hytils.HytilsReborn;
import net.minecraft.block.material.MapColor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockHighlightConfig {

    @Color(
        name = "White",
        category = "Colors"
    )
    public static OneColor white = new OneColor(MapColor.snowColor.colorValue);

    @Color(
        name = "Orange",
        category = "Colors"
    )
    public static OneColor orange = new OneColor(MapColor.adobeColor.colorValue);

    @Color(
        name = "Magenta",
        category = "Colors"
    )
    public static OneColor magenta = new OneColor(MapColor.magentaColor.colorValue);

    @Color(
        name = "Light Blue",
        category = "Colors"
    )
    public static OneColor lightBlue = new OneColor(MapColor.lightBlueColor.colorValue);

    @Color(
        name = "Yellow",
        category = "Colors"
    )
    public static OneColor yellow = new OneColor(MapColor.yellowColor.colorValue);

    @Color(
        name = "Lime",
        category = "Colors"
    )
    public static OneColor lime = new OneColor(MapColor.limeColor.colorValue);

    @Color(
        name = "Pink",
        category = "Colors"
    )
    public static OneColor pink = new OneColor(MapColor.pinkColor.colorValue);

    @Color(
        name = "Gray",
        category = "Colors"
    )
    public static OneColor gray = new OneColor(MapColor.grayColor.colorValue);

    @Color(
        name = "Silver",
        category = "Colors"
    )
    public static OneColor silver = new OneColor(MapColor.silverColor.colorValue);

    @Color(
        name = "Cyan",
        category = "Colors"
    )
    public static OneColor cyan = new OneColor(MapColor.cyanColor.colorValue);

    @Color(
        name = "Purple",
        category = "Colors"
    )
    public static OneColor purple = new OneColor(MapColor.purpleColor.colorValue);

    @Color(
        name = "Blue",
        category = "Colors"
    )
    public static OneColor blue = new OneColor(MapColor.blueColor.colorValue);

    @Color(
        name = "Brown",
        category = "Colors"
    )
    public static OneColor brown = new OneColor(MapColor.brownColor.colorValue);

    @Color(
        name = "Green",
        category = "Colors"
    )
    public static OneColor green = new OneColor(MapColor.greenColor.colorValue);

    @Color(
        name = "Red",
        category = "Colors"
    )
    public static OneColor red = new OneColor(MapColor.redColor.colorValue);

    @Color(
        name = "Black",
        category = "Colors"
    )
    public static OneColor black = new OneColor(MapColor.blackColor.colorValue);

    public BlockHighlightConfig() {
        try {
            File modDir = HytilsReborn.INSTANCE.modDir;
            File oldModDir = new File(modDir.getParentFile(), "Hytilities Reborn");
            File oldBlockConfig = new File(oldModDir, "blockhighlight.toml");
            if (oldBlockConfig.exists()) {
                FileUtils.writeStringToFile(new File(modDir, "blockhighlight.toml"), FileUtils.readFileToString(oldBlockConfig, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                if (!oldBlockConfig.renameTo(new File(modDir, "blockhighlight_backup.toml"))) {
                    Files.move(oldBlockConfig.toPath(), modDir.toPath().resolve("blockhighlight_backup.toml"), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
