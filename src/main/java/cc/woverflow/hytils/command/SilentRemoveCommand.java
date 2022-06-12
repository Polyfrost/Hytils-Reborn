/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.hytils.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.command.parser.PlayerName;

import java.util.Set;

@Command("silentremove")
public class SilentRemoveCommand {

    @Main
    private static void add(PlayerName player) {
        String name = player.name;
        final Set<String> silentUsers = HytilsReborn.INSTANCE.getSilentRemoval().getSilentUsers();
        if (silentUsers.contains(name)) {
            silentUsers.remove(name);
            HytilsReborn.INSTANCE.sendMessage("&aRemoved &e" + name + " &afrom the removal queue.");
            return;
        }

        silentUsers.add(name);
        HytilsReborn.INSTANCE.sendMessage("&aAdded &e" + name + " &ato the removal queue.");
    }

    @SubCommand("clear")
    private static class ClearSubCommand {
        @Main
        private static void clear() {
            final Set<String> silentUsers = HytilsReborn.INSTANCE.getSilentRemoval().getSilentUsers();
            if (silentUsers.isEmpty()) {
                HytilsReborn.INSTANCE.sendMessage("&cSilent Removal list is already empty.");
                return;
            }
            silentUsers.clear();
        }
    }
}
