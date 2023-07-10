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
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.handlers.cache.HeightHandler;
import cc.woverflow.hytils.util.ranks.RankType;
import com.google.gson.JsonObject;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.math.BigInteger;
import java.util.*;

public class HypixelAPIUtils {
    public static String gexp;
    public static String winstreak;
    public static LocrawInfo locraw;
    private int ticks = 0;

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
            Minecraft.getMinecraft().getSessionService().joinServer(Minecraft
                .getMinecraft()
                .getSession()
                .getProfile(), Minecraft.getMinecraft().getSession().getToken(), serverId);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return false;
        }
        JsonObject response = NetworkUtils.getJsonElement("https://api.polyfrost.cc/hypixel/authentication?username=" + Minecraft.getMinecraft().getSession().getUsername() + "&serverId=" + serverId).getAsJsonObject();
        return !response.has("error");
    }


    /**
     * Gets the player's GEXP and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getGEXP() {
        JsonObject exp = NetworkUtils.getJsonElement("https://api.polyfrost.cc/hypixel/guild-exp").getAsJsonObject();
        if (exp.has("error")) {
            if (Objects.equals(exp.get("error").getAsString(), "INVALID_AUTHORIZATION")) {
                if (authorize()) {
                    return getGEXP();
                } else {
                    return false;
                }
            } else {
                return false;
            }
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
        JsonObject exp = NetworkUtils.getJsonElement("https://api.polyfrost.cc/hypixel/guild-exp?username=" + username).getAsJsonObject();
        if (exp.has("error")) {
            if (Objects.equals(exp.get("error").getAsString(), "INVALID_AUTHORIZATION")) {
                if (authorize()) {
                    return getGEXP(username);
                } else {
                    return false;
                }
            } else {
                return false;
            }
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
        JsonObject exp = NetworkUtils.getJsonElement("https://api.polyfrost.cc/hypixel/guild-exp").getAsJsonObject();
        if (exp.has("error")) {
            if (Objects.equals(exp.get("error").getAsString(), "INVALID_AUTHORIZATION")) {
                if (authorize()) {
                    return getWeeklyGEXP();
                } else {
                    return false;
                }
            } else {
                return false;
            }
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
        JsonObject exp = NetworkUtils.getJsonElement("https://api.polyfrost.cc/hypixel/guild-exp?username=" + username).getAsJsonObject();
        if (exp.has("error")) {
            if (Objects.equals(exp.get("error").getAsString(), "INVALID_AUTHORIZATION")) {
                if (authorize()) {
                    return getWeeklyGEXP(username);
                } else {
                    return false;
                }
            } else {
                return false;
            }
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
        JsonObject winstreak = NetworkUtils.getJsonElement("https://api.polyfrost.cc/hypixel/winstreak?game=" + gameType).getAsJsonObject();
        if (winstreak.has("error")) {
            if (Objects.equals(winstreak.get("error").getAsString(), "INVALID_AUTHORIZATION")) {
                if (authorize()) {
                    return getWinstreak();
                } else {
                    return false;
                }
            } else {
                return false;
            }
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
        JsonObject winstreak = NetworkUtils.getJsonElement("https://api.polyfrost.cc/hypixel/winstreak?game=" + gameType + "&username=" + username).getAsJsonObject();
        if (winstreak.has("error")) {
            if (Objects.equals(winstreak.get("error").getAsString(), "INVALID_AUTHORIZATION")) {
                if (authorize()) {
                    return getWinstreak(username);
                } else {
                    return false;
                }
            } else {
                return false;
            }
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
        JsonObject winstreak = NetworkUtils.getJsonElement("https://api.polyfrost.cc/hypixel/winstreak?game=" + game + "&username=" + username).getAsJsonObject();
        if (winstreak.has("error")) {
            if (Objects.equals(winstreak.get("error").getAsString(), "INVALID_AUTHORIZATION")) {
                if (authorize()) {
                    return getWinstreak(username, game);
                } else {
                    return false;
                }
            } else {
                return false;
            }
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
            NetworkUtils.getJsonElement("https://api.polyfrost.cc/hypixel/rank").getAsJsonObject();
        if (playerRank.has("error")) {
            if (Objects.equals(playerRank.get("error").getAsString(), "INVALID_AUTHORIZATION")) {
                if (authorize()) {
                    return getRank();
                } else {
                    return RankType.UNKNOWN;
                }
            } else {
                return RankType.UNKNOWN;
            }
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
