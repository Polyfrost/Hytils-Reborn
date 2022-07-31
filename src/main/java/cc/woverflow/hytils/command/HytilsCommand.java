/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
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

package cc.woverflow.hytils.command;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.Name;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.command.parser.*;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.util.HypixelAPIUtils;
import cc.woverflow.hytils.util.notification.NotificationManager;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@Command(value = "hytils", aliases = {"hytilities", "hytilsreborn", "hytilitiesreborn", "hytil"})
public class HytilsCommand {

    static {
        CommandManager.INSTANCE.addParser(new GEXPTypeParser());
        CommandManager.INSTANCE.addParser(new WinstreakTypeParser());
    }

    @Main
    private static void handleDefault() {
        HytilsReborn.INSTANCE.getConfig().openGui();
    }

    @SubCommand("gexp")
    private static class GEXPCommand {
        @Main
        private static void getGEXP() {
            getGEXP(null, null);
        }

        @Main
        private static void getGEXP(@Name("username") PlayerName username) {
            getGEXP(username, null);
        }

        @SuppressWarnings("SameParameterValue")
        @Main
        private static void getGEXP(@Name("username") @Nullable PlayerName username, @Name("type") @Nullable GEXPType type) {
            Multithreading.runAsync(() -> {
                if (HytilsConfig.apiKey.isEmpty() || !HypixelAPIUtils.isValidKey(HytilsConfig.apiKey)) {
                    HytilsReborn.INSTANCE.sendMessage(EnumChatFormatting.RED + "You need to provide a valid API key to run this command! Type /api new to autoset a key.");
                    return;
                }
                if (username != null) {
                    if (type == null) {
                        if (HypixelAPIUtils.getGEXP(username.name)) {
                            NotificationManager.INSTANCE
                                .push(HytilsReborn.MOD_NAME, username.name + " currently has " + HypixelAPIUtils.gexp + " guild EXP.");
                        } else {
                            NotificationManager.INSTANCE
                                .push(HytilsReborn.MOD_NAME, "There was a problem trying to get " + username.name + "'s GEXP.");
                        }
                    } else {
                        switch (type) {
                            case DAILY:
                                if (HypixelAPIUtils.getGEXP(username.name)) {
                                    NotificationManager.INSTANCE
                                        .push(
                                            HytilsReborn.MOD_NAME,
                                            username.name + " currently has " + HypixelAPIUtils.gexp + " daily guild EXP."
                                        );
                                } else {
                                    NotificationManager.INSTANCE
                                        .push(HytilsReborn.MOD_NAME, "There was a problem trying to get $username's daily GEXP.");
                                }
                            case WEEKLY:
                                if (HypixelAPIUtils.getWeeklyGEXP(username.name)) {
                                    NotificationManager.INSTANCE
                                        .push(
                                            HytilsReborn.MOD_NAME,
                                            username.name + " currently has " + HypixelAPIUtils.gexp + " weekly guild EXP."
                                        );
                                } else {
                                    NotificationManager.INSTANCE
                                        .push(HytilsReborn.MOD_NAME, "There was a problem trying to get " + username.name + "'s weekly GEXP.");
                                }
                        }
                    }
                } else {
                    if (HypixelAPIUtils.getGEXP()) {
                        NotificationManager.INSTANCE
                            .push(HytilsReborn.MOD_NAME, "You currently have " + HypixelAPIUtils.gexp + " guild EXP.");
                    } else {
                        NotificationManager.INSTANCE
                            .push(HytilsReborn.MOD_NAME, "There was a problem trying to get your GEXP.");
                    }
                }
            });
        }
    }

    @SubCommand("winstreak")
    private static class WinStreakCommand {
        @Main
        private static void getWinStreak() {
            getWinStreak(null, null);
        }

        @Main
        private static void getWinStreak(@Name("username") PlayerName username) {
            getWinStreak(username, null);
        }

        @SuppressWarnings("SameParameterValue")
        @Main
        private static void getWinStreak(@Name("username") @Nullable PlayerName player, @Name("type") @Nullable WinstreakType gamemode) {
            Multithreading.runAsync(() -> {
                if (HytilsConfig.apiKey.isEmpty() || !HypixelAPIUtils.isValidKey(HytilsConfig.apiKey)) {
                    HytilsReborn.INSTANCE.sendMessage(EnumChatFormatting.RED + "You need to provide a valid API key to run this command! Type /api new to autoset a key.");
                    return;
                }
                if (player != null) {
                    if (gamemode != null) {
                        if (HypixelAPIUtils.getWinstreak(player.name, gamemode.name())) {
                            NotificationManager.INSTANCE
                                .push(
                                    HytilsReborn.MOD_NAME,
                                    player.name + " currently has a " + HypixelAPIUtils.winstreak + " winstreak in " + gamemode.name().toLowerCase(Locale.ENGLISH) + "."
                                );
                        } else {
                            NotificationManager.INSTANCE
                                .push(
                                    HytilsReborn.MOD_NAME,
                                    "There was a problem trying to get " + player.name + "'s winstreak in $gamemode."
                                );
                        }
                    } else {
                        if (HypixelAPIUtils.getWinstreak(player.name)) {
                            NotificationManager.INSTANCE
                                .push(
                                    HytilsReborn.MOD_NAME,
                                    player.name + " currently has a " + HypixelAPIUtils.winstreak + " winstreak."
                                );
                        } else {
                            NotificationManager.INSTANCE
                                .push(HytilsReborn.MOD_NAME, "There was a problem trying to get " + player.name + "'s winstreak.");
                        }
                    }
                } else {
                    if (HypixelAPIUtils.getWinstreak()) {
                        NotificationManager.INSTANCE
                            .push(HytilsReborn.MOD_NAME, "You currently have a " + HypixelAPIUtils.winstreak + " winstreak.");
                    } else {
                        NotificationManager.INSTANCE
                            .push(HytilsReborn.MOD_NAME, "There was a problem trying to get your winstreak.");
                    }
                }
            });
        }
    }

    @SubCommand("setkey")
    private static class SetKeyCommand {
        @Main
        private static void setKey(@Name("API Key") String apiKey) {
            Multithreading.runAsync(() -> {
                if (HypixelAPIUtils.isValidKey(apiKey)) {
                    HytilsConfig.apiKey = apiKey;
                    HytilsReborn.INSTANCE.getConfig().save();
                    HytilsReborn.INSTANCE.sendMessage(EnumChatFormatting.GREEN + "Saved API key successfully!");
                } else {
                    HytilsReborn.INSTANCE.sendMessage(EnumChatFormatting.RED + "Invalid API key! Please try again.");
                }
            });
        }
    }
}
