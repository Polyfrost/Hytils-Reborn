/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
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

package cc.woverflow.hytils.handlers.lobby.armorstands;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cc.woverflow.hytils.HytilsReborn;

public class ArmorStandHider {
    private static final String[] armorStandNames = {"click", "mystery vault", "daily reward tokens", "advent calendar reward", "free rewards", "special holiday quests", "release",
        "festive floors", "museums", "hype", "coming soon", " set #", "fireball/tnt jumping", "parkour starts this way", "go ahead into the cave", "holiday mode", "new update", "new modes & maps",
        "bug fixes & qol update", "sign posting", "parkour challenge", "your arcade games profile", "crimson isle", "featured", "limited time", "hytale trailer",
        "happy easter", "happy halloween", "happy holidays", "easter event", "holiday event", "halloween event", "summer event", "goal", "defend", "jump in to score"};

    @SubscribeEvent
    public void onEntityRenderer(RenderLivingEvent.Pre<EntityLivingBase> event) {
        final LocrawInfo locraw = HypixelUtils.INSTANCE.getLocrawInfo();
        if (HypixelUtils.INSTANCE.isHypixel() && locraw != null && (HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby() && HytilsConfig.hideUselessArmorStands || HytilsConfig.hideUselessArmorStandsGame && (locraw.getGameType() == LocrawInfo.GameType.SKYBLOCK || locraw.getGameType() == LocrawInfo.GameType.BEDWARS || locraw.getGameType() == LocrawInfo.GameType.SKYWARS || locraw.getGameMode().contains("BRIDGE")))) {
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
