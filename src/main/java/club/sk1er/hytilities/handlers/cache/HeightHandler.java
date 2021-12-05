package club.sk1er.hytilities.handlers.cache;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.util.HypixelAPIUtils;
import club.sk1er.hytilities.util.JsonUtils;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import gg.essential.api.utils.WebUtil;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Locale;
import java.util.Objects;

public class HeightHandler extends CacheHandler<String, Integer> {
    public static HeightHandler INSTANCE = new HeightHandler();

    private boolean printException = true;

    private int currentHeight = -2;

    public int getHeight() {
        if (currentHeight != -2) return currentHeight;
        if (Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation() == null || jsonObject == null || Hytilities.INSTANCE.getLobbyChecker().playerIsInLobby())
            return -1;
        try {
            LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
            if (HypixelAPIUtils.isBedwars) {
                if (locraw.getMapName() != null && !locraw.getMapName().trim().isEmpty()) {
                    String map = locraw.getMapName().toLowerCase(Locale.ENGLISH).replace(" ", "_");
                    if (jsonObject.getAsJsonObject("bedwars").has(map)) {
                        Integer cached = cache.getIfPresent(map);
                        if (cached == null) {
                            cache.put(map, (jsonObject.getAsJsonObject("bedwars").get(map).getAsInt()));
                            currentHeight = Objects.requireNonNull(cache.getIfPresent(map));
                            return currentHeight;
                        } else {
                            currentHeight = cached;
                            return cached;
                        }
                    }
                }
            } else if (HypixelAPIUtils.isBridge) {
                currentHeight = 100;
                return currentHeight;
            }
            currentHeight = -1;
            return -1;
        } catch (Exception e) {
            if (printException) {
                e.printStackTrace();
                printException = false;
            }
            return -1;
        }
    }


    public void initialize() {
        try {
            jsonObject = JsonUtils.PARSER.parse(WebUtil.fetchString("https://api.pinkulu.com/HeightLimitMod/Limits")).getAsJsonObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e) {
        currentHeight = -2;
        printException = true;
    }
}
