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

import org.polyfrost.oneconfig.api.hypixel.v0.HypixelAPI;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.LocrawGamesHandler;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

public class AutoQueue implements ChatReceiveModule {
    private String command = null;
    private boolean sentCommand;

    /**
     * We want this to activate early so that it catches the queue message.
     */
    @Override
    public int getPriority() {
        return -11;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (!HytilsConfig.autoQueue) {
            return;
        }

        final String message = getStrippedMessage(event.message);
        Matcher matcher = getLanguage().autoQueuePrefixGlobalRegex.matcher(message);
        HypixelAPI.Location location = HypixelAPI.getLocation();
        if (matcher.matches()) {
            if (location.getMode().isPresent() && location.getGameType().isPresent()) {
                String game = location.getMode().get();
                String gameType = location.getGameType().get().getDatabaseName();
                String value = LocrawGamesHandler.locrawGames.get(gameType.toLowerCase() + "_" + game.toLowerCase());
                if (value != null) {
                    game = value;
                }
                this.command = "/play " + game.toLowerCase();
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoQueue;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (this.command != null) {
            this.switchGame();
        }
    }

    @SubscribeEvent
    public void onMouseEvent(InputEvent.MouseInputEvent event) {
        if (this.command != null) {
            this.switchGame();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        // stop the command from being spammed, to prevent chat from filling with "please do not spam commands"
        if (event.world.provider.getDimensionId() == 0 && this.sentCommand) {
            this.sentCommand = false;
        }
    }

    private void switchGame() {
        Multithreading.schedule(() -> {
            if (!this.sentCommand) {
                HytilsReborn.INSTANCE.getCommandQueue().queue(this.command);
                this.sentCommand = true;
                this.command = null;
            }

        }, HytilsConfig.autoQueueDelay, TimeUnit.SECONDS);
    }
}
