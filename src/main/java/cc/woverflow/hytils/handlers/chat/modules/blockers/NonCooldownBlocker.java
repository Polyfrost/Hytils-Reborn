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

package cc.woverflow.hytils.handlers.chat.modules.blockers;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatSendModule;
import cc.woverflow.hytils.util.HypixelAPIUtils;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class NonCooldownBlocker implements ChatSendModule {
    private long nonCooldown = 3;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.#"); // only 1 decimal

    @Override
    public @Nullable String onMessageSend(@NotNull String message) {
        // This needs to be moved out of here as it currently causes a lag spike every time the user sends a message.
        // In my attempts to move it out, the rank never updates and stays null and I don't know why please help :sob:
        String rank = HypixelAPIUtils.getRank(Minecraft.getMinecraft().thePlayer.getName());
        if (!message.startsWith("/") && rank != null && rank.equals("DEFAULT")) {
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
