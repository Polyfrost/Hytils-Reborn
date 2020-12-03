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
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.List;
import java.util.Set;

public class SilentRemoveCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "silentremove";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " <username|clear>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            Hytilities.INSTANCE.sendMessage("&cInvalid usage: " + getCommandUsage(sender));
        } else {
            final Set<String> silentUsers = Hytilities.INSTANCE.getSilentRemoval().getSilentUsers();
            if (args[0].equalsIgnoreCase("clear")) {
                if (silentUsers.isEmpty()) {
                    Hytilities.INSTANCE.sendMessage("&cSilent Removal list is already empty.");
                    return;
                }

                silentUsers.clear();
            } else {
                final String player = args[0];
                if (silentUsers.contains(player)) {
                    silentUsers.remove(player);
                    Hytilities.INSTANCE.sendMessage("&aRemoved &e" + player + " &afrom the removal queue.");
                    return;
                }

                silentUsers.add(player);
                Hytilities.INSTANCE.sendMessage("&aAdded &e" + player + " &ato the removal queue.");
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "clear") : null;
    }
}
