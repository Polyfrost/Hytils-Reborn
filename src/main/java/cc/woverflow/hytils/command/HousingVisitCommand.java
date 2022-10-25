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

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Description;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.woverflow.hytils.HytilsReborn;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
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

    @Main
    private void handle(@Description("Player Name") EntityPlayer player) {
        if (usernameRegex.matcher(player.getName()).matches()) {
            playerName = player.getName();

            // if we are in the housing lobby, just immediately run the /visit command
            if ("HOUSING".equals(EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName()))) {
                visit(0);
            } else {
                HytilsReborn.INSTANCE.getCommandQueue().queue("/l housing");
                MinecraftForge.EVENT_BUS.register(new HousingVisitHook());
            }
        } else {
            HytilsReborn.INSTANCE.sendMessage("&cInvalid username!");
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
