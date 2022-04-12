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

import gg.essential.api.EssentialAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class LimboCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "limbo";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/limbo";
    }

    // TODO: take precedence over AutoTip, remove the Hypixel error messages created in chat
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (EssentialAPI.getMinecraftUtil().isHypixel()) Minecraft.getMinecraft().thePlayer.sendChatMessage("§");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
