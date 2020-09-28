package club.sk1er.hytilities.util.locraw;

import club.sk1er.hytilities.handlers.game.GameType;
import com.google.gson.annotations.SerializedName;

public class LocrawInformation {

    @SerializedName("server")
    public String serverId;
    @SerializedName("mode")
    public String gameMode;
    @SerializedName("map")
    public String mapName;

    @SerializedName("gametype")
    public GameType gameType;

}