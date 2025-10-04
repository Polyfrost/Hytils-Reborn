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

package org.polyfrost.hytils.handlers.language;

import com.google.gson.JsonElement;
import org.polyfrost.oneconfig.utils.v1.JsonUtils;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

/**
 * Automatically switches the player's language based on their current
 * Hypixel language. It does so through the Sk1er API which caches the result
 * for a select period of time.
 *
 * @author Koding
 */
public class LanguageHandler {

    private final Gson gson = new GsonBuilder().create();
    private final LanguageData fallback = new LanguageData();

    private JsonObject regex = null;

    private LanguageData current = fallback;

    public LanguageHandler() {
        Multithreading.submit(this::initialize);
    }

    private void initialize() {
        fallback.initialize();
        JsonElement maybeRegex = JsonUtils.parseFromUrl("https://data.polyfrost.org/regex.json");
        if (maybeRegex != null) {
            regex = maybeRegex.getAsJsonObject();
        }
        if (regex != null && !regex.entrySet().isEmpty()) {
            current = gson.fromJson(regex.getAsJsonObject("en").toString(), LanguageData.class);
        }
        current.initialize();
    }

    public @Nullable JsonObject getRegexJson() {
        return regex;
    }

    public LanguageData getCurrent() {
        return current;
    }
}
