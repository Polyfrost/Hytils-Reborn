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

package net.wyvest.hytilities.command;

import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.DisplayName;
import gg.essential.api.commands.SubCommand;
import gg.essential.api.utils.Multithreading;
import net.minecraft.util.EnumChatFormatting;
import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.util.HypixelAPIUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class HytilitiesCommand extends Command {

    private final Set<Alias> hashSet = new HashSet<>();

    @Override
    public Set<Alias> getCommandAliases() {
        return hashSet;
    }

    public HytilitiesCommand() {
        super("hytilities", true);
        hashSet.add(new Alias("hytils"));
        hashSet.add(new Alias("hytilsreborn"));
        hashSet.add(new Alias("hytilitiesreborn"));
    }

    @DefaultHandler
    public void handleDefault() {
        EssentialAPI.getGuiUtil().openScreen(Hytilities.INSTANCE.getConfig().gui());
    }

    @SubCommand("gexp")
    public void getGEXP(@DisplayName("username") Optional<String> username, @DisplayName("type") Optional<String> type) {
        Multithreading.runAsync(() -> {
            if (HytilitiesConfig.apiKey.isEmpty() || !HypixelAPIUtils.isValidKey(HytilitiesConfig.apiKey)) {
                Hytilities.INSTANCE.sendMessage(EnumChatFormatting.RED + "You need to provide a valid API key to run this command! Type /api new to autoset a key.");
                return;
            }
            if (username.isPresent()) {
                if (!type.isPresent()) {
                    if (HypixelAPIUtils.getGEXP(username.get())) {
                        EssentialAPI.getNotifications()
                            .push(Hytilities.MOD_NAME, username.get() + " currently has " + HypixelAPIUtils.gexp + " guild EXP.");
                    } else {
                        EssentialAPI.getNotifications()
                            .push(Hytilities.MOD_NAME, "There was a problem trying to get " + username.get() + "'s GEXP.");
                    }
                } else {
                    if (type.get().equals("daily")) {
                        if (HypixelAPIUtils.getGEXP(username.get())) {
                            EssentialAPI.getNotifications()
                                .push(
                                    Hytilities.MOD_NAME,
                                    username.get() + " currently has " + HypixelAPIUtils.gexp + " daily guild EXP."
                                );
                        } else {
                            EssentialAPI.getNotifications()
                                .push(Hytilities.MOD_NAME, "There was a problem trying to get $username's daily GEXP.");
                        }
                    } else if (type.get().equals("weekly")) {
                        if (HypixelAPIUtils.getWeeklyGEXP(username.get())) {
                            EssentialAPI.getNotifications()
                                .push(
                                    Hytilities.MOD_NAME,
                                    username.get() + " currently has " + HypixelAPIUtils.gexp + " weekly guild EXP."
                                );
                        } else {
                            EssentialAPI.getNotifications()
                                .push(Hytilities.MOD_NAME, "There was a problem trying to get " + username.get() + "'s weekly GEXP.");
                        }
                    } else {
                        EssentialAPI.getNotifications()
                            .push(Hytilities.MOD_NAME, "The type argument was not valid.");
                    }
                }
            } else {
                if (HypixelAPIUtils.getGEXP()) {
                    EssentialAPI.getNotifications()
                        .push(Hytilities.MOD_NAME, "You currently have " + HypixelAPIUtils.gexp + " guild EXP.");
                } else {
                    EssentialAPI.getNotifications()
                        .push(Hytilities.MOD_NAME, "There was a problem trying to get your GEXP.");
                }
            }
        });
    }

    @SubCommand("winstreak")
    public void getWinstreak(@DisplayName("username") Optional<String> username, @DisplayName("gamemode") Optional<String> gamemode) {
        Multithreading.runAsync(() -> {
            if (HytilitiesConfig.apiKey.isEmpty() || !HypixelAPIUtils.isValidKey(HytilitiesConfig.apiKey)) {
                Hytilities.INSTANCE.sendMessage(EnumChatFormatting.RED + "You need to provide a valid API key to run this command! Type /api new to autoset a key.");
                return;
            }
            if (username.isPresent()) {
                if (gamemode.isPresent()) {
                    if (HypixelAPIUtils.getWinstreak(username.get(), gamemode.get())) {
                        EssentialAPI.getNotifications()
                            .push(
                                Hytilities.MOD_NAME,
                                username.get() + " currently has a " + HypixelAPIUtils.winstreak + " winstreak in " + gamemode.get() + "."
                            );
                    } else {
                        EssentialAPI.getNotifications()
                            .push(
                                Hytilities.MOD_NAME,
                                "There was a problem trying to get " + username.get() + "'s winstreak in $gamemode."
                            );
                    }
                } else {
                    if (HypixelAPIUtils.getWinstreak(username.get())) {
                        EssentialAPI.getNotifications()
                            .push(
                                Hytilities.MOD_NAME,
                                username.get() + " currently has a " + HypixelAPIUtils.winstreak + " winstreak."
                            );
                    } else {
                        EssentialAPI.getNotifications()
                            .push(Hytilities.MOD_NAME, "There was a problem trying to get " + username.get() + "'s winstreak.");
                    }
                }
            } else {
                if (HypixelAPIUtils.getWinstreak()) {
                    EssentialAPI.getNotifications()
                        .push(Hytilities.MOD_NAME, "You currently have a " + HypixelAPIUtils.winstreak + " winstreak.");
                } else {
                    EssentialAPI.getNotifications()
                        .push(Hytilities.MOD_NAME, "There was a problem trying to get your winstreak.");
                }
            }
        });
    }

    @SubCommand("setkey")
    public void setKey(@DisplayName("API Key") String apiKey) {
        Multithreading.runAsync(() -> {
            if (HypixelAPIUtils.isValidKey(apiKey)) {
                HytilitiesConfig.apiKey = apiKey;
                Hytilities.INSTANCE.getConfig().markDirty();
                Hytilities.INSTANCE.getConfig().writeData();
                Hytilities.INSTANCE.sendMessage(EnumChatFormatting.GREEN + "Saved API key successfully!");
            } else {
                Hytilities.INSTANCE.sendMessage(EnumChatFormatting.RED + "Invalid API key! Please try again.");
            }
        });
    }
}
