package club.sk1er.hytilities.handlers.cache;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.handlers.game.GameType;
import club.sk1er.hytilities.util.JsonUtils;
import club.sk1er.hytilities.util.locraw.LocrawEvent;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import com.google.gson.JsonElement;
import gg.essential.api.utils.Multithreading;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class HeightHandler extends CacheHandler<String, Integer> {
    public static HeightHandler INSTANCE = new HeightHandler();
    private final File limitFile = new File("./config/hytilitieslimits.json");
    public int height = -1;
    private Future<?> downloadTask = null;

    public void getHeight() {
        final LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (locraw == null || Hytilities.INSTANCE.getLocrawUtil().isLobby()) {
            height = -1;
            return;
        }
        if (locraw.getGameType() == GameType.DUELS) {
            if (locraw.getGameMode().toLowerCase(Locale.ENGLISH).contains("bridge") || locraw.getGameMode().toLowerCase(Locale.ENGLISH).contains("ctf")) {
                height = 100;
            } else {
                height = -1;
            }
            return;
        }
        if (downloadTask == null || !downloadTask.isDone() || !limitFile.exists() || jsonObject == null) {
            return;
        }
        try {
            if (Objects.requireNonNull(locraw).getGameType() == GameType.BED_WARS) {
                if (locraw.getMapName() != null && !(locraw.getMapName().trim().isEmpty())) {
                    String map = locraw.getMapName().toLowerCase(Locale.ENGLISH).replace(" ", "_");
                    Integer cached = cache.getIfPresent(map);
                    if (cached == null) {
                        cache.put(map, (jsonObject.getAsJsonObject("bedwars").get(map).getAsInt()));
                        Integer funny = cache.getIfPresent(map);
                        if (funny == null) {
                            height = -1;
                        } else {
                            height = funny;
                        }
                    } else {
                        height = cached;
                    }
                } else {
                    height = -1;
                }
            } else {
                height = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            height = -1;
        }
    }


    public void initialize() {
        downloadLimits();
    }

    private boolean downloadLimits() {
        try {
            if (downloadTask == null) {
                AtomicBoolean yes = new AtomicBoolean(false);
                downloadTask = Multithreading.INSTANCE.submit(() -> {
                    if (JsonUtils.download("https://api.pinkulu.com/HeightLimitMod/Limits", limitFile)) {
                        JsonElement temp = JsonUtils.read("hytilitieslimits.json", limitFile.getParentFile());
                        if (temp != null) {
                            jsonObject = temp.getAsJsonObject();
                            yes.set(true);
                        }
                    } else {
                        if (limitFile.exists()) {
                            JsonElement temp = JsonUtils.read("hytilitieslimits.json", limitFile.getParentFile());
                            if (temp != null) {
                                jsonObject = temp.getAsJsonObject();
                                System.out.println("Using pre-downloaded file.");
                                yes.set(true);
                            }
                        }
                    }
                });
                if (yes.get()) return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @SubscribeEvent
    public void onLocraw(LocrawEvent event) {
        if (downloadLimits()) {
            return;
        }
        getHeight();
    }
}
