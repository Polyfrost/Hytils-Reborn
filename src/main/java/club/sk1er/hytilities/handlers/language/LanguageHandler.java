package club.sk1er.hytilities.handlers.language;

import club.sk1er.mods.core.util.JsonHolder;
import club.sk1er.mods.core.util.Multithreading;
import club.sk1er.mods.core.util.WebUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private final LanguageData fallback = readData("en");
    private final Map<String, String> languageMappings = new HashMap<String, String>() {{
        put("ENGLISH", "en");
    }};

    private LanguageData current = fallback;

    public LanguageHandler() {
        Multithreading.runAsync(this::initialize);
    }

    private void initialize() {
        final String username = Minecraft.getMinecraft().getSession().getUsername();
        final JsonHolder json = WebUtil.fetchJSON("https://api.sk1er.club/player/" + username);
        final String language = json.optJSONObject("player").defaultOptString("userLanguage", "ENGLISH");
        current = loadData(languageMappings.getOrDefault(language, "en"));
    }

    private LanguageData loadData(String language) {
        final LanguageData data = readData(language);
        return data == null ? fallback : data;
    }

    private LanguageData readData(String language) {
        try (InputStream stream = LanguageHandler.class.getResourceAsStream("/languages/" + language + ".json")) {
            if (stream == null) return null;
            final LanguageData data = gson.fromJson(new InputStreamReader(stream), LanguageData.class);
            data.initialize();
            return data;
        } catch (IOException e) {
            return null;
        }
    }

    public LanguageData getCurrent() {
        return current;
    }
}
