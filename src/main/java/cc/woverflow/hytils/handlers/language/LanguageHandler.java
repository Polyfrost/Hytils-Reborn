/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
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

package cc.woverflow.hytils.handlers.language;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;

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
    private final Map<String, String> languageMappings = new HashMap<String, String>() {{
        put("ENGLISH", "en");
        put("FRENCH", "fr");
    }};

    private LanguageData current = fallback;

    public LanguageHandler() {
        Multithreading.runAsync(this::initialize);
    }

    private void initialize() {
        final String username = Minecraft.getMinecraft().getSession().getUsername();
        final JsonObject json = NetworkUtils.getJsonElement("https://api.sk1er.club/player/" + username).getAsJsonObject();
        final JsonObject player = json.getAsJsonObject("player");
        String userLanguage;
        if (player.has("userLanguage")) {
            userLanguage = player.get("userLanguage").getAsString();
        } else {
            userLanguage = "ENGLISH";
        }
        final String language = languageMappings.getOrDefault(userLanguage, "en");
        JsonObject finalJsonObject = NetworkUtils.getJsonElement("https://data.woverflow.cc/regex.json").getAsJsonObject();
        if (!finalJsonObject.entrySet().isEmpty()) {
            current = gson.fromJson(finalJsonObject.has(language) ? finalJsonObject.getAsJsonObject(language).toString() : finalJsonObject.getAsJsonObject("en").toString(), LanguageData.class);
        }
        current.initialize();
    }

    public LanguageData getCurrent() {
        return current;
    }
}
