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

package org.polyfrost.hytils.command;

import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Parameter;
import org.polyfrost.oneconfig.api.ui.v1.notifications.Notifications;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.command.parser.GEXPType;
import org.polyfrost.hytils.command.parser.GEXPTypeParser;
import org.polyfrost.hytils.command.parser.WinstreakType;
import org.polyfrost.hytils.command.parser.WinstreakTypeParser;
import org.polyfrost.hytils.util.HypixelAPIUtils;
import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@Command({"hytils", "hytilities", "hytilsreborn", "hytilitiesreborn", "hytil"})
public class HytilsCommand {
    static {
        CommandManager.INSTANCE.registerParser(new GEXPTypeParser());
        CommandManager.INSTANCE.registerParser(new WinstreakTypeParser());
    }

    @Command
    private void main() {
        //HytilsReborn.INSTANCE.getConfig().openGui();
    }

    @Command(value = {"guildexp", "guildexperience"}, description = "Shows your guild experience")
    @SuppressWarnings("SameParameterValue")
    private void gexp(@Parameter("Username") @Nullable GameProfile player, @Parameter("GEXP Type") @Nullable GEXPType type) {
        Multithreading.submit(() -> {
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
                            break;
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
                            break;
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

    @Command(value = {"winstreak", "ws"}, description = "Shows your winstreak")
    @SuppressWarnings("SameParameterValue")
    private void winstreak(@Parameter("Username") @Nullable GameProfile player, @Parameter("Winstreak Type") @Nullable WinstreakType gamemode) {
        Multithreading.submit(() -> {
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
                                "There was a problem trying to get " + player.getName() + "'s winstreak in " + gamemode + "."
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
}
