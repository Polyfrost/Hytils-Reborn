/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
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

package cc.woverflow.hytils.handlers.lobby.armorstands;

import cc.woverflow.hytils.config.HytilsConfig;
import gg.essential.api.EssentialAPI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.handlers.game.GameType;
import cc.woverflow.hytils.util.locraw.LocrawInformation;

public class ArmorStandHider {
    private static final String[] armorStandNames = {"click", "mystery vault", "daily reward tokens", "advent calendar reward", "free rewards", "special holiday quests",
        "festive floors", "museums", "hype", "coming soon", " set #", "fireball/tnt jumping", "parkour starts this way", "go ahead into the cave", "holiday mode", "new update", "new modes & maps",
        "challenges released", "bug fixes & qol update", "catacombs m7!", "sign posting", "parkour challenge", "your arcade games profile", "crimson isle", "featured", "limited time!", "hytale trailer",
        "happy easter", "happy halloween", "happy holidays", "easter event", "holiday event", "halloween event", "summer event"};

    @SubscribeEvent
    public void onEntityRenderer(RenderLivingEvent.Pre<EntityLivingBase> event) {
        final LocrawInformation locraw = HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (HytilsConfig.hideUselessArmorStands && EssentialAPI.getMinecraftUtil().isHypixel() && HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby() || (HytilsConfig.hideUselessArmorStandsSkyblock && locraw != null && locraw.getGameType() == GameType.SKYBLOCK)) {
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
