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

package club.sk1er.mods.autogg.config;

public class AutoGGConfig {

    private boolean autoGGEnabled = true;
    private boolean casualAutoGGEnabled;
    private boolean antiGGEnabled;
    private boolean antiKarmaEnabled;
    private int autoGGDelay = 1;
    private int autoGGPhrase = 0;
    private boolean secondaryEnabled;
    private int autoGGPhrase2 = 0;
    private int secondaryDelay = 1;

    public boolean isModEnabled() {
        return autoGGEnabled;
    }

    public boolean isCasualAutoGGEnabled() {
        return casualAutoGGEnabled;
    }

    public boolean isAntiGGEnabled() {
        return antiGGEnabled;
    }

    public boolean isAntiKarmaEnabled() {
        return antiKarmaEnabled;
    }

    public int getAutoGGDelay() {
        return autoGGDelay;
    }

    public int getAutoGGPhrase() {
        return autoGGPhrase;
    }

    public boolean isSecondaryEnabled() {
        return secondaryEnabled;
    }

    public int getAutoGGPhrase2() {
        return autoGGPhrase2;
    }

    public int getSecondaryDelay() {
        return secondaryDelay;
    }

    public void markDirty() {}

    public void writeData() {}
}
