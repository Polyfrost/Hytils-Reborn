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

package cc.woverflow.hytils.command.parser;

import cc.polyfrost.oneconfig.utils.commands.arguments.ArgumentParser;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerNameParser extends ArgumentParser<PlayerName> {
    @Nullable
    @Override
    public PlayerName parse(@NotNull String arg) throws Exception {
        return new PlayerName(arg);
    }

    @Override
    public @NotNull List<String> complete(String current, Parameter parameter) {
        if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().theWorld.playerEntities != null) {
            return Minecraft.getMinecraft().theWorld.playerEntities.stream()
                .map(EntityPlayer::getName)
                .filter(name -> name.toLowerCase().startsWith(current.toLowerCase()))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
