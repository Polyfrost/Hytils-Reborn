/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wyvest.hytilities.handlers.lobby.armorstands;

import gg.essential.api.EssentialAPI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.handlers.game.GameType;
import net.wyvest.hytilities.util.locraw.LocrawInformation;

public class ArmorStandHider {
    private static final String[] armorStandNames = {"click", "mystery vault", "daily reward tokens", "advent calendar reward", "free rewards", "special holiday quests", "happy holidays",
        "festive floors", "museums", "hype", "coming soon", " set #", "fireball/tnt jumping", "parkour starts this way", "go ahead into the cave", "holiday mode", "new update", "new modes & maps",
        "challenges released", "bug fixes & qol update"};

    @SubscribeEvent
    public void onEntityRenderer(RenderLivingEvent.Pre<EntityLivingBase> event) {
        final LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (HytilitiesConfig.hideUselessArmorStands && EssentialAPI.getMinecraftUtil().isHypixel() && Hytilities.INSTANCE.getLobbyChecker().playerIsInLobby() || (HytilitiesConfig.hideUselessArmorStandsSkyblock && locraw != null && locraw.getGameType() == GameType.SKYBLOCK)) {
            if (event.entity instanceof EntityArmorStand) {
                String unformattedArmorStandName = event.entity.getCustomNameTag().toLowerCase();
                for (String armorStands : armorStandNames) {
                    if (unformattedArmorStandName.contains(armorStands)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
