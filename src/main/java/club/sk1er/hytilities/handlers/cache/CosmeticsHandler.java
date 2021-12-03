package club.sk1er.hytilities.handlers.cache;

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
            final String gotten = WebUtil.fetchString("https://raw.githubusercontent.com/Qalcyo/DataStorage/main/corgal/cosmetics.json");
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
