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

import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.oneconfig.utils.v1.NetworkUtils;
import org.polyfrost.hytils.HytilsReborn;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LocrawGamesHandler {
    public static LocrawGamesHandler INSTANCE = new LocrawGamesHandler();
    public static Map<String, String> locrawGames = new HashMap<>();

    public void initialize() {
        Multithreading.submit(() -> {
            try {
                String url = "https://data.woverflow.cc/locraw_games.json";
                String content = NetworkUtils.getString(url);
                Type stringStringMap = new TypeToken<HashMap<String, String>>() {
                }.getType();
                locrawGames = new Gson().fromJson(content, stringStringMap);
            } catch (JsonSyntaxException e) {
                HytilsReborn.INSTANCE.getLogger().error("Failed to fetch locraw_games list.", e);
            }
        });
    }
}
