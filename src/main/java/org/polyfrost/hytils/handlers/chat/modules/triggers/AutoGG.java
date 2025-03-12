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

package org.polyfrost.hytils.handlers.chat.modules.triggers;

import net.minecraft.util.EnumChatFormatting;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.jetbrains.annotations.NotNull;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.PatternHandler;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.universal.UChat;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class AutoGG implements ChatReceiveModule {
    private static final String[] ggMessagesOne = {"gg", "GG", "gf", "Good Game", "Good Fight", "Good Round! :D"};
    private static final String[] ggMessagesTwo = {"Have a good day!", "<3", "AutoGG By Hytils Reborn!", "gf", "Good Fight", "Good Round", ":D", "Well played!", "wp"};

    private static String getGGMessageOne() {
        return ggMessagesOne[HytilsConfig.autoGGMessage];
    }
    private static String getGGMessageTwo() {
        return ggMessagesTwo[HytilsConfig.autoGGMessage2];
    }

    @Override
    public void onMessageReceived(@NotNull ChatEvent.Receive event) {
        String message = event.getFullyUnformattedMessage();
        if (!hasGameEnded(message)) return;
        Multithreading.schedule(() -> UChat.say("/ac " + getGGMessageOne()), (long) (HytilsConfig.autoGGFirstPhraseDelay * 1000), TimeUnit.MILLISECONDS);
        if (HytilsConfig.autoGGSecondMessage)
            Multithreading.schedule(() -> UChat.say("/ac " + getGGMessageTwo()), (long) ((HytilsConfig.autoGGSecondPhraseDelay + HytilsConfig.autoGGFirstPhraseDelay) * 1000), TimeUnit.MILLISECONDS);
    }

    private boolean hasGameEnded(String message) {
        if (!PatternHandler.INSTANCE.gameEnd.isEmpty()) {
            for (Pattern triggers : PatternHandler.INSTANCE.gameEnd) {
                if (triggers.matcher(message).matches()) {
                    return true;
                }
            }
        }

        // TODO: UNTESTED!
        return getLanguage().casualGameEndRegex.matcher(message).matches();
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoGG && (!HytilsReborn.INSTANCE.isSk1erAutoGG || !club.sk1er.mods.autogg.AutoGG.INSTANCE.getAutoGGConfig().isModEnabled()); // If Sk1er's AutoGG is enabled, we don't want to interfere with it.
    }

    @Override
    public int getPriority() {
        return 3;
    }
}
