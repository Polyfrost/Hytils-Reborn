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

import cc.woverflow.hytils.HytilsReborn;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import net.minecraft.block.material.MapColor;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockHighlightConfig extends Vigilant {

    @Property(
        type = PropertyType.COLOR,
        name = "White",
        category = "Colors",
        allowAlpha = false
    )
    public static Color white = new Color(MapColor.snowColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Orange",
        category = "Colors",
        allowAlpha = false
    )
    public static Color orange = new Color(MapColor.adobeColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Magenta",
        category = "Colors",
        allowAlpha = false
    )
    public static Color magenta = new Color(MapColor.magentaColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Light Blue",
        category = "Colors",
        allowAlpha = false
    )
    public static Color lightBlue = new Color(MapColor.lightBlueColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Yellow",
        category = "Colors",
        allowAlpha = false
    )
    public static Color yellow = new Color(MapColor.yellowColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Lime",
        category = "Colors",
        allowAlpha = false
    )
    public static Color lime = new Color(MapColor.limeColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Pink",
        category = "Colors",
        allowAlpha = false
    )
    public static Color pink = new Color(MapColor.pinkColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Gray",
        category = "Colors",
        allowAlpha = false
    )
    public static Color gray = new Color(MapColor.grayColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Silver",
        category = "Colors",
        allowAlpha = false
    )
    public static Color silver = new Color(MapColor.silverColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Cyan",
        category = "Colors",
        allowAlpha = false
    )
    public static Color cyan = new Color(MapColor.cyanColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Purple",
        category = "Colors",
        allowAlpha = false
    )
    public static Color purple = new Color(MapColor.purpleColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Blue",
        category = "Colors",
        allowAlpha = false
    )
    public static Color blue = new Color(MapColor.blueColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Brown",
        category = "Colors",
        allowAlpha = false
    )
    public static Color brown = new Color(MapColor.brownColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Green",
        category = "Colors",
        allowAlpha = false
    )
    public static Color green = new Color(MapColor.greenColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Red",
        category = "Colors",
        allowAlpha = false
    )
    public static Color red = new Color(MapColor.redColor.colorValue);

    @Property(
        type = PropertyType.COLOR,
        name = "Black",
        category = "Colors",
        allowAlpha = false
    )
    public static Color black = new Color(MapColor.blackColor.colorValue);

    public BlockHighlightConfig() {
        super(new File(HytilsReborn.INSTANCE.modDir, "blockhighlight.toml"));
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
        initialize();
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

    public static final Map<MapColor, Supplier<Color>> colorMap = new HashMap<>();
}
