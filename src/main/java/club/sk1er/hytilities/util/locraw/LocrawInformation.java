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