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

package org.polyfrost.hytils.command.parser;

import org.polyfrost.oneconfig.api.commands.v1.arguments.ArgumentParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GEXPTypeParser extends ArgumentParser<GEXPType> {
    @Override
    public GEXPType parse(@NotNull String arg) {
        return GEXPType.valueOf(arg.toUpperCase(Locale.ROOT));
    }

    @Override
    public Class<GEXPType> getType() {
        return GEXPType.class;
    }

    @Override
    public @Nullable List<String> getAutoCompletions(String arg) {
        if (arg.isEmpty()) {
            return null;
        }
        List<String> completions = new ArrayList<>();
        for (GEXPType type : GEXPType.values()) {
            if (type.name().toLowerCase(Locale.ENGLISH).startsWith(arg.toLowerCase(Locale.ENGLISH))) {
                completions.add(type.name().toLowerCase(Locale.ENGLISH));
            }
        }
        return completions;
    }
}
