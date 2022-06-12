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

package cc.woverflow.hytils.util;

import cc.polyfrost.oneconfig.events.event.LocrawEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.cache.HeightHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumChatFormatting;

import java.text.SimpleDateFormat;
import java.util.*;

public class HypixelAPIUtils {
    public static boolean isBedwars = false;
    public static boolean isBridge = false;
    public static String gexp;
    public static String winstreak;
    public static String rank;
    public static LocrawInfo locraw;
    private int ticks = 0;

    private static String getCurrentESTTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    public static void checkGameModes() {
        if (HypixelUtils.INSTANCE.isHypixel()) {
            ScoreObjective scoreboardObj = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreboardObj != null) {
                String scObjName = ScoreboardUtil.cleanSB(scoreboardObj.getDisplayName());
                if (scObjName.contains("BED WARS")) {
                    isBedwars = true;
                    isBridge = false;
                    return;
                } else if (scObjName.contains("DUELS")) {
                    for (String s : ScoreboardUtil.getSidebarLines()) {
                        if (s.toLowerCase(Locale.ENGLISH).contains("bridge ")) {
                            isBridge = true;
                            isBedwars = false;
                            return;
                        }
                    }
                }
            }
        }
        isBedwars = false;
        isBridge = false;
    }

    /**
     * Gets the player's GEXP and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getGEXP() {
        String gexp = null;
        String uuid = Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString().replace("-", "");
        JsonArray guildMembers = NetworkUtils.getJsonElement("https://api.hypixel.net/guild?key=" + HytilsConfig.apiKey + ";player=" + uuid).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
        for (JsonElement e : guildMembers) {
            if (e.getAsJsonObject().get("uuid").getAsString().equals(uuid)) {
                gexp = Integer.toString(e.getAsJsonObject().getAsJsonObject("expHistory").get(getCurrentESTTime()).getAsInt());
                break;
            }
        }
        if (gexp == null) return false;
        HypixelAPIUtils.gexp = gexp;
        return true;
    }

    /**
     * Gets the specified player's GEXP and stores it in a variable.
     *
     * @param username The username of the player.
     * @return Whether the "getting" was successful.
     */
    public static boolean getGEXP(String username) {
        String gexp = null;
        String uuid = getUUID(username);
        JsonArray guildMembers = NetworkUtils.getJsonElement("https://api.hypixel.net/guild?key=" + HytilsConfig.apiKey + ";player=" + uuid).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
        for (JsonElement e : guildMembers) {
            if (e.getAsJsonObject().get("uuid").getAsString().equals(uuid)) {
                gexp = Integer.toString(e.getAsJsonObject().getAsJsonObject("expHistory").get(getCurrentESTTime()).getAsInt());
                break;
            }
        }
        if (gexp == null) return false;
        HypixelAPIUtils.gexp = gexp;
        return true;
    }

    /**
     * Gets the player's weekly GEXP and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getWeeklyGEXP() {
        String gexp = null;
        String uuid = Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString().replace("-", "");
        JsonArray guildMembers = NetworkUtils.getJsonElement("https://api.hypixel.net/guild?key=" + HytilsConfig.apiKey + ";player=" + uuid).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
        for (JsonElement e : guildMembers) {
            if (e.getAsJsonObject().get("uuid").getAsString().equals(uuid)) {
                int addGEXP = 0;
                for (Map.Entry<String, JsonElement> set : e.getAsJsonObject().get("expHistory").getAsJsonObject().entrySet()) {
                    addGEXP += set.getValue().getAsInt();
                }
                gexp = Integer.toString(addGEXP);
                break;
            }
        }
        if (gexp == null) return false;
        HypixelAPIUtils.gexp = gexp;
        return true;
    }

    /**
     * Gets the player's weekly GEXP and stores it in a variable.
     *
     * @param username The username of the player.
     * @return Whether the "getting" was successful.
     */
    public static boolean getWeeklyGEXP(String username) {
        String gexp = null;
        String uuid = getUUID(username);
        JsonArray guildMembers = NetworkUtils.getJsonElement("https://api.hypixel.net/guild?key=" + HytilsConfig.apiKey + ";player=" + uuid).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
        for (JsonElement e : guildMembers) {
            if (e.getAsJsonObject().get("uuid").getAsString().equals(uuid)) {
                int addGEXP = 0;
                for (Map.Entry<String, JsonElement> set : e.getAsJsonObject().get("expHistory").getAsJsonObject().entrySet()) {
                    addGEXP += set.getValue().getAsInt();
                }
                gexp = Integer.toString(addGEXP);
                break;
            }
        }
        if (gexp == null) return false;
        HypixelAPIUtils.gexp = gexp;
        return true;
    }

    /**
     * Gets the player's current winstreak and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getWinstreak() {
        String uuid = Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString().replace("-", "");
        JsonObject playerStats =
            NetworkUtils.getJsonElement("https://api.hypixel.net/player?key=" + HytilsConfig.apiKey + ";uuid=" + uuid).getAsJsonObject().getAsJsonObject("player").getAsJsonObject("stats");
        if (locraw != null) {
            switch (locraw.getGameType()) {
                case BEDWARS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Bedwars").get("winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SKYWARS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("SkyWars").get("win_streak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case DUELS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Duels").get("current_winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Gets the specified player's current winstreak and stores it in a variable.
     *
     * @param username The username of the player.
     * @return Whether the "getting" was successful.
     */
    public static boolean getWinstreak(String username) {
        String uuid = getUUID(username);
        JsonObject playerStats =
            NetworkUtils.getJsonElement("https://api.hypixel.net/player?key=" + HytilsConfig.apiKey + ";uuid=" + uuid).getAsJsonObject().getAsJsonObject("player").getAsJsonObject("stats");
        if (locraw != null) {
            switch (locraw.getGameType()) {
                case BEDWARS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Bedwars").get("winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SKYWARS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("SkyWars").get("win_streak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case DUELS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Duels").get("current_winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Gets the player's current winstreak and stores it in a variable.
     *
     * @param username The username of the player.
     * @param game     The game to get the stats.
     * @return Whether the "getting" was successful.
     */
    public static boolean getWinstreak(String username, String game) {
        String uuid = getUUID(username);
        JsonObject playerStats =
            NetworkUtils.getJsonElement("https://api.hypixel.net/player?key=" + HytilsConfig.apiKey + ";uuid=" + uuid).getAsJsonObject().getAsJsonObject("player").getAsJsonObject("stats");
        if (game != null) {
            switch (game.toLowerCase(Locale.ENGLISH)) {
                case "bedwars":
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Bedwars").get("winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "skywars":
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("SkyWars").get("win_streak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "duels":
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Duels").get("current_winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Gets the Hypixel rank of the specified player.
     *
     * @param username The username of the player.
     * @return Player rank
     */
    public static String getRank(String username) {
        if (!HytilsConfig.apiKey.isEmpty() && HypixelAPIUtils.isValidKey(HytilsConfig.apiKey)) {
            String uuid = getUUID(username);
            try {
                JsonObject playerRank =
                    NetworkUtils.getJsonElement("https://api.hypixel.net/player?key=" + HytilsConfig.apiKey + ";uuid=" + uuid).getAsJsonObject().getAsJsonObject("player");
                rank = playerRank.get("newPackageRank").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rank;
    }

    /**
     * Gets a UUID based on the username provided.
     *
     * @param username The username of the player to get.
     */
    public static String getUUID(String username) {
        JsonObject uuidResponse =
            NetworkUtils.getJsonElement("https://api.mojang.com/users/profiles/minecraft/" + username).getAsJsonObject();
        if (uuidResponse.has("error")) {
            HytilsReborn.INSTANCE.sendMessage(
                EnumChatFormatting.RED + "Failed with error: " + uuidResponse.get("reason").getAsString()
            );
            return null;
        }
        return uuidResponse.get("id").getAsString();
    }

    public static boolean isValidKey(String key) {
        try {
            JsonObject gotten = NetworkUtils.getJsonElement("https://api.hypixel.net/key?key=" + key).getAsJsonObject();
            return gotten.has("success") && gotten.get("success").getAsBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (event.stage == Stage.START) {
            if (ticks % 20 == 0) {
                if (Minecraft.getMinecraft().thePlayer != null) {
                    checkGameModes();
                    HeightHandler.INSTANCE.getHeight();
                }
                ticks = 0;
            }

            ticks++;
        }
    }

    @Subscribe
    private void onLocraw(LocrawEvent event) {
        locraw = event.info;
    }
}
