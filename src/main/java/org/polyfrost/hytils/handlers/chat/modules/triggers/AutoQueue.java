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

import net.minecraft.world.World;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.KeyInputEvent;
import org.polyfrost.oneconfig.api.event.v1.events.MouseInputEvent;
import org.polyfrost.oneconfig.api.event.v1.events.WorldLoadEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.LocrawGamesHandler;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

public class AutoQueue implements ChatReceiveModule {
    private String command = null;
    private boolean sentCommand;

    @Override
    public void onMessageReceived(@NotNull ChatEvent.Receive event) {
        if (!HytilsConfig.autoQueue) {
            return;
        }

        final String message = getStrippedMessage(event.getMessage());
        Matcher matcher = getLanguage().autoQueuePrefixGlobalRegex.matcher(message);
        HypixelUtils.Location location = HypixelUtils.getLocation();
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

    @Subscribe
    public void onKeyInput(KeyInputEvent event) {
        if (this.command != null) {
            this.switchGame();
        }
    }

    @Subscribe
    public void onMouseEvent(MouseInputEvent event) {
        if (this.command != null) {
            this.switchGame();
        }
    }

    @Subscribe
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();

        // stop the command from being spammed, to prevent chat from filling with "please do not spam commands"
        if (world.provider.getDimensionId() == 0 && this.sentCommand) {
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

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoQueue;
    }

    /**
     * We want this to activate early so that it catches the queue message.
     */
    @Override
    public int getPriority() {
        return -11;
    }
}
