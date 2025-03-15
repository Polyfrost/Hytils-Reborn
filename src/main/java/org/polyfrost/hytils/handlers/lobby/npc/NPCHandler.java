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

package org.polyfrost.hytils.handlers.lobby.npc;

import net.hypixel.data.type.GameType;
import org.polyfrost.hytils.config.HytilsConfig;
import com.google.common.collect.Collections2;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import org.polyfrost.oneconfig.api.event.v1.events.RenderLivingEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

import java.util.Collection;

public class NPCHandler {

    @Subscribe
    public void onEntityRender(RenderLivingEvent.Pre event) {
        Object entityRaw = event.getEntity();
        if (!(entityRaw instanceof EntityLivingBase)) {
            return;
        }

        EntityLivingBase entity = (EntityLivingBase) entityRaw;
        if (!HypixelUtils.isHypixel()) {
            return;
        }
        final HypixelUtils.Location location = HypixelUtils.getLocation();

        // hypixel marks npc uuids as version 2
        if (entity.getUniqueID().version() == 2 || (entity instanceof EntityVillager)) {
            if (HytilsConfig.npcHider && !location.inGame()) {
                event.cancelled = true;
            }
        } else if (HytilsConfig.hideNonNPCs && location.getGameType().orElse(null) == GameType.SKYBLOCK && !(entity instanceof EntityArmorStand && !entity.getCustomNameTag().toLowerCase().trim().isEmpty()) && entity instanceof EntityOtherPlayerMP) {
            event.cancelled = true;
        }
    }

    public static Collection<NetworkPlayerInfo> hideTabNpcs(Collection<NetworkPlayerInfo> playerInfoCollection) {
        if (playerInfoCollection == null) return null;
        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (!HypixelUtils.isHypixel() || !HytilsConfig.hideNpcsInTab) {
            return playerInfoCollection;
        } else {
            if (HytilsConfig.keepImportantNpcsInTab && location.getGameType().isPresent() && (location.getGameType().get() == GameType.SKYBLOCK || location.getGameType().get() == GameType.REPLAY)) {
                return playerInfoCollection;
            }

            return Collections2.filter(playerInfoCollection, player -> player != null && player.getGameProfile().getId().version() != 2);
        }
    }
}
