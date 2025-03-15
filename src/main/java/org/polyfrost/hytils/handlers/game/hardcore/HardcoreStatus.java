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

package org.polyfrost.hytils.handlers.game.hardcore;

import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.events.TitleEvent;
import net.minecraft.util.EnumChatFormatting;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

public class HardcoreStatus {
    private boolean danger;

    @Subscribe
    public void onWorldLoad(WorldEvent.Load event) {
        if (this.danger) {
            this.danger = false;
        }
    }

    @Subscribe
    public void onTitle(TitleEvent event) {
        final String unformattedTitle = EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle());
        final String unformattedSubTitle = EnumChatFormatting.getTextWithoutFormattingCodes(event.getSubtitle());

        if (unformattedTitle != null && (unformattedTitle.equals("Your Mini Wither died!") ||
            unformattedTitle.equals("Your Wither died!") ||
            unformattedSubTitle.equals("You will no longer respawn!")) &&
            HytilsConfig.hardcoreHearts) {
            danger = true;
        }
    }

    @Subscribe
    public void onActionbar(ChatEvent.Receive event) { // The forge chat event gets stuff from the action bar as well
        String msg = event.getFullyUnformattedMessage();
        if ((msg.equals("YOUR WITHER IS DEAD") || msg.startsWith("BED DESTRUCTION > Your Bed was") || msg.equals("All beds have been destroyed!")) &&
            HytilsConfig.hardcoreHearts) {
            danger = true;
        }
    }

    public boolean shouldChangeStyle() {
        return this.danger && HytilsConfig.hardcoreHearts;
    }
}
