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

package org.polyfrost.hytils.handlers.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.hypixel.data.type.GameType;
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent;
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.EventHandler;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.oneconfig.utils.v1.JsonUtils;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.hytils.HytilsReborn;
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
        String.format("%s Cache Thread (Handler %s) %s", HytilsReborn.NAME, getClass().getSimpleName(), counter.incrementAndGet())
    )
    );

    public final Cache<String, Integer> cache = Caffeine.newBuilder().executor(POOL).maximumSize(100).build();

    private int currentHeight = -2;

    public int getHeight() {
        if (currentHeight != -2) return currentHeight;
        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (jsonObject == null || !location.inGame()) {
            return -1;
        }

        try {
            if (location.getGameType().orElse(null) == GameType.BEDWARS) {
                if (location.getMapName().isPresent()) {
                    String map = location.getMapName().get().toLowerCase(Locale.ENGLISH).replace(" ", "_");
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
            } else if (location.getMode().orElse("null").contains("BRIDGE")) {
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
        Multithreading.submit(() -> {
            try {
                jsonObject = Objects.requireNonNull(JsonUtils.parseFromUrl("https://maps.pinkulu.com")).getAsJsonObject();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        EventHandler.of(HypixelLocationEvent.class, () -> {
            currentHeight = -2;
            printException = true;
            getHeight();
        }).register();
        EventHandler.of(WorldEvent.Load.class, () -> {
            currentHeight = -2;
            printException = true;
        }).register();
    }
}
