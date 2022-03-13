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

package cc.woverflow.hytils.handlers.chat.modules.events;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.events.HypixelAchievementEvent;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

public class AchievementEvent implements ChatReceiveModule {

    private final Set<String> achievementsGotten = new HashSet<>();

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final String unformattedText = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        final Matcher matcher = getLanguage().achievementRegex.matcher(unformattedText);
        if (matcher.matches()) {
            final String achievement = matcher.group("achievement");
            if (!achievementsGotten.contains(achievement)) {
                MinecraftForge.EVENT_BUS.post(new HypixelAchievementEvent(achievement));

                // check to stop spamming of guild chat if achievement is broken and you get it many times
                achievementsGotten.add(achievement);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.broadcastAchievements;
    }

    @Override
    public int getPriority() {
        return -3;
    }

    @SubscribeEvent
    public void onAchievementGet(HypixelAchievementEvent event) {
        HytilsReborn.INSTANCE.getCommandQueue().queue("/gchat Achievement unlocked! I unlocked the " + event.getAchievement() + " achievement!");
    }

}
