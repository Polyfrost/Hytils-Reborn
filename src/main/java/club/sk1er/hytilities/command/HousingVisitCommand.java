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
import gg.essential.api.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Combination command & listener, since they are both small.
 */
public class HousingVisitCommand extends CommandBase {

    /**
     * Used for performing a rudimentary check to prevent visiting invalid houses.
     */
    protected final Pattern usernameRegex = Pattern.compile("\\w{1,16}");

    protected String playerName = "";

    @Override
    public String getCommandName() {
        return "housingvisit";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("hvisit");
    }

    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "/" + getCommandName() + " <playername>";
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] strings) {
        if (strings.length == 1) {
            if (usernameRegex.matcher(strings[0]).matches()) {
                playerName = strings[0];
                // if we are in the housing lobby, just immediately run the /visit command
                if ("HOUSING".equals(EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().theWorld
                    .getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName()))) {
                    visit(0);
                } else {
                    Hytilities.INSTANCE.getCommandQueue().queue("/l housing");
                    MinecraftForge.EVENT_BUS.register(this);
                }
            } else {
                Hytilities.INSTANCE.sendMessage("&cInvalid username!");
            }
        } else {
            Hytilities.INSTANCE.sendMessage("&cIncorrect arguments. Command usage is: " + getCommandUsage(sender));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    @SubscribeEvent
    public void onHousingLobbyJoin(final WorldEvent.Load event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        visit(300);
    }

   void visit(final long time) {
       if (playerName != null) {
           Multithreading.schedule(
               () -> Hytilities.INSTANCE.getCommandQueue().queue("/visit " + playerName),
               time, TimeUnit.MILLISECONDS); // at 300ms you can be nearly certain that nothing important will be null
       }
   }
}
