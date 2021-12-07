/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
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

package net.wyvest.hytilities.handlers.chat.modules.triggers;

import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.handlers.cache.PatternHandler;
import net.wyvest.hytilities.handlers.chat.ChatReceiveResetModule;
import net.wyvest.hytilities.mixin.GuiIngameAccessor;
import net.wyvest.hytilities.util.HypixelAPIUtils;
import net.wyvest.hytilities.util.locraw.LocrawInformation;
import gg.essential.api.EssentialAPI;
import gg.essential.api.utils.Multithreading;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.regex.Pattern;

public class AutoVictory implements ChatReceiveResetModule {
    private boolean victoryDetected = false;
    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.autoGetWinstreak;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        String unformattedText = UTextComponent.Companion.stripFormatting(event.message.getUnformattedText());
        if (PatternHandler.INSTANCE.gameEnd.size() != 0) {
            if (!victoryDetected) { // prevent victories being detected twice
                Multithreading.runAsync(() -> { //run this async as getting from the API normally would freeze minecraft
                    for (Pattern triggers : PatternHandler.INSTANCE.gameEnd) {
                        if (triggers.matcher(unformattedText).matches()) {
                            victoryDetected = true;
                            if (HytilitiesConfig.autoGetGEXP) {
                                if (HytilitiesConfig.gexpMode == 0) {
                                    if (HypixelAPIUtils.getGEXP()) {
                                        EssentialAPI.getNotifications()
                                            .push(
                                                Hytilities.MOD_NAME,
                                                "You currently have " + HypixelAPIUtils.gexp + " daily guild EXP."
                                            );
                                    } else {
                                        EssentialAPI.getNotifications()
                                            .push(Hytilities.MOD_NAME, "There was a problem trying to get your GEXP.");
                                    }
                                } else {
                                    if (HypixelAPIUtils.getWeeklyGEXP()) {
                                        EssentialAPI.getNotifications()
                                            .push(
                                                Hytilities.MOD_NAME,
                                                "You currently have " + HypixelAPIUtils.gexp + " weekly guild EXP."
                                            );
                                    } else {
                                        EssentialAPI.getNotifications()
                                            .push(Hytilities.MOD_NAME, "There was a problem trying to get your GEXP.");
                                    }
                                }
                            }
                            if (isSupportedMode(Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation()) && HytilitiesConfig.autoGetWinstreak) {
                                if (HypixelAPIUtils.getWinstreak()) {
                                    EssentialAPI.getNotifications().push(
                                        Hytilities.MOD_NAME,
                                        "You currently have a " + HypixelAPIUtils.winstreak + " winstreak."
                                    );
                                } else {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, "There was a problem trying to get your winstreak.");
                                }
                            }
                            return;
                        }
                    }
                    String title = ((GuiIngameAccessor) Minecraft.getMinecraft().ingameGUI).getDisplayedTitle().toLowerCase(Locale.ENGLISH);
                    if (title.equals("victory!") || title.equals("game over") || title.equals("game over!") || title.endsWith(" wins") || title.endsWith(" wins!")) {
                        victoryDetected = true;
                        if (HytilitiesConfig.autoGetGEXP) {
                            if (HytilitiesConfig.gexpMode == 0) {
                                if (HypixelAPIUtils.getGEXP()) {
                                    EssentialAPI.getNotifications()
                                        .push(
                                            Hytilities.MOD_NAME,
                                            "You currently have " + HypixelAPIUtils.gexp + " daily guild EXP."
                                        );
                                } else {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, "There was a problem trying to get your GEXP.");
                                }
                            } else {
                                if (HypixelAPIUtils.getWeeklyGEXP()) {
                                    EssentialAPI.getNotifications()
                                        .push(
                                            Hytilities.MOD_NAME,
                                            "You currently have " + HypixelAPIUtils.gexp + " weekly guild EXP."
                                        );
                                } else {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, "There was a problem trying to get your GEXP.");
                                }
                            }
                        }
                        if (isSupportedMode(Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation()) && HytilitiesConfig.autoGetWinstreak) {
                            if (HypixelAPIUtils.getWinstreak()) {
                                EssentialAPI.getNotifications().push(
                                    Hytilities.MOD_NAME,
                                    "You currently have a " + HypixelAPIUtils.winstreak + " winstreak."
                                );
                            } else {
                                EssentialAPI.getNotifications()
                                    .push(Hytilities.MOD_NAME, "There was a problem trying to get your winstreak.");
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void reset() {
        victoryDetected = false;
    }

    private boolean isSupportedMode(LocrawInformation locraw) {
        if (locraw != null && locraw.getGameType() != null) {
            switch (locraw.getGameType()) {
                case BED_WARS:
                case SKY_WARS:
                case DUELS:
                    return true;
            }
        }
        return false;
    }
}
