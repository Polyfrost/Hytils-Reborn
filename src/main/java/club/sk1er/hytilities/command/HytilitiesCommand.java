/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
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

package club.sk1er.hytilities.command;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.cache.HeightHandler;
import club.sk1er.hytilities.util.HypixelAPIUtils;
import gg.essential.api.EssentialAPI;
import gg.essential.api.utils.Multithreading;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HytilitiesCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "hytilities";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("hytils");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + Hytilities.MOD_NAME;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            EssentialAPI.getGuiUtil().openScreen(Hytilities.INSTANCE.getConfig().gui());
        } else {
            switch (args[0].toLowerCase(Locale.ENGLISH)) {
                case "gexp": {
                    Multithreading.runAsync(() -> {
                        if (HytilitiesConfig.apiKey.isEmpty() || !HypixelAPIUtils.isValidKey(HytilitiesConfig.apiKey)) {
                            Hytilities.INSTANCE.sendMessage(EnumChatFormatting.RED + "You need to provide a valid API key to run this command! Type /api new to autoset a key.");
                            return;
                        }
                        switch (args.length) {
                            case 3: {
                                switch (args[2]) {
                                    case "daily": {
                                        if (HypixelAPIUtils.getGEXP(args[1])) {
                                            EssentialAPI.getNotifications()
                                                .push(Hytilities.MOD_NAME, args[1] + " currently has " + HypixelAPIUtils.gexp + " daily guild EXP.");
                                        } else {
                                            EssentialAPI.getNotifications()
                                                .push(Hytilities.MOD_NAME, "There was a problem trying to get " + args[1] + "'s daily GEXP.");
                                        }
                                        return;
                                    }
                                    case "weekly": {
                                        if (HypixelAPIUtils.getWeeklyGEXP(args[1])) {
                                            EssentialAPI.getNotifications()
                                                .push(Hytilities.MOD_NAME, args[1] + " currently has " + HypixelAPIUtils.gexp + " weekly guild EXP.");
                                        } else {
                                            EssentialAPI.getNotifications()
                                                .push(Hytilities.MOD_NAME, "There was a problem trying to get " + args[1] + "'s weekly GEXP.");
                                        }
                                        return;
                                    }
                                    default: {
                                        Hytilities.INSTANCE.sendMessage("Invalid command usage.");
                                        return;
                                    }
                                }
                            }
                            case 2: {
                                if (HypixelAPIUtils.getGEXP(args[1])) {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, args[1] + " currently has " + HypixelAPIUtils.gexp + " guild EXP.");
                                } else {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, "There was a problem trying to get " + args[1] + "'s GEXP.");
                                }
                                return;
                            }
                            default: {
                                if (HypixelAPIUtils.getGEXP()) {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, "You currently have " + HypixelAPIUtils.gexp + " guild EXP.");
                                } else {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, "There was a problem trying to get your GEXP.");
                                }
                            }
                        }
                    });
                    return;
                }
                case "winstreak": {
                    Multithreading.runAsync(() -> {
                        if (HytilitiesConfig.apiKey.isEmpty() || !HypixelAPIUtils.isValidKey(HytilitiesConfig.apiKey)) {
                            Hytilities.INSTANCE.sendMessage(EnumChatFormatting.RED + "You need to provide a valid API key to run this command! Type /api new to autoset a key.");
                            return;
                        }
                        switch (args.length) {
                            case 3: {
                                if (HypixelAPIUtils.getWinstreak(args[1], args[2])) {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, args[1] + " currently has a " + HypixelAPIUtils.winstreak + " winstreak in " + args[2] + ".");
                                } else {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, "There was a problem trying to get " + args[1] + "'s winstreak in " + args[2] + ".");
                                }
                                return;
                            }
                            case 2: {
                                if (HypixelAPIUtils.getWinstreak(args[1])) {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, args[1] + " currently has a " + HypixelAPIUtils.winstreak + " winstreak.");
                                } else {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, "There was a problem trying to get " + args[1] + "'s winstreak.");
                                }
                                return;
                            }
                            default: {
                                if (HypixelAPIUtils.getWinstreak()) {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, "You currently have a " + HypixelAPIUtils.winstreak + " winstreak.");
                                } else {
                                    EssentialAPI.getNotifications()
                                        .push(Hytilities.MOD_NAME, "There was a problem trying to get your winstreak.");
                                }
                            }
                        }
                    });
                    return;
                }
                case "setkey": {
                    Multithreading.runAsync(() -> {
                        if (args.length >= 3 && HypixelAPIUtils.isValidKey(args[2])) {
                            HytilitiesConfig.apiKey = args[2];
                            Hytilities.INSTANCE.getConfig().markDirty();
                            Hytilities.INSTANCE.getConfig().writeData();
                            Hytilities.INSTANCE.sendMessage(EnumChatFormatting.GREEN + "Saved API key successfully!");
                        } else {
                            Hytilities.INSTANCE.sendMessage(EnumChatFormatting.RED + "Invalid API key! Please try again.");
                        }
                    });
                    return;
                }
                case "forcecalculate": {
                    HeightHandler.INSTANCE.initialize();
                    return;
                }
                default: {
                    Hytilities.INSTANCE.sendMessage("Invalid command usage.");
                }
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
