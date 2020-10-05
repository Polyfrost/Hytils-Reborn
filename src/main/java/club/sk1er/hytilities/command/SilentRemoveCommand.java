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
        } else if (args[0].equalsIgnoreCase("clear")) {
            if (Hytilities.INSTANCE.getSilentRemoval().getSilentUsers().isEmpty()) {
                Hytilities.INSTANCE.sendMessage("&cSilent Removal list is already empty.");
                return;
            }

            Hytilities.INSTANCE.getSilentRemoval().getSilentUsers().clear();
        } else {
            String player = args[0];

            if (Hytilities.INSTANCE.getSilentRemoval().getSilentUsers().contains(player)) {
                Hytilities.INSTANCE.getSilentRemoval().getSilentUsers().remove(player);
                Hytilities.INSTANCE.sendMessage("&aRemoved &e" + player + " &afrom the removal queue.");
                return;
            }

            Hytilities.INSTANCE.getSilentRemoval().getSilentUsers().add(player);
            Hytilities.INSTANCE.sendMessage("&aAdded &e" + player + " &ato the removal queue.");
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
