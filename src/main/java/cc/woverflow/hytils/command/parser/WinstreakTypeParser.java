/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022, 2023  Polyfrost, Sk1er LLC and contributors
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
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class WinstreakTypeParser extends ArgumentParser<WinstreakType> {
    private static final List<String> DEFAULT_COMPLETIONS =
        Arrays.stream(WinstreakType.values()).map(Enum::name).collect(Collectors.toList());

    @Override
    public WinstreakType parse(@NotNull String arg) {
        return WinstreakType.valueOf(arg.toUpperCase(Locale.ROOT));
    }

    @Override
    public @NotNull List<String> complete(String arg, Parameter parameter) {
        if (arg.isEmpty()) {
            return DEFAULT_COMPLETIONS;
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
