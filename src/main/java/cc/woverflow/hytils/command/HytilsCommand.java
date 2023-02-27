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

package cc.woverflow.hytils.command;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.Notifications;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Description;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.command.parser.GEXPType;
import cc.woverflow.hytils.command.parser.GEXPTypeParser;
import cc.woverflow.hytils.command.parser.WinstreakType;
import cc.woverflow.hytils.command.parser.WinstreakTypeParser;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.util.HypixelAPIUtils;
import com.mojang.authlib.GameProfile;
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
    private void handleDefault() {
        HytilsReborn.INSTANCE.getConfig().openGui();
    }

    @SubCommand(description = "Shows your guild experience", aliases = {"guildexp", "guildexperience"})
    @SuppressWarnings("SameParameterValue")
    private void gexp(@Description("username") @Nullable GameProfile player, @Description("type") @Nullable GEXPType type) {
        Multithreading.runAsync(() -> {
            if (HytilsConfig.apiKey.isEmpty() || !HypixelAPIUtils.isValidKey(HytilsConfig.apiKey)) {
                HytilsReborn.INSTANCE.sendMessage(EnumChatFormatting.RED + "You need to provide a valid API key to run this command! Type /api new to autoset a key.");
                return;
            }
            if (player != null) {
                if (type == null) {
                    if (HypixelAPIUtils.getGEXP(player.getName())) {
                        Notifications.INSTANCE
                            .send(HytilsReborn.MOD_NAME, player.getName() + " currently has " + HypixelAPIUtils.gexp + " guild EXP.");
                    } else {
                        Notifications.INSTANCE
                            .send(HytilsReborn.MOD_NAME, "There was a problem trying to get " + player.getName() + "'s GEXP.");
                    }
                } else {
                    switch (type) {
                        case DAILY:
                            if (HypixelAPIUtils.getGEXP(player.getName())) {
                                Notifications.INSTANCE
                                    .send(
                                        HytilsReborn.MOD_NAME,
                                        player.getName() + " currently has " + HypixelAPIUtils.gexp + " daily guild EXP."
                                    );
                            } else {
                                Notifications.INSTANCE
                                    .send(HytilsReborn.MOD_NAME, "There was a problem trying to get " + player.getName() + "'s daily GEXP.");
                            }
                        case WEEKLY:
                            if (HypixelAPIUtils.getWeeklyGEXP(player.getName())) {
                                Notifications.INSTANCE
                                    .send(
                                        HytilsReborn.MOD_NAME,
                                        player.getName() + " currently has " + HypixelAPIUtils.gexp + " weekly guild EXP."
                                    );
                            } else {
                                Notifications.INSTANCE
                                    .send(HytilsReborn.MOD_NAME, "There was a problem trying to get " + player.getName() + "'s weekly GEXP.");
                            }
                    }
                }
            } else {
                if (HypixelAPIUtils.getGEXP()) {
                    Notifications.INSTANCE
                        .send(HytilsReborn.MOD_NAME, "You currently have " + HypixelAPIUtils.gexp + " guild EXP.");
                } else {
                    Notifications.INSTANCE
                        .send(HytilsReborn.MOD_NAME, "There was a problem trying to get your GEXP.");
                }
            }
        });
    }

    @SubCommand(description = "Shows your winstreak", aliases = {"winstreak", "ws"})
    @SuppressWarnings("SameParameterValue")
    private void winstreak(@Description("username") @Nullable GameProfile player, @Description("type") @Nullable WinstreakType gamemode) {
        Multithreading.runAsync(() -> {
            if (HytilsConfig.apiKey.isEmpty() || !HypixelAPIUtils.isValidKey(HytilsConfig.apiKey)) {
                HytilsReborn.INSTANCE.sendMessage(EnumChatFormatting.RED + "You need to provide a valid API key to run this command! Type /api new to autoset a key.");
                return;
            }
            if (player != null) {
                if (gamemode != null) {
                    if (HypixelAPIUtils.getWinstreak(player.getName(), gamemode.name())) {
                        Notifications.INSTANCE
                            .send(
                                HytilsReborn.MOD_NAME,
                                player.getName() + " currently has a " + HypixelAPIUtils.winstreak + " winstreak in " + gamemode.name().toLowerCase(Locale.ENGLISH) + "."
                            );
                    } else {
                        Notifications.INSTANCE
                            .send(
                                HytilsReborn.MOD_NAME,
                                "There was a problem trying to get " + player.getName() + "'s winstreak in $gamemode."
                            );
                    }
                } else {
                    if (HypixelAPIUtils.getWinstreak(player.getName())) {
                        Notifications.INSTANCE
                            .send(
                                HytilsReborn.MOD_NAME,
                                player.getName() + " currently has a " + HypixelAPIUtils.winstreak + " winstreak."
                            );
                    } else {
                        Notifications.INSTANCE
                            .send(HytilsReborn.MOD_NAME, "There was a problem trying to get " + player.getName() + "'s winstreak.");
                    }
                }
            } else {
                if (HypixelAPIUtils.getWinstreak()) {
                    Notifications.INSTANCE
                        .send(HytilsReborn.MOD_NAME, "You currently have a " + HypixelAPIUtils.winstreak + " winstreak.");
                } else {
                    Notifications.INSTANCE
                        .send(HytilsReborn.MOD_NAME, "There was a problem trying to get your winstreak.");
                }
            }
        });
    }

    @SubCommand(description = "Sets your API key.", aliases = "setkey")
    private static void key(@Description("API Key") String apiKey) {
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
