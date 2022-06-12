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
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternHandler extends CacheHandler<String, Pattern> {
    public static PatternHandler INSTANCE = new PatternHandler();
    public List<Pattern> gameEnd = new ArrayList<>(); // OKAY DEFTU

    public void initialize() {
        Multithreading.runAsync(() -> {
            jsonObject = JsonUtils.parseString("https://data.woverflow.cc/regex.json").getAsJsonObject();
            for (JsonElement element : jsonObject.getAsJsonArray("game_end")) {
                gameEnd.add(Pattern.compile(element.getAsString()));
            }
        });
    }

}
