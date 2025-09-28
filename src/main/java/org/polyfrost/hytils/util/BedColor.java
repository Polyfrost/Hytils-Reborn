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

package org.polyfrost.hytils.util;

public enum BedColor {
    WHITE,
    LIGHT_GRAY, // hypixel gray, "silver"
    GRAY,
    BLACK,
    BROWN,
    RED,
    ORANGE,
    YELLOW,
    LIME,
    GREEN,
    CYAN, // aqua
    LIGHT_BLUE,
    BLUE,
    PURPLE,
    MAGENTA,
    PINK;

    public static final BedColor[] COLORS = values();

    public ResourceLocation getIdentifier() {
        return new ResourceLocation("hytils", this.name().toLowerCase() + "_bed");
    }

    public ModelResourceLocation getBlockStateIdentifier(IBlockState state) {
        BlockBed.EnumPartType type = state.getValue(BlockBed.PART);
        EnumFacing direction = state.getValue(BlockBed.FACING);
        return new ModelResourceLocation(this.getIdentifier(), "facing=" + direction.name() + ",part=" + type.name());
    }

    public static BedColor getBedColor(String id) {
        if (id == null) return null;
        if (id.equalsIgnoreCase("aqua")) return CYAN;
        if (id.equalsIgnoreCase("gray")) return LIGHT_GRAY;
        for (BedColor value : COLORS) {
            if (value.name().equalsIgnoreCase(id)) {
                return value;
            }
        }
        return null;
    }

}
