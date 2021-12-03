package club.sk1er.hytilities.handlers.cache;

import club.sk1er.hytilities.util.JsonUtils;
import com.google.gson.JsonElement;
import gg.essential.api.utils.Multithreading;
import gg.essential.api.utils.WebUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternHandler extends CacheHandler<String, Pattern> {
    public static PatternHandler INSTANCE = new PatternHandler();
    public List<Pattern> gameEnd = new ArrayList<>(); // OKAY DEFTU

    public void initialize() {
        Multithreading.runAsync(() -> {
            jsonObject = JsonUtils.PARSER.parse(WebUtil.fetchString("https://raw.githubusercontent.com/Qalcyo/DataStorage/main/corgal/regex.json")).getAsJsonObject();
            for (JsonElement element : jsonObject.getAsJsonArray("game_end")) {
                gameEnd.add(Pattern.compile(element.getAsString()));
            }
            for (JsonElement element : jsonObject.getAsJsonArray("misc")) {
                cache.put(element.getAsJsonObject().get("id").getAsString(), Pattern.compile(element.getAsJsonObject().get("regex").getAsString()));
            }
        });
    }

}
