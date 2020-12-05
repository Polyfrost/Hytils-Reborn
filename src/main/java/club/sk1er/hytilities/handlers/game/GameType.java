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

    private final String serverName;
    private static final GameType[] typeArray = values();

    GameType(String serverName) {
        this.serverName = serverName;
    }

    public static GameType getFromLocraw(String gameType) {
        for (GameType value : typeArray) {
            if (value.serverName.equals(gameType)) {
                return value;
            }
        }

        return UNKNOWN;
    }

    public String getServerName() {
        return serverName;
    }
}
