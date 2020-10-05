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

package club.sk1er.hytilities.handlers.chat.events;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.events.HypixelAchievementEvent;
import club.sk1er.hytilities.handlers.chat.ChatReceiveModule;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class AchievementEvent implements ChatReceiveModule {

    private final List<String> achievementsGotten = new ArrayList<>();

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        String unformattedText = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

        Matcher matcher = getLanguage().achievementRegex.matcher(unformattedText);
        if (matcher.matches()) {
            String achievement = matcher.group("achievement");

            if (!achievementsGotten.contains(achievement)) {
                MinecraftForge.EVENT_BUS.post(new HypixelAchievementEvent(achievement));

                // check to stop spamming of guild chat if achievement is broken and you get it many times
                achievementsGotten.add(achievement);
            }
        }
    }

    @Override
    public boolean isReceiveModuleEnabled() {
        return true;
    }

    @SubscribeEvent
    public void onAchievementGet(HypixelAchievementEvent event) {
        if (HytilitiesConfig.broadcastAchievements) {
            Hytilities.INSTANCE.getCommandQueue().queue("/gchat Achievement unlocked! I unlocked the " + event.getAchievement() + " achievement!");
        }
    }
}
