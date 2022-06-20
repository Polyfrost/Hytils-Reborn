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

package cc.woverflow.hytils.hooks;

import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class LineSeparatorEnhancements {

    private static final HashMap<String, String> cache = new HashMap<>();

    public static boolean isSeparatingChat = false;

    public static void trimLineSeparator(List<IChatComponent> list) {
        if (isSeparatingChat) {
            if (HytilsConfig.lineBreakerTrim) {
                ListIterator<IChatComponent> iterator = list.listIterator();
                while (iterator.hasNext()) {
                    IChatComponent next = iterator.next();
                    if (iterator.hasNext()) {
                        if (isSeparator(EnumChatFormatting.getTextWithoutFormattingCodes(next.getUnformattedText()))) {
                            while (iterator.hasNext()) {
                                IChatComponent nextNext = iterator.next();
                                if (isSeparator(EnumChatFormatting.getTextWithoutFormattingCodes(nextNext.getUnformattedText()))) {
                                    iterator.remove();
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static String cleanLineSeparator(String formattedText) {
        if (HytilsConfig.cleanLineSeparator) {
            String cached = cache.get(formattedText);
            if (cached != null) {
                return cached;
            } else {
                String unformattedText = EnumChatFormatting.getTextWithoutFormattingCodes(formattedText);
                if (isUncleanSeparator(unformattedText) || ((unformattedText.startsWith("-") && unformattedText.endsWith("-")) && formattedText.contains("§m"))) {
                    String processedText = Minecraft.getMinecraft().fontRendererObj.trimStringToWidth(formattedText.replace("▬▬", "§m--").replace("≡≡", "§m--").replace("--", "§m--"), Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatWidth() + 6);
                    cache.put(formattedText, processedText);
                    return processedText;
                }
            }
        }
        return formattedText;
    }

    public static boolean isSeparator(String s) {
        return (s.startsWith("-") && s.endsWith("-")) || (s.startsWith("▬") && s.endsWith("▬")) || (s.startsWith("≡") && s.endsWith("≡"));
    }

    public static boolean isUncleanSeparator(String s) {
        return (s.startsWith("▬") && s.endsWith("▬")) || (s.startsWith("≡") && s.endsWith("≡"));
    }
}
