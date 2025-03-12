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
import org.polyfrost.hytils.events.HypixelLevelupEvent;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

import java.util.regex.Matcher;

public class LevelupEvent implements ChatReceiveModule {

    @Override
    public void onMessageReceived(@NotNull ChatEvent.Receive event) {
        final String unformattedText = event.getFullyUnformattedMessage();
        final Matcher matcher = getLanguage().hypixelLevelUpRegex.matcher(unformattedText.trim());
        if (matcher.find()) {
            if (unformattedText.contains(": ")) return; // TODO: This is a temporary solution to prevent regular player messages from triggering it. Fix the regex!
            final String level = matcher.group("level");
            if (StringUtils.isNumeric(level)) {
                EventManager.INSTANCE.post(new HypixelLevelupEvent(Integer.parseInt(level)));
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.broadcastLevelup;
    }

    @Override
    public int getPriority() {
        return -3;
    }

    @Subscribe
    public void levelUpEvent(HypixelLevelupEvent event) {
        HytilsReborn.INSTANCE.getCommandQueue().queue("/gchat Levelup! I am now Hypixel Level: " + event.getLevel() + "!");
    }
}
