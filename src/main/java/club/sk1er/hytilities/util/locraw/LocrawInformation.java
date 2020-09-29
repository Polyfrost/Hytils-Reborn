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

    public String getServerId() {
        return serverId;
    }

    public String getRawGameType() {
        return rawGameType;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return gameType;
    }

    public String getMapName() {
        return mapName;
    }
}