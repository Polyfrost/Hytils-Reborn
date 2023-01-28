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

package cc.woverflow.hytils.mixin;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiPlayerTabOverlay.class)
public class GuiPlayerTabOverlayMixin_RemoveAds {

    @Shadow
    private IChatComponent footer;

    @Shadow
    private IChatComponent header;

    @Inject(method = "renderPlayerlist", at = @At("HEAD"))
    private void renderPlayerlist(int width, Scoreboard scoreboard, ScoreObjective objective, CallbackInfo ci) {
        if (HytilsConfig.hideAdsInTab && HypixelUtils.INSTANCE.isHypixel()) {
            if (footer != null) {
                String formattedFooter = footer.getFormattedText().replaceAll("(\n\u00a7r)?\u00a7r\u00a7aRanks, Boosters & MORE! \u00a7r\u00a7c\u00a7lSTORE\\.HYPIXEL\\.NET", "");
                if (formattedFooter.endsWith("\u00a7r")) footer = new ChatComponentText(formattedFooter.substring(0, formattedFooter.length() - 2).trim());
            }
            if (header != null) {
                String formattedHeader = header.getFormattedText().replaceAll("\u00a7r\u00a7bYou are playing on \u00a7r\u00a7e\u00a7lMC\\.HYPIXEL\\.NET(\u00a7r\n)?", "");
                if (formattedHeader.endsWith("\u00a7r")) header = new ChatComponentText(formattedHeader.substring(0, formattedHeader.length() - 2).trim());
            }
        }
    }
}
