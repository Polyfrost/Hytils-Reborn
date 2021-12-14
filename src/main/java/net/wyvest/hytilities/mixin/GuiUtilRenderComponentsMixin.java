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

import net.wyvest.hytilities.config.HytilitiesConfig;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(GuiUtilRenderComponents.class)
public class GuiUtilRenderComponentsMixin {

    @Dynamic
    @Redirect(method = "splitText", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V"))
    private static void redirectSplit(List<Object> instance, int i, Object e) {
        if (HytilitiesConfig.lineBreakerTrim) {
            String s = EnumChatFormatting.getTextWithoutFormattingCodes(((ChatComponentText) e).getUnformattedTextForChat());
            if ((s.startsWith("-") && s.endsWith("-")) || s.startsWith("▬") && s.endsWith("▬") || (s.startsWith("≡") && s.endsWith("≡"))) {
                return;
            }
        }
        instance.add(i, e);
    }
}
