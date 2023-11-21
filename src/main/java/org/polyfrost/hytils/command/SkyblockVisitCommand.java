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
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Command(value = "skyblockvisit", aliases = "sbvisit")
public class SkyblockVisitCommand {

    /**
     * Used for performing a rudimentary check to prevent visiting invalid houses.
     */
    protected static final Pattern usernameRegex = Pattern.compile("\\w{1,16}");

    protected static String playerName = "";

    @Main
    private void handle() {
        UChat.chat(EnumChatFormatting.RED + "Usage: /skyblockvisit <username>");
    }

    @Main(description = "Visit a player's SkyBlock island.")
    private void main(@Description("Player Name") GameProfile player) {
        if (!HypixelUtils.INSTANCE.isHypixel()) {
            UChat.chat(ChatColor.RED + "You must be on Hypixel to use this command!");
            return;
        }
        if (usernameRegex.matcher(player.getName()).matches()) {
            playerName = player.getName();
            if (LocrawUtil.INSTANCE.getLocrawInfo() != null && LocrawUtil.INSTANCE.getLocrawInfo().getGameType() == LocrawInfo.GameType.SKYBLOCK && LocrawUtil.INSTANCE.isInGame()) {
                visit(0);
                return;
            }
            HytilsReborn.INSTANCE.getCommandQueue().queue("/play skyblock");
            MinecraftForge.EVENT_BUS.register(new SkyblockVisitHook());
        } else {
            UChat.chat(ChatColor.RED + "Invalid username!");
        }
    }

    private void visit(final long time) {
        if (playerName != null) {
            Multithreading.schedule(
                () -> HytilsReborn.INSTANCE.getCommandQueue().queue("/visit " + playerName),
                time, TimeUnit.MILLISECONDS); // at 300ms you can be nearly certain that nothing important will be null
        }
    }

    private class SkyblockVisitHook {
        @SubscribeEvent
        public void onSkyblockLobbyJoin(final WorldEvent.Load event) {
            MinecraftForge.EVENT_BUS.unregister(this);
            visit(300);
        }
    }
}
