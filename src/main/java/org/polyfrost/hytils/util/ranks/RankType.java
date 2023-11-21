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

package org.polyfrost.hytils.util.ranks;

public enum RankType {
    UNKNOWN("UNKNOWN"), NON("DEFAULT"), VIP("VIP"), VIP_PLUS("VIP_PLUS"), MVP("MVP"), MVP_PLUS("MVP_PLUS"), MVP_PLUS_PLUS("SUPERSTAR"), YOUTUBE("YOUTUBER"), GAME_MASTER("GAME_MASTER"), ADMIN("ADMIN");

    private final String rank;

    RankType(String rank) {
        this.rank = rank;
    }

    public static RankType getRank(String rank) {
        if (rank == null) return UNKNOWN;
        for (RankType value : values()) {
            if (value.rank.equals(rank)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
