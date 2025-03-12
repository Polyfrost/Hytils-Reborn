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

package org.polyfrost.hytils.handlers.chat.modules.events;

import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.events.HypixelAchievementEvent;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.jetbrains.annotations.NotNull;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

public class AchievementEvent implements ChatReceiveModule {

    private final Set<String> achievementsGotten = new HashSet<>();

    @Override
    public void onMessageReceived(@NotNull ChatEvent.Receive event) {
        final String unformattedText = event.getFullyUnformattedMessage();
        final Matcher matcher = getLanguage().achievementRegex.matcher(unformattedText);
        if (matcher.matches()) {
            final String achievement = matcher.group("achievement");
            if (!achievementsGotten.contains(achievement)) {
                EventManager.INSTANCE.post(new HypixelAchievementEvent(achievement));

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

    @Subscribe
    public void onAchievementGet(HypixelAchievementEvent event) {
        HytilsReborn.INSTANCE.getCommandQueue().queue("/gchat Achievement unlocked! I unlocked the " + event.getAchievement() + " achievement!");
    }

}
