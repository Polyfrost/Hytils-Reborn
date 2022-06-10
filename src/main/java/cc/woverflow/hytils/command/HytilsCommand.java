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

package cc.woverflow.hytils.command;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.util.HypixelAPIUtils;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.DisplayName;
import gg.essential.api.commands.SubCommand;
import gg.essential.api.utils.Multithreading;
import net.minecraft.util.EnumChatFormatting;
import cc.woverflow.hytils.config.HytilsConfig;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class HytilsCommand extends Command {

    private final Set<Alias> hashSet = new HashSet<>();

    @Override
    public Set<Alias> getCommandAliases() {
        return hashSet;
    }

    public HytilsCommand() {
        super("hytils", true);
        hashSet.add(new Alias("hytilities"));
        hashSet.add(new Alias("hytilsreborn"));
        hashSet.add(new Alias("hytilitiesreborn"));
        hashSet.add(new Alias("hytil"));
    }

    @DefaultHandler
    public void handleDefault() {
        HytilsReborn.INSTANCE.getConfig().openGui();
    }

    @SubCommand("gexp")
    public void getGEXP(@DisplayName("username") Optional<String> username, @DisplayName("type") Optional<String> type) {
        Multithreading.runAsync(() -> {
            if (HytilsConfig.apiKey.isEmpty() || !HypixelAPIUtils.isValidKey(HytilsConfig.apiKey)) {
                HytilsReborn.INSTANCE.sendMessage(EnumChatFormatting.RED + "You need to provide a valid API key to run this command! Type /api new to autoset a key.");
                return;
            }
            if (username.isPresent()) {
                if (!type.isPresent()) {
                    if (HypixelAPIUtils.getGEXP(username.get())) {
                        EssentialAPI.getNotifications()
                            .push(HytilsReborn.MOD_NAME, username.get() + " currently has " + HypixelAPIUtils.gexp + " guild EXP.");
                    } else {
                        EssentialAPI.getNotifications()
                            .push(HytilsReborn.MOD_NAME, "There was a problem trying to get " + username.get() + "'s GEXP.");
                    }
                } else {
                    if (type.get().equals("daily")) {
                        if (HypixelAPIUtils.getGEXP(username.get())) {
                            EssentialAPI.getNotifications()
                                .push(
                                    HytilsReborn.MOD_NAME,
                                    username.get() + " currently has " + HypixelAPIUtils.gexp + " daily guild EXP."
                                );
                        } else {
                            EssentialAPI.getNotifications()
                                .push(HytilsReborn.MOD_NAME, "There was a problem trying to get $username's daily GEXP.");
                        }
                    } else if (type.get().equals("weekly")) {
                        if (HypixelAPIUtils.getWeeklyGEXP(username.get())) {
                            EssentialAPI.getNotifications()
                                .push(
                                    HytilsReborn.MOD_NAME,
                                    username.get() + " currently has " + HypixelAPIUtils.gexp + " weekly guild EXP."
                                );
                        } else {
                            EssentialAPI.getNotifications()
                                .push(HytilsReborn.MOD_NAME, "There was a problem trying to get " + username.get() + "'s weekly GEXP.");
                        }
                    } else {
                        EssentialAPI.getNotifications()
                            .push(HytilsReborn.MOD_NAME, "The type argument was not valid.");
                    }
                }
            } else {
                if (HypixelAPIUtils.getGEXP()) {
                    EssentialAPI.getNotifications()
                        .push(HytilsReborn.MOD_NAME, "You currently have " + HypixelAPIUtils.gexp + " guild EXP.");
                } else {
                    EssentialAPI.getNotifications()
                        .push(HytilsReborn.MOD_NAME, "There was a problem trying to get your GEXP.");
                }
            }
        });
    }

    @SubCommand("winstreak")
    public void getWinstreak(@DisplayName("username") Optional<String> username, @DisplayName("gamemode") Optional<String> gamemode) {
        Multithreading.runAsync(() -> {
            if (HytilsConfig.apiKey.isEmpty() || !HypixelAPIUtils.isValidKey(HytilsConfig.apiKey)) {
                HytilsReborn.INSTANCE.sendMessage(EnumChatFormatting.RED + "You need to provide a valid API key to run this command! Type /api new to autoset a key.");
                return;
            }
            if (username.isPresent()) {
                if (gamemode.isPresent()) {
                    if (HypixelAPIUtils.getWinstreak(username.get(), gamemode.get())) {
                        EssentialAPI.getNotifications()
                            .push(
                                HytilsReborn.MOD_NAME,
                                username.get() + " currently has a " + HypixelAPIUtils.winstreak + " winstreak in " + gamemode.get() + "."
                            );
                    } else {
                        EssentialAPI.getNotifications()
                            .push(
                                HytilsReborn.MOD_NAME,
                                "There was a problem trying to get " + username.get() + "'s winstreak in $gamemode."
                            );
                    }
                } else {
                    if (HypixelAPIUtils.getWinstreak(username.get())) {
                        EssentialAPI.getNotifications()
                            .push(
                                HytilsReborn.MOD_NAME,
                                username.get() + " currently has a " + HypixelAPIUtils.winstreak + " winstreak."
                            );
                    } else {
                        EssentialAPI.getNotifications()
                            .push(HytilsReborn.MOD_NAME, "There was a problem trying to get " + username.get() + "'s winstreak.");
                    }
                }
            } else {
                if (HypixelAPIUtils.getWinstreak()) {
                    EssentialAPI.getNotifications()
                        .push(HytilsReborn.MOD_NAME, "You currently have a " + HypixelAPIUtils.winstreak + " winstreak.");
                } else {
                    EssentialAPI.getNotifications()
                        .push(HytilsReborn.MOD_NAME, "There was a problem trying to get your winstreak.");
                }
            }
        });
    }

    @SubCommand("setkey")
    public void setKey(@DisplayName("API Key") String apiKey) {
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
