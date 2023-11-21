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

package org.polyfrost.hytils.util;

import cc.polyfrost.oneconfig.events.event.LocrawEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.JsonUtils;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.handlers.cache.HeightHandler;
import org.polyfrost.hytils.util.ranks.RankType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class HypixelAPIUtils {
    private static final String[] rankValues = {"rank", "monthlyPackageRank", "newPackageRank", "packageRank"};
    public static String gexp;
    public static String winstreak;
    public static LocrawInfo locraw;
    @Nullable
    private static String token;
    @Nullable
    private static Instant expiry;
    @Nullable
    private static String username;
    @Nullable
    private static String serverId;
    private int ticks = 0;

    private static String getCurrentESTTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    private static boolean authorize() {
        String serverId = UUID.randomUUID().toString();
        try {
            GameProfile profile = Minecraft.getMinecraft().getSession().getProfile();
            String token = Minecraft.getMinecraft().getSession().getToken();
            Minecraft.getMinecraft().getSessionService().joinServer(profile, token, serverId);
            username = profile.getName();
            HypixelAPIUtils.serverId = serverId;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static JsonObject getJsonObjectAuth(String url) {
        HttpURLConnection connection = setupConnection(url);
        if (connection == null) return null;
        try (InputStreamReader input = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
            JsonElement element = JsonUtils.parseString(IOUtils.toString(input));
            if (username != null && serverId != null) {
                token = connection.getHeaderField("x-ursa-token");
                expiry = calculateExpiry(connection.getHeaderField("x-ursa-expires"));
                username = null;
                serverId = null;
            }
            if (element == null || !element.isJsonObject()) {
                return null;
            }
            return element.getAsJsonObject();
        } catch (IOException e) {
            return null;
        }
    }

    private static Instant calculateExpiry(String expiry) {
        try {
            long expiryLong = Long.parseLong(expiry);
            return Instant.ofEpochMilli(expiryLong);
        } catch (NumberFormatException e) {
            return Instant.now().plus(Duration.ofMinutes(55));
        }
    }

    private static HttpURLConnection setupConnection(String url) {
        HttpURLConnection connection;
        try {
            connection = ((HttpURLConnection) new URL(url).openConnection());
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.addRequestProperty("User-Agent", "Hytils-Reborn/" + HytilsReborn.VERSION);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);
            if (token != null && expiry != null && expiry.isAfter(Instant.now())) {
                connection.addRequestProperty("x-ursa-token", token);
            } else {
                token = null;
                expiry = null;
                if (authorize()) {
                    connection.addRequestProperty("x-ursa-username", username);
                    connection.addRequestProperty("x-ursa-serverid", serverId);
                } else {
                    return null;
                }
            }
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Gets the player's GEXP and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getGEXP() {
        String gexp = null;
        String uuid = Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString().replace("-", "");
        JsonObject jsonObject = getJsonObjectAuth("https://api.polyfrost.cc/ursa/v1/hypixel/guild/" + uuid);
        if (jsonObject == null) {
            return false;
        }
        JsonArray guildMembers = jsonObject.getAsJsonObject("guild").getAsJsonArray("members");
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
        JsonObject jsonObject = getJsonObjectAuth("https://api.polyfrost.cc/ursa/v1/hypixel/guild/" + uuid);
        if (jsonObject == null) {
            return false;
        }
        JsonArray guildMembers = jsonObject.getAsJsonObject("guild").getAsJsonArray("members");
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
        JsonObject jsonObject = getJsonObjectAuth("https://api.polyfrost.cc/ursa/v1/hypixel/guild/" + uuid);
        if (jsonObject == null) {
            return false;
        }
        JsonArray guildMembers = jsonObject.getAsJsonObject("guild").getAsJsonArray("members");
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
        JsonObject jsonObject = getJsonObjectAuth("https://api.polyfrost.cc/ursa/v1/hypixel/guild/" + uuid);
        if (jsonObject == null) {
            return false;
        }
        JsonArray guildMembers = jsonObject.getAsJsonObject("guild").getAsJsonArray("members");
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
        JsonObject jsonObject = getJsonObjectAuth("https://api.polyfrost.cc/ursa/v1/hypixel/player/" + uuid);
        if (jsonObject == null) {
            return false;
        }
        JsonObject playerStats = jsonObject.getAsJsonObject("player").getAsJsonObject("stats");
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
        JsonObject jsonObject = getJsonObjectAuth("https://api.polyfrost.cc/ursa/v1/hypixel/player/" + uuid);
        if (jsonObject == null) {
            return false;
        }
        JsonObject playerStats = jsonObject.getAsJsonObject("player").getAsJsonObject("stats");
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
        JsonObject jsonObject = getJsonObjectAuth("https://api.polyfrost.cc/ursa/v1/hypixel/player/" + uuid);
        if (jsonObject == null) {
            return false;
        }
        JsonObject playerStats = jsonObject.getAsJsonObject("player").getAsJsonObject("stats");
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
     * @return Player rank, RankType.UNKNOWN if the player does not exist or the API key is empty
     */
    public static RankType getRank(String username) {
        String uuid = getUUID(username);
        try {
            JsonObject jsonObject = getJsonObjectAuth("https://api.polyfrost.cc/ursa/v1/hypixel/player/" + uuid);
            if (jsonObject == null) {
                return RankType.UNKNOWN;
            }
            JsonObject playerRank = jsonObject.getAsJsonObject("player");
            for (String value : rankValues) {
                if (playerRank.has(value) && !playerRank.get(value).getAsString().matches("NONE|NORMAL")) {
                    return RankType.getRank(playerRank.get(value).getAsString());
                }
            }
            return RankType.NON;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RankType.UNKNOWN;
    }

    /**
     * Gets a UUID based on the username provided.
     *
     * @param username The username of the player to get.
     */
    public static String getUUID(String username) {
        try {
            JsonObject uuidResponse = NetworkUtils.getJsonElement("https://api.mojang.com/users/profiles/minecraft/" + username).getAsJsonObject();
            if (uuidResponse.has("error")) {
                HytilsReborn.INSTANCE.sendMessage(EnumChatFormatting.RED + "Failed with error: " + uuidResponse.get("reason").getAsString());
                return null;
            }
            return uuidResponse.get("id").getAsString();
        } catch (Exception e) {
            HytilsReborn.INSTANCE.sendMessage(EnumChatFormatting.RED + "Failed to fetch " + username + "'s data. Please make sure this user exists.");
            return null;
        }
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (event.stage == Stage.START) {
            if (ticks % 20 == 0) {
                if (Minecraft.getMinecraft().thePlayer != null) {
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
