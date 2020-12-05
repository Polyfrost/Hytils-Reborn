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
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class SkyblockVisitCommand extends HousingVisitCommand {

    // thank you DJ; https://github.com/BiscuitDevelopment/SkyblockAddons/blob/development/src/main/java/codes/biscuit/skyblockaddons/utils/Utils.java#L100
    private static final Set<String> SKYBLOCK_IN_ALL_LANGUAGES = Sets.newHashSet(
        "SKYBLOCK","\u7A7A\u5C9B\u751F\u5B58", "\u7A7A\u5CF6\u751F\u5B58");

    @Override
    public String getCommandName() {
        return "sbvisit"; // nobody gonna type out `/skyblockvisit`
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] strings) {
        if (strings.length == 1) {
            if (usernameRegex.matcher(strings[0]).matches()) {
                playerName = strings[0];
                // if we are in skyblock, just immediately run the /visit command
                @Nullable final ScoreObjective title = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
                if (title != null && SKYBLOCK_IN_ALL_LANGUAGES.contains(EnumChatFormatting
                    .getTextWithoutFormattingCodes(title.getDisplayName()).split(" ")[0])) {
                    visit(0);
                    return;
                }
                Hytilities.INSTANCE.getCommandQueue().queue("/play sb"); // handled by LimboPlayCommandHelper
                MinecraftForge.EVENT_BUS.register(this);
            } else {
                Hytilities.INSTANCE.sendMessage("&cInvalid playername!");
            }
        } else {
            Hytilities.INSTANCE.sendMessage("&cIncorrect arguments. Command usage is: " + getCommandUsage(sender));
        }
    }

}
