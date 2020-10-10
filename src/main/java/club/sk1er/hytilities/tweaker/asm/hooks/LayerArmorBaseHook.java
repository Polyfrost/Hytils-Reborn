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

package club.sk1er.hytilities.tweaker.asm.hooks;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
public class LayerArmorBaseHook {
    public static boolean shouldRenderArmour(ItemStack itemStack) {
        if (!HytilitiesConfig.hideArmour || itemStack == null) return true;

        final Item item = itemStack.getItem();

        // armor piece is made of leather
        if (item instanceof ItemArmor && ((ItemArmor) item).getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
            final LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();

            if (locraw != null) {
                switch (locraw.getGameType()) {
                    case BED_WARS:
                        return false;
                    case ARCADE_GAMES:
                        // capture the wool
                        return !locraw.getGameMode().contains("PVP_CTW");
                    case DUELS:
                        return !locraw.getGameMode().contains("BRIDGE");
                }
            }
        }

        return true;
    }
}
