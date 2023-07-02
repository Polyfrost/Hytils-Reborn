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

package cc.woverflow.hytils.util;

import cc.polyfrost.oneconfig.events.event.LocrawEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.JsonUtils;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.handlers.cache.HeightHandler;
import cc.woverflow.hytils.util.ranks.RankType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HypixelAPIUtils {
    public static String gexp;
    public static String winstreak;
    public static LocrawInfo locraw;
    private int ticks = 0;
    private static String token;

    public HypixelAPIUtils() {
        authorize();
    }

    private static boolean authorize() {
        Random r1 = new Random();
        Random r2 = new Random(System.identityHashCode(new Object()));
        BigInteger random1Bi = new BigInteger(128, r1);
        BigInteger random2Bi = new BigInteger(128, r2);
        BigInteger serverBi = random1Bi.xor(random2Bi);
        String serverId = serverBi.toString(16);
        try {
            GameProfile profile = Minecraft.getMinecraft().getSession().getProfile();
            String token = Minecraft.getMinecraft().getSession().getToken();
            Minecraft.getMinecraft().getSessionService().joinServer(profile, token, serverId);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return false;
        }
        JsonObject response = NetworkUtils.getJsonElement("https://api.polyfrost.cc/hypixel/v1/authentication?username=" + Minecraft.getMinecraft().getSession().getUsername() + "&serverId=" + serverId).getAsJsonObject();
        if (response.has("error")) {
            return false;
        }
        token = response.get("token").getAsString();
        return true;
    }


    /**
     * Gets the player's GEXP and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getGEXP() {
        JsonObject exp = getJsonObjectAuth("https://api.polyfrost.cc/hypixel/v1/guild-exp");
        if (exp.has("error")) {
            return false;
        }
        gexp = exp.get("exp").getAsString();
        return true;
    }

    /**
     * Gets the specified player's GEXP and stores it in a variable.
     *
     * @param username The username of the player.
     * @return Whether the "getting" was successful.
     */
    public static boolean getGEXP(String username) {
        JsonObject exp = getJsonObjectAuth("https://api.polyfrost.cc/hypixel/v1/guild-exp?username=" + username);
        if (exp.has("error")) {
            return false;
        }
        gexp = exp.get("exp").getAsString();
        return true;
    }

    /**
     * Gets the player's weekly GEXP and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getWeeklyGEXP() {
        JsonObject exp = getJsonObjectAuth("https://api.polyfrost.cc/hypixel/v1/guild-exp");
        if (exp.has("error")) {
            return false;
        }
        gexp = exp.get("weeklyExp").getAsString();
        return true;
    }

    /**
     * Gets the player's weekly GEXP and stores it in a variable.
     *
     * @param username The username of the player.
     * @return Whether the "getting" was successful.
     */
    public static boolean getWeeklyGEXP(String username) {
        JsonObject exp = getJsonObjectAuth("https://api.polyfrost.cc/hypixel/v1/guild-exp?username=" + username);
        if (exp.has("error")) {
            return false;
        }
        gexp = exp.get("weeklyExp").getAsString();
        return true;
    }

    /**
     * Gets the player's current winstreak and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getWinstreak() {
        String gameType = "bedwars";
        if (locraw != null) {
            switch (locraw.getGameType()) {
                case BEDWARS:
                    gameType = "bedwars";
                    break;
                case SKYWARS:
                    gameType = "skywars";
                    break;
                case DUELS:
                    gameType = "duels";
                    break;
                default:
                    return false;
            }
        }
        JsonObject winstreak = getJsonObjectAuth("https://api.polyfrost.cc/hypixel/v1/winstreak?game=" + gameType);
        if (winstreak.has("error")) {
            return false;
        }
        HypixelAPIUtils.winstreak = String.valueOf(winstreak.get("winstreak").getAsInt());
        return true;
    }

    /**
     * Gets the specified player's current winstreak and stores it in a variable.
     *
     * @param username The username of the player.
     * @return Whether the "getting" was successful.
     */
    public static boolean getWinstreak(String username) {
        String gameType = "bedwars";
        if (locraw != null) {
            switch (locraw.getGameType()) {
                case BEDWARS:
                    gameType = "bedwars";
                    break;
                case SKYWARS:
                    gameType = "skywars";
                    break;
                case DUELS:
                    gameType = "duels";
                    break;
                default:
                    return false;
            }
        }
        JsonObject winstreak = getJsonObjectAuth("https://api.polyfrost.cc/hypixel/v1/winstreak?game=" + gameType + "&username=" + username);
        if (winstreak.has("error")) {
            return false;
        }
        HypixelAPIUtils.winstreak = String.valueOf(winstreak.get("winstreak").getAsInt());
        return true;
    }

    /**
     * Gets the player's current winstreak and stores it in a variable.
     *
     * @param username The username of the player.
     * @param game     The game to get the stats.
     * @return Whether the "getting" was successful.
     */
    public static boolean getWinstreak(String username, String game) {
        if (!Objects.equals(game, "bedwars") && !Objects.equals(game, "skywars") && !Objects.equals(game, "duels")) {
            return false;
        }
        JsonObject winstreak = getJsonObjectAuth("https://api.polyfrost.cc/hypixel/v1/winstreak?game=" + game + "&username=" + username);
        if (winstreak.has("error")) {
            return false;
        }
        HypixelAPIUtils.winstreak = String.valueOf(winstreak.get("winstreak").getAsInt());
        return true;
    }

    /**
     * Gets the Hypixel rank of the specified player.
     *
     * @return Player rank, RankType.UNKNOWN if the player does not exist or the API key is empty
     */
    public static RankType getRank() {
        JsonObject playerRank =
            getJsonObjectAuth("https://api.polyfrost.cc/hypixel/v1/rank");
        if (playerRank.has("error")) {
            return RankType.UNKNOWN;
        }
        return RankType.getRank(playerRank.get("rank").getAsString());
    }

    /**
     * Gets a UUID based on the username provided.
     *
     * @param username The username of the player to get.
     */
    public static String getUUID(String username) {
        try {
            JsonObject uuidResponse =
                NetworkUtils.getJsonElement("https://api.mojang.com/users/profiles/minecraft/" + username).getAsJsonObject();
            if (uuidResponse.has("error")) {
                HytilsReborn.INSTANCE.sendMessage(
                    EnumChatFormatting.RED + "Failed with error: " + uuidResponse.get("reason").getAsString()
                );
                return null;
            }
            return uuidResponse.get("id").getAsString();
        } catch (Exception e) {
            HytilsReborn.INSTANCE.sendMessage(
                EnumChatFormatting.RED + "Failed to fetch " + username + "'s data. Please make sure this user exists."
            );
            return null;
        }
    }

    public static JsonObject getJsonObjectAuth(String url) {
        HttpURLConnection connection;
        try {
            connection = ((HttpURLConnection) new URL(url).openConnection());
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.addRequestProperty("User-Agent", "Hytils Reborn/" + HytilsReborn.VERSION);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);
            connection.addRequestProperty("Authorization", token);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try (InputStreamReader input = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
            JsonElement element = JsonUtils.parseString(IOUtils.toString(input));
            if (element == null || !element.isJsonObject()) {
                return null;
            }
            return element.getAsJsonObject();
        } catch (IOException e) {
            if (e.getMessage().contains("403")) {
                if (!authorize()) {
                    return null;
                }
                return getJsonObjectAuth(url);
            }
            return null;
        }
    }

    @Subscribe
    private void onTick(TickEvent event) {
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
