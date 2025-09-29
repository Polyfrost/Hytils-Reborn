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

package org.polyfrost.hytils.handlers.lobby.armorstands;

import net.hypixel.data.type.GameType;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.ArmorStandHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumChatFormatting;
import org.polyfrost.oneconfig.api.event.v1.events.RenderLivingEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

public class ArmorStandHider {

    @Subscribe
    public void onEntityRenderer(RenderLivingEvent.Pre event) {
        Object entityRaw = event.getEntity();
        if (!(entityRaw instanceof EntityLivingBase)) {
            return;
        }

        EntityLivingBase entity = (EntityLivingBase) entityRaw;
        final HypixelUtils.Location location = HypixelUtils.getLocation();
        if (HypixelUtils.isHypixel() && ((!location.inGame() && HytilsConfig.hideUselessArmorStands) || (HytilsConfig.hideUselessArmorStandsGame && location.inGame() && location.getGameType().isPresent() && (location.getGameType().get() == GameType.SKYBLOCK || location.getGameType().get() == GameType.BEDWARS || location.getGameType().get() == GameType.SKYWARS || location.getMode().orElse("").contains("BRIDGE"))))) {
            if (entity instanceof EntityArmorStand) {
                String unformattedArmorStandName = EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCustomNameTag().toLowerCase());
                for (String armorStands : ArmorStandHandler.INSTANCE.armorStandNames) {
                    if (unformattedArmorStandName.contains(armorStands)) {
                        event.cancelled = true;
                        break;
                    }
                }
            }
        }
    }

}
