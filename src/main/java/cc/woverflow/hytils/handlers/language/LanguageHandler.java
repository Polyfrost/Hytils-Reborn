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

package cc.woverflow.hytils.handlers.language;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gg.essential.api.utils.JsonHolder;
import gg.essential.api.utils.Multithreading;
import gg.essential.api.utils.WebUtil;
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
        final JsonHolder json = WebUtil.fetchJSON("https://api.sk1er.club/player/" + username);
        final String language = languageMappings.getOrDefault(json.optJSONObject("player").defaultOptString("userLanguage", "ENGLISH"), "en");
        JsonHolder jsonHolder = WebUtil.fetchJSON("https://data.woverflow.cc/regex.json");
        if (!jsonHolder.getKeys().isEmpty()) {
            current = gson.fromJson(jsonHolder.has(language) ? jsonHolder.optActualJSONObject(language).toString() : jsonHolder.optActualJSONObject("en").toString(), LanguageData.class);
        }
        current.initialize();
    }

    public LanguageData getCurrent() {
        return current;
    }
}
