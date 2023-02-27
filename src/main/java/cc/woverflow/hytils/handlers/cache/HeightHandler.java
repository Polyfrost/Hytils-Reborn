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

import cc.polyfrost.oneconfig.events.event.LocrawEvent;
import cc.polyfrost.oneconfig.events.event.WorldLoadEvent;
import cc.polyfrost.oneconfig.libs.caffeine.cache.Cache;
import cc.polyfrost.oneconfig.libs.caffeine.cache.Caffeine;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.HytilsReborn;
import com.google.gson.JsonObject;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HeightHandler {
    public static HeightHandler INSTANCE = new HeightHandler();

    private boolean printException = true;
    private JsonObject jsonObject = null;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final ThreadPoolExecutor POOL = new ThreadPoolExecutor(
        50, 50,
        0L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(), (r) -> new Thread(
        r,
        String.format("%s Cache Thread (Handler %s) %s", HytilsReborn.MOD_NAME, getClass().getSimpleName(), counter.incrementAndGet())
    )
    );

    public final Cache<String, Integer> cache = Caffeine.newBuilder().executor(POOL).maximumSize(100).build();

    private int currentHeight = -2;

    public int getHeight() {
        if (currentHeight != -2) return currentHeight;
        if (LocrawUtil.INSTANCE.getLocrawInfo() == null || jsonObject == null || !LocrawUtil.INSTANCE.isInGame())
            return -1;
        try {
            LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
            if (locraw != null && locraw.getGameType() == LocrawInfo.GameType.BEDWARS) {
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
            } else if (locraw != null && locraw.getGameMode().contains("BRIDGE")) {
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
        Multithreading.runAsync(() -> {
            try {
                jsonObject = NetworkUtils.getJsonElement("https://maps.pinkulu.com").getAsJsonObject();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Subscribe
    public void onLocraw(LocrawEvent e) {
        currentHeight = -2;
        printException = true;
        getHeight();
    }

    @Subscribe
    public void onWorldLoad(WorldLoadEvent e) {
        currentHeight = -2;
        printException = true;
    }
}
