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

import dev.deftu.omnicore.api.client.chat.OmniClientChat;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.oneconfig.api.ui.v1.Notifications;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.events.TitleEvent;
import org.polyfrost.hytils.handlers.cache.PatternHandler;
import org.polyfrost.hytils.handlers.chat.ChatReceiveResetModule;
import org.polyfrost.hytils.mixin.GuiIngameAccessor;
import org.polyfrost.hytils.util.HypixelAPIUtils;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.regex.Pattern;

public class AutoVictory implements ChatReceiveResetModule {
    private boolean victoryDetected = false;

    public AutoVictory() {
        EventManager.INSTANCE.register(this);
    }

    @Override
    public void onMessageReceived(@NotNull ChatEvent.Receive event) {
        String unformattedText = event.getFullyUnformattedMessage();
        if (!PatternHandler.INSTANCE.gameEnd.isEmpty()) {
            if (!victoryDetected) { // prevent victories being detected twice
                Multithreading.submit(() -> { //run this async as getting from the API normally would freeze minecraft
                    for (Pattern triggers : PatternHandler.INSTANCE.gameEnd) {
                        if (triggers.matcher(unformattedText).matches()) {
                            doNotification();
                            return;
                        }
                    }
                });
            }
        }
    }

    @Subscribe
    public void onTitle(TitleEvent event) {
        if (!victoryDetected) {
            final String title = EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle().toLowerCase(Locale.ENGLISH));
            if (title.equals("victory!") || title.equals("game over") || title.equals("game over!") || title.endsWith(" wins!")) {
                Multithreading.submit(this::doNotification);
            }
        }
    }

    private void doNotification() {
        victoryDetected = true;
        if (HytilsConfig.autoGetGEXP) {
            if (HytilsConfig.gexpMode == 0) {
                try {
                    if (HypixelAPIUtils.getGEXP()) {
                        Notifications.INSTANCE
                            .enqueue(Notifications.Type.Info,
                                HytilsReborn.NAME,
                                "You currently have " + HypixelAPIUtils.gexp + " daily guild EXP."
                            );
                        return;
                    }
                } catch (Exception ignored) {

                }
                Notifications.INSTANCE
                    .enqueue(Notifications.Type.Error, HytilsReborn.NAME, "There was a problem trying to get your GEXP.");
            } else {
                try {
                    if (HypixelAPIUtils.getWeeklyGEXP()) {
                        Notifications.INSTANCE
                            .enqueue(Notifications.Type.Info,
                                HytilsReborn.NAME,
                                "You currently have " + HypixelAPIUtils.gexp + " weekly guild EXP."
                            );
                        return;
                    }
                } catch (Exception ignored) {

                }
                Notifications.INSTANCE
                    .enqueue(Notifications.Type.Error, HytilsReborn.NAME, "There was a problem trying to get your GEXP.");
            }
        }
        if (isSupportedMode(HypixelUtils.getLocation()) && HytilsConfig.autoGetWinstreak) {
            try {
                if (HypixelAPIUtils.getWinstreak()) {
                    Notifications.INSTANCE
                        .enqueue(Notifications.Type.Info,
                        HytilsReborn.NAME,
                        "You currently have a " + HypixelAPIUtils.winstreak + " winstreak."
                    );
                    return;
                }
            } catch (Exception ignored) {

            }
            Notifications.INSTANCE
                .enqueue(Notifications.Type.Error, HytilsReborn.NAME, "There was a problem trying to get your winstreak.");
        }
    }

    @Override
    public void reset() {
        victoryDetected = false;
        //TODO: CHeck that this properly overrides the title and subtitle
        OmniClientChat.displayTitle("", "");
    }

    private boolean isSupportedMode(HypixelUtils.Location location) {
        if (location.getGameType().isPresent()) {
            switch (location.getGameType().get()) {
                case BEDWARS:
                case SKYWARS:
                case DUELS:
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoGetWinstreak || HytilsConfig.autoGetGEXP;
    }

    @Override
    public int getPriority() {
        return 3;
    }

}
