/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
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

package net.wyvest.hytilities.handlers.cache;

import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.events.LocrawEvent;
import net.wyvest.hytilities.util.HypixelAPIUtils;
import net.wyvest.hytilities.util.JsonUtils;
import net.wyvest.hytilities.util.locraw.LocrawInformation;
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
    public void onLocraw(LocrawEvent e) {
        currentHeight = -2;
        printException = true;
        getHeight();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e) {
        currentHeight = -2;
        printException = true;
    }
}
