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

package cc.woverflow.hytils.handlers.chat.modules.blockers;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatSendModule;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class NonCooldownBlocker implements ChatSendModule {
    private long nonCooldown = 3;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.#"); // only 1 decimal

    @Override
    public @Nullable String onMessageSend(@NotNull String message) {
        String rank = HytilsReborn.INSTANCE.rank;
        if (message.startsWith("/")) return message;
        if (rank != null && rank.equals("DEFAULT")) {
            if (nonCooldown < System.currentTimeMillis()) {
                nonCooldown = System.currentTimeMillis() + (getCooldownLengthInSeconds() * 1000L);
                return message;
            } else {
                long secondsLeft = (nonCooldown - System.currentTimeMillis()) / 1000L;
                Minecraft.getMinecraft().thePlayer.addChatMessage(colorMessage("§eYour freedom of speech is on cooldown. Please wait " + decimalFormat.format(secondsLeft) + " more second" + (secondsLeft == 1 ? "." : "s.")));
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
