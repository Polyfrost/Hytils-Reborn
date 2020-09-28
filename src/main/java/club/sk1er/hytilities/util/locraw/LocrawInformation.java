package club.sk1er.hytilities.util.locraw;

import club.sk1er.hytilities.handlers.game.GameType;
import com.google.gson.annotations.SerializedName;

public class LocrawInformation {

    @SerializedName("server")
    private String serverId;
    @SerializedName("mode")
    private String gameMode;
    @SerializedName("map")
    private String mapName;

    @SerializedName("gametype")
    private GameType gameType;

    public String getServerId() {
        return serverId;
    }

    public String getGameMode() {
        return gameMode;
    }

    public GameType getGameType() {
        return gameType;
    }

    public String getMapName() {
        return mapName;
    }
}