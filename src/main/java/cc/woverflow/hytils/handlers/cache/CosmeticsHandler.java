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

import com.google.gson.JsonElement;
import gg.essential.api.utils.Multithreading;
import gg.essential.api.utils.WebUtil;

import java.util.ArrayList;
import java.util.List;

public class CosmeticsHandler extends Handler<String> {
    public static CosmeticsHandler INSTANCE = new CosmeticsHandler();
    public List<String> particleCosmetics = new ArrayList<>();
    public List<String> itemCosmetics = new ArrayList<>();

    public void initialize() {
        Multithreading.runAsync(() -> {
            final String gotten = WebUtil.fetchString("https://data.woverflow.cc/cosmetics.json");
            if (gotten != null) {
                jsonObject = parser.parse(gotten).getAsJsonObject();
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
