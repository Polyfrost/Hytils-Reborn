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

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Description;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import org.polyfrost.hytils.HytilsReborn;
import com.mojang.authlib.GameProfile;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Combination command & listener, since they are both small.
 */
@Command(value = "housingvisit", aliases = "hvisit")
public class HousingVisitCommand {

    /**
     * Used for performing a rudimentary check to prevent visiting invalid houses.
     */
    protected static final Pattern usernameRegex = Pattern.compile("\\w{1,16}");

    protected static String playerName = "";

    @Main
    private void handle() {
        UChat.chat(ChatColor.RED + "Usage: /housingvisit <username>");
    }

    @Main(description = "Visits a player's house in Housing.")
    private void main(@Description("Player Name") GameProfile player) {
        if (!HypixelUtils.INSTANCE.isHypixel()) {
            UChat.chat(ChatColor.RED + "You must be on Hypixel to use this command!");
            return;
        }
        if (usernameRegex.matcher(player.getName()).matches()) {
            playerName = player.getName();

            // if we are in the housing lobby, just immediately run the /visit command
            if (LocrawUtil.INSTANCE.getLocrawInfo() != null && LocrawUtil.INSTANCE.getLocrawInfo().getGameType() == LocrawInfo.GameType.HOUSING && !LocrawUtil.INSTANCE.isInGame()) {
                visit(0);
            } else {
                HytilsReborn.INSTANCE.getCommandQueue().queue("/l housing");
                MinecraftForge.EVENT_BUS.register(new HousingVisitHook());
            }
        } else {
            UChat.chat(ChatColor.RED + "Invalid username!");
        }
    }

    private void visit(final long time) {
        if (playerName != null) {
            Multithreading.schedule(() -> HytilsReborn.INSTANCE.getCommandQueue().queue("/visit " + playerName), time, TimeUnit.MILLISECONDS); // at 300ms you can be nearly certain that nothing important will be null
        }
    }

    private class HousingVisitHook {
        @SubscribeEvent
        public void onHousingLobbyJoin(final WorldEvent.Load event) {
            MinecraftForge.EVENT_BUS.unregister(this);
            visit(300);
        }
    }
}
