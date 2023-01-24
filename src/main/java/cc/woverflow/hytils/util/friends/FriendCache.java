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

package cc.woverflow.hytils.util.friends;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hytils.HytilsReborn;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FriendCache {
    /**
     * True if a thread is currently running that will fetch the friend data
     */
    private boolean currentlyDownloadingFriendData = false;

    /**
     * True if we have already tried to download friend data but the request failed.
     * Will be false if no request has been sent out yet.
     */
    private boolean hasRequestFailed = false;

    /**
     * The list of friend UUIDs, or null if usernames have not been cached yet
     */
    private Set<UUID> friends = null;

    /**
     * Downloads friend information from https://api.sk1er.club/friends/playerUUID.
     * This method should be run in a separate thread in case the HTTP request takes too long.
     *
     * @return A list of UUIDs that the player is friends on Hypixel with, or null if the request failed
     */
    private Set<UUID> downloadFriendDataFromApi() {
        String url = "https://api.sk1er.club/friends/" + Minecraft.getMinecraft().getSession().getPlayerID();
        try {
            // Convert output format into a list of names
            // TODO: Error handling if JSON data is in bad format
            JsonObject json = NetworkUtils.getJsonElement(url, "OneConfig/1.0.0", 15000, false).getAsJsonObject();
            Set<UUID> friends = new HashSet<>();
            // Note that the keys are the UUIDs
            for (Map.Entry<String, JsonElement> key : json.entrySet()) {
                // Key needs to have hyphens inserted, see https://stackoverflow.com/a/19399768
                String keyWithHyphens = key.getKey().replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                    "$1-$2-$3-$4-$5");
                friends.add(UUID.fromString(keyWithHyphens));
            }

            // Return the list of friends
            return friends;
        } catch (Exception e) {
            HytilsReborn.INSTANCE.getLogger().error("Failed retrieving friend list from {}", url, e);

            // If connection fails, return null
            return null;
        }
    }

    /**
     * <p>
     * Gets the list of friend UUIDs if they have been cached.
     * Starts a thread to download the UUIDs and returns null if they have not been cached.
     * </p>
     *
     * <p>
     * NOTE: Since the list of friends may not exist yet if this is the first time this method is called,
     * there is a good chance of this method returning null. If this happens, call this method later and the friend list
     * may exist.
     * </p>
     *
     * @return The list of friend UUIDs if they have been cached, or null otherwise.
     */
    public Set<UUID> getFriendUUIDs() {
        if (friends != null) {
            return friends;
        } else if (!currentlyDownloadingFriendData && !hasRequestFailed) {
            // Start thread to download friend data
            Multithreading.runAsync(() -> {
                currentlyDownloadingFriendData = true;
                Set<UUID> result = downloadFriendDataFromApi();
                if (result != null) {
                    friends = result;
                } else {
                    hasRequestFailed = true;
                }
                currentlyDownloadingFriendData = false;
            });
        }
        return null;
    }
}
