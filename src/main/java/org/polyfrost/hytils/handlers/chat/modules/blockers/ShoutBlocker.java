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

import dev.deftu.textile.minecraft.MCTextHolder;
import net.hypixel.data.type.GameType;
import net.minecraft.util.IChatComponent;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import org.polyfrost.hytils.handlers.chat.ChatSendModule;
import net.minecraft.client.Minecraft;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.jetbrains.annotations.NotNull;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

import java.text.DecimalFormat;

/**
 * Blocks using /shout in Bedwars and Skywars modes, where there are multiple people in 1 team and also in Capture The Wool.
 * If there are more modes supporting /shout, feel free to add support for them.
 */
public class ShoutBlocker implements ChatSendModule, ChatReceiveModule {
    @Override
    public int getPriority() {
        return -3;
    }

    private long shoutCooldown = 0L;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.#"); // only 1 decimal

    @Override
    public String onMessageSend(@NotNull String message) {
        if (message.startsWith("/shout ")) {
            if (shoutCooldown < System.currentTimeMillis()) {
                shoutCooldown = System.currentTimeMillis() + (getCooldownLengthInSeconds() * 1000L);
                return message;
            } else {
                long secondsLeft = (shoutCooldown - System.currentTimeMillis()) / 1000L;
                Minecraft.getMinecraft().thePlayer.addChatMessage(colorMessage("&eShout command is on cooldown. Please wait " + decimalFormat.format(secondsLeft) + " more second" + (secondsLeft == 1 ? "." : "s.")));
                return null;
            }
        }
        return message;
    }

    private long getCooldownLengthInSeconds() {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (!"LOBBY".equals(location.getMode()) && location.getGameType().isPresent()) {
            switch (location.getGameType().get()) {
                case BEDWARS: {
                    if (!"BEDWARS_EIGHT_ONE".equals(location.getMode())) return 60L;
                    break;
                }
                case SKYWARS: {
                    return 3L;
                }
                case ARCADE: {
                    if ("PVP_CTW".equals(location.getMode().orElse(null))) return 10L;
                    break;
                }
                case UHC: {
                    if ("TEAMS".equals(location.getMode().orElse(null))) return 90L;
                    break;
                }
            }
        }
        return 0L;
    }

    @Override
    public void onMessageReceived(@NotNull ChatEvent.Receive event) {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        IChatComponent message = MCTextHolder.convertToVanilla(event.getMessage());
        if (location.getGameType().orElse(null) == GameType.SKYWARS && message.getFormattedText().equals(getLanguage().cannotShoutBeforeSkywars) || // fun fact: there is no message when you shout after a skywars game
                message.getFormattedText().equals(getLanguage().cannotShoutAfterGame) ||
                message.getFormattedText().equals(getLanguage().cannotShoutBeforeGame) ||
                message.getFormattedText().equals(getLanguage().noSpectatorCommands)
        ) {
            shoutCooldown = 0L;
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.preventShoutingOnCooldown;
    }

}
