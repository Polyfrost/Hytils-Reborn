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

package net.wyvest.hytilities.handlers.chat.modules.blockers;

import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.handlers.chat.ChatReceiveModule;
import org.jetbrains.annotations.NotNull;

public class DuelsNoStatsChange implements ChatReceiveModule {
    private static final String[] cancelNoStatsChangeMessages = {"Your stats did not change because you /duel'ed your opponent!", "Your stats did not change because you dueled someone in your party!", "No stats will be affected in this round!"};

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        String unformattedText = UTextComponent.Companion.stripFormatting(event.message.getUnformattedText());
        for (String glMessage : cancelNoStatsChangeMessages) {
            if (unformattedText.contains(glMessage)) {
                event.setCanceled(true);
                return;
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.duelsNoStatsChange;
    }

    @Override
    public int getPriority() {
        return -1;
    }
}
