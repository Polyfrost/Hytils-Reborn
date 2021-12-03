/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  Sk1er LLC
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

package club.sk1er.hytilities.util;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.util.locraw.LocrawEvent;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.essential.api.EssentialAPI;
import gg.essential.api.utils.JsonHolder;
import gg.essential.api.utils.WebUtil;
import kotlin.text.StringsKt;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class HypixelAPIUtils {
    public static String gexp;
    public static String winstreak;
    public static LocrawInformation locraw;

    /**
     * @return Whether the player is in a Hypixel lobby.
     */
    public static boolean isLobby() {
        if (EssentialAPI.getMinecraftUtil().isHypixel()) {
            if (locraw != null) {
                return locraw.getGameMode() == null || StringsKt.isBlank(locraw.getGameMode()) || locraw.getGameType() == null;
            }
        }
        return false;
    }

    private static String getCurrentESTTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * Gets the player's GEXP and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getGEXP() {
        String gexp = null;
        String uuid = Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString().replace("-", "");
        JsonArray guildMembers = JsonUtils.PARSER.parse(WebUtil.fetchString("https://api.hypixel.net/guild?key=" + HytilitiesConfig.apiKey + ";player=" + uuid)).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
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
        JsonArray guildMembers = JsonUtils.PARSER.parse(WebUtil.fetchString("https://api.hypixel.net/guild?key=" + HytilitiesConfig.apiKey + ";player=" + uuid)).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
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
        JsonArray guildMembers = JsonUtils.PARSER.parse(WebUtil.fetchString("https://api.hypixel.net/guild?key=" + HytilitiesConfig.apiKey + ";player=" + uuid)).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
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
        JsonArray guildMembers = JsonUtils.PARSER.parse(WebUtil.fetchString("https://api.hypixel.net/guild?key=" + HytilitiesConfig.apiKey + ";player=" + uuid)).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
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
            JsonUtils.PARSER.parse(WebUtil.fetchString("https://api.hypixel.net/player?key=" + HytilitiesConfig.apiKey + ";uuid=" + uuid)).getAsJsonObject().getAsJsonObject("player").getAsJsonObject("stats");
        if (locraw != null) {
            switch (locraw.getGameType()) {
                case BED_WARS: {
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Bedwars").get("winstreak").getAsInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                case SKY_WARS: {
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("SkyWars").get("win_streak").getAsInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                case DUELS: {
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Duels").get("current_winstreak").getAsInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                default:
                    return false;
            }
        }
        return true;
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
            JsonUtils.PARSER.parse(WebUtil.fetchString("https://api.hypixel.net/player?key=" + HytilitiesConfig.apiKey + ";uuid=" + uuid)).getAsJsonObject().getAsJsonObject("player").getAsJsonObject("stats");
        if (locraw != null) {
            switch (locraw.getGameType()) {
                case BED_WARS: {
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Bedwars").get("winstreak").getAsInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                case SKY_WARS: {
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("SkyWars").get("win_streak").getAsInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                case DUELS: {
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Duels").get("current_winstreak").getAsInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                default:
                    return false;
            }
        }
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
        String uuid = getUUID(username);
        JsonObject playerStats =
            JsonUtils.PARSER.parse(WebUtil.fetchString("https://api.hypixel.net/player?key=" + HytilitiesConfig.apiKey + ";uuid=" + uuid)).getAsJsonObject().getAsJsonObject("player").getAsJsonObject("stats");
        if (game != null) {
            switch (game.toLowerCase(Locale.ENGLISH)) {
                case "bedwars": {
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Bedwars").get("winstreak").getAsInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                case "skywars": {
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("SkyWars").get("win_streak").getAsInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                case "duels": {
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Duels").get("current_winstreak").getAsInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                default:
                    return false;
            }
        }
        return true;
    }

    /**
     * Gets a UUID based on the username provided.
     *
     * @param username The username of the player to get.
     */
    public static String getUUID(String username) {
        JsonHolder uuidResponse =
            WebUtil.fetchJSON("https://api.mojang.com/users/profiles/minecraft/" + username);
        if (uuidResponse.has("error")) {
            Hytilities.INSTANCE.sendMessage(
                EnumChatFormatting.RED + "Failed with error: " + uuidResponse.optString("reason")
            );
            return null;
        }
        return uuidResponse.optString("id");
    }

    private static String cleanSB(String scoreboard) {
        char[] nvString = StringUtils.stripControlCodes(scoreboard).toCharArray();
        StringBuilder cleaned = new StringBuilder();

        for (char c : nvString) {
            if ((int) c > 20 && (int) c < 127) {
                cleaned.append(c);
            }
        }

        return cleaned.toString();
    }

    public static boolean isValidKey(String key) {
        JsonObject gotten = JsonUtils.PARSER.parse(WebUtil.fetchString("https://api.hypixel.net/key?key=" + key)).getAsJsonObject();
        return gotten.has("success") && gotten.get("success").getAsBoolean();
    }

    @SubscribeEvent
    public void onLocraw(LocrawEvent event) {
        locraw = event.locraw;
    }
}
