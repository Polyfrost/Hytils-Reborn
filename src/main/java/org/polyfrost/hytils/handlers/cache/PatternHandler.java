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

package org.polyfrost.hytils.handlers.cache;

import org.polyfrost.oneconfig.utils.v1.JsonUtils;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.oneconfig.utils.v1.NetworkUtils;
import org.polyfrost.hytils.HytilsReborn;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternHandler {
    public static PatternHandler INSTANCE = new PatternHandler();
    public List<Pattern> gameEnd = new ArrayList<>(); // OKAY DEFTU

    public void initialize() {
        Multithreading.submit(() -> {
            try {
                JsonObject cached = HytilsReborn.INSTANCE.getLanguageHandler().getRegexJson();
                if (cached != null) {
                    processJson(cached);
                    return;
                }
                final String gotten = NetworkUtils.getString("https://data.woverflow.cc/regex.json");
                if (gotten != null) {
                    processJson(JsonUtils.parse(gotten).getAsJsonObject());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void processJson(JsonObject jsonObject) {
        for (JsonElement element : jsonObject.getAsJsonArray("game_end")) {
            gameEnd.add(Pattern.compile(element.getAsString()));
        }
    }
}
