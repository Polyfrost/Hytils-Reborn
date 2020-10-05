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
import club.sk1er.mods.core.ModCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

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
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            ModCore.getInstance().getGuiHandler().open(Hytilities.INSTANCE.getConfig().gui());
        } else {
            Hytilities.INSTANCE.sendMessage("&cIncorrect arguments. Command usage is: " + getCommandUsage(sender));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
