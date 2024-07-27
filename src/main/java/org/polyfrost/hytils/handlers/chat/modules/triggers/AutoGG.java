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

import cc.polyfrost.oneconfig.events.event.WorldLoadEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.PatternHandler;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class AutoGG implements ChatReceiveModule {
    public static AutoGG INSTANCE = new AutoGG();
    private int matchesFound = 0;

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        if (!hasGameEnded(message) || matchesFound > 1) return;
        Multithreading.schedule(() -> UChat.say("/ac " + HytilsConfig.ggMessage), (long) (HytilsConfig.autoGGFirstPhraseDelay * 1000), TimeUnit.MILLISECONDS);
        if (HytilsConfig.autoGGSecondMessage)
            Multithreading.schedule(() -> UChat.say("/ac " + HytilsConfig.ggMessage2), (long) ((HytilsConfig.autoGGSecondPhraseDelay + HytilsConfig.autoGGFirstPhraseDelay) * 1000), TimeUnit.MILLISECONDS);
    }

    private boolean hasGameEnded(String message) {
        if (!PatternHandler.INSTANCE.gameEnd.isEmpty()) {
            for (Pattern triggers : PatternHandler.INSTANCE.gameEnd) {
                if (triggers.matcher(message).matches()) {
                    matchesFound++;
                    return true;
                }
            }
        }

        // TODO: UNTESTED!
        return getLanguage().casualGameEndRegex.matcher(message).matches();
    }

    @Subscribe
    public void onWorldLoad(WorldLoadEvent event) {
        matchesFound = 0;
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
