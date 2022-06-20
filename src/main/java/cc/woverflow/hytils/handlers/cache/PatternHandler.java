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

package cc.woverflow.hytils.handlers.cache;

import cc.polyfrost.oneconfig.utils.JsonUtils;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternHandler {
    public static PatternHandler INSTANCE = new PatternHandler();
    public List<Pattern> gameEnd = new ArrayList<>(); // OKAY DEFTU

    public void initialize() {
        Multithreading.runAsync(() -> {
            try {
                final String gotten = NetworkUtils.getString("https://data.woverflow.cc/regex.json");
                if (gotten != null) {
                    JsonObject jsonObject = JsonUtils.parseString(gotten).getAsJsonObject();
                    for (JsonElement element : jsonObject.getAsJsonArray("game_end")) {
                        gameEnd.add(Pattern.compile(element.getAsString()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
