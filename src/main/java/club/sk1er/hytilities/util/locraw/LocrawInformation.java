/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
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

package club.sk1er.hytilities.util.locraw;

import club.sk1er.hytilities.handlers.game.GameType;
import com.google.gson.annotations.SerializedName;

public class LocrawInformation {

    @SerializedName("server")
    private String serverId;

    @SerializedName("mode")
    private String gameMode = "lobby";

    @SerializedName("map")
    private String mapName;

    @SerializedName("gametype")
    private String rawGameType;
    private GameType gameType;

    /**
     * @return The serverID of the server you are currently on, ex: mini121
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * @return The GameType of the server as a String.
     */
    public String getRawGameType() {
        return rawGameType;
    }

    /**
     * @return The GameMode of the server, ex: solo_insane
     */
    public String getGameMode() {
        return gameMode;
    }

    /**
     * @param gameType The GameType to set it to.
     */
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    /**
     * @return The GameType of the server as an Enum.
     */
    public GameType getGameType() {
        return gameType;
    }

    /**
     * @return The map of the server, ex: Shire.
     */
    public String getMapName() {
        return mapName;
    }
}
