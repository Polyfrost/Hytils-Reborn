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

package cc.woverflow.hytils.handlers.cache;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CosmeticsHandler {
    public static CosmeticsHandler INSTANCE = new CosmeticsHandler();
    public List<String> particleCosmetics = new ArrayList<>();
    public List<String> itemCosmetics = new ArrayList<>();

    public void initialize() {
        Multithreading.runAsync(() -> {
            final JsonElement gotten = NetworkUtils.getJsonElement("https://data.woverflow.cc/cosmetics.json");
            if (gotten != null) {
                JsonObject jsonObject = gotten.getAsJsonObject();
                for (JsonElement cosmetic : jsonObject.getAsJsonArray("particles")) {
                    particleCosmetics.add(cosmetic.getAsString());
                }
                for (JsonElement cosmetic : jsonObject.getAsJsonArray("items")) {
                    itemCosmetics.add(cosmetic.getAsString());
                }
            }
        });
    }
}
