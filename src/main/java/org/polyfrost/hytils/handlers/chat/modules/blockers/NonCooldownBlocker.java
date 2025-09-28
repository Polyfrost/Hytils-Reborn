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

package org.polyfrost.hytils.handlers.chat.modules.blockers;

import dev.deftu.omnicore.api.client.chat.OmniClientChat;
import dev.deftu.textile.minecraft.MCSimpleTextHolder;
import dev.deftu.textile.minecraft.MCTextHolder;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatSendModule;
import org.polyfrost.hytils.util.ranks.RankType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class NonCooldownBlocker implements ChatSendModule {
    private long nonCooldown = 3;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.#"); // only 1 decimal

    @Override
    public @Nullable String onMessageSend(@NotNull String message) {
        if (message.startsWith("/")) return message;
        if (HytilsReborn.INSTANCE.getRank() == RankType.NON) {
            if (nonCooldown < System.currentTimeMillis()) {
                nonCooldown = System.currentTimeMillis() + (getCooldownLengthInSeconds() * 1000L);
                return message;
            } else {
                long secondsLeft = (nonCooldown - System.currentTimeMillis()) / 1000L;
                MCTextHolder<MCSimpleTextHolder> resp = new MCSimpleTextHolder(colorMessage("§eYour freedom of speech is on cooldown. Please wait " + decimalFormat.format(secondsLeft) + " more second" + (secondsLeft == 1 ? "." : "s.")));
                OmniClientChat.displayChatMessage(resp);
                return null;
            }
        }
        return message;
    }

    // Some gamemodes might turn off cooldown for nons in which nons must send many messages such as Guess the Build.
    // As I have a rank, I cannot test this myself.
    private long getCooldownLengthInSeconds() {
        return 3;
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.preventNonCooldown;
    }
}
