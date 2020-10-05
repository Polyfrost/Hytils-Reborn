/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
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

package club.sk1er.hytilities.handlers.lobby.tab;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.tweaker.asm.GuiPlayerTabOverlayTransformer;
import club.sk1er.mods.core.util.MinecraftUtils;
import org.objectweb.asm.tree.ClassNode;

/**
 * Used in {@link GuiPlayerTabOverlayTransformer#transform(ClassNode, String)}
 */
@SuppressWarnings("unused")
public class TabNameChanger {

    public static String modifyName(String name) {

        if (MinecraftUtils.isHypixel()) {
            if (HytilitiesConfig.hidePlayerRanksInTab && name.startsWith("[", 2)) {
                // keep the name color if player rank is removed
                // §b[MVP§c+§b] Steve
                String color = "\u00a7" + name.charAt(1);

                // add the rank color, and trim off the player rank
                name = color + name.substring(name.indexOf("]") + 2);
            }

            if (HytilitiesConfig.hideGuildTagsInTab && name.endsWith("]")) {
                // trim off the guild tag
                // e.g. Steve §6[GUILD]
                name = name.substring(0, name.lastIndexOf("[") - 3);
            }
        }

        return name;
    }
}
