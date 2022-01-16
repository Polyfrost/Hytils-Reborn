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

package net.wyvest.hytilities.mixin;

import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.wyvest.hytilities.config.HytilitiesConfig;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiUtilRenderComponents.class)
public class GuiUtilRenderComponentsMixin_TrimLineBreaker {

    @Dynamic
    @Redirect(method = "splitText", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private static boolean trimLineSeparator(List<IChatComponent> list, Object obj) {
        boolean value = false;
        if (obj instanceof IChatComponent) {
            value = list.add((IChatComponent) obj);
            if (HytilitiesConfig.lineBreakerTrim) {
                boolean seperatorFound = false;
                int i = -1;
                List<Integer> remove = new ArrayList<>();
                for (IChatComponent component : list) {
                    i++;
                    String s = EnumChatFormatting.getTextWithoutFormattingCodes(component.getUnformattedText());

                    if ((s.startsWith("---") && s.endsWith("---")) || (s.startsWith("▬▬▬") && s.endsWith("▬▬▬")) || (s.startsWith("≡≡≡") && s.endsWith("≡≡≡"))) {
                        if (seperatorFound) {
                            remove.add(i);
                        } else {
                            seperatorFound = true;
                        }
                    } else if (seperatorFound) {
                        seperatorFound = false;
                    }
                }
                for (Integer removed : remove) {
                    list.remove((int) removed);
                }
            }
        }
        return value;
    }
}
