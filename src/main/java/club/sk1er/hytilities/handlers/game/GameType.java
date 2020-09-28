package club.sk1er.hytilities.handlers.game;

public enum GameType {
    UNKNOWN(""),
    BED_WARS("BEDWARS"),
    SKY_WARS("SKYWARS"),
    PROTOTYPE("PROTOTYPE"),
    SKYBLOCK("SKYBLOCK"),
    MAIN("MAIN"),
    MURDER_MYSTERY("MURDER_MYSTERY"),
    HOUSING("HOUSING"),
    ARCADE_GAMES("ARCADE"),
    BUILD_BATTLE("BUILD_BATTLE"),
    DUELS("DUELS"),
    PIT("PIT"),
    UHC_CHAMPIONS("UHC"),
    SPEED_UHC("SPEED_UHC"),
    TNT_GAMES("TNTGAMES"),
    CLASSIC_GAMES("LEGACY"),
    COPS_AND_CRIMS("MCGO"),
    BLITZ_SG("SURVIVAL_GAMES"),
    MEGA_WALLS("WALLS3"),
    SMASH_HEROES("SUPER_SMASH"),
    WARLORDS("BATTLEGROUND");

    private String serverName;

    GameType(String serverName) {
        this.serverName = serverName;
    }

    public static GameType getFromLocraw(String gameType) {
        for (GameType value : values()) {
            if (value.serverName.equals(gameType)) return value;
        }

        return UNKNOWN;
    }

    public String getServerName() {
        return serverName;
    }
}
