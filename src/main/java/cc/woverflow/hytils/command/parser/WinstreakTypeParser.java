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

package cc.woverflow.hytils.command.parser;

import cc.polyfrost.oneconfig.utils.commands.arguments.ArgumentParser;
import cc.polyfrost.oneconfig.utils.commands.arguments.Arguments;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WinstreakTypeParser extends ArgumentParser<WinstreakType> {

    @Override
    public @Nullable WinstreakType parse(Arguments arguments) {
        String arg = arguments.poll();
        if (!arg.isEmpty()) {
            for (WinstreakType type : WinstreakType.values()) {
                if (type.name().toLowerCase(Locale.ENGLISH).startsWith(arg.toLowerCase(Locale.ENGLISH))) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public @Nullable List<String> complete(Arguments arguments, Parameter parameter) {
        String arg = arguments.poll();
        if (arg.isEmpty()) {
            return null;
        }
        List<String> completions = new ArrayList<>();
        for (WinstreakType type : WinstreakType.values()) {
            if (type.name().toLowerCase(Locale.ENGLISH).startsWith(arg.toLowerCase(Locale.ENGLISH))) {
                completions.add(type.name().toLowerCase(Locale.ENGLISH));
            }
        }
        return completions;
    }
}
