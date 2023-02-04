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

package cc.woverflow.hytils.handlers.lobby.npc;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.config.HytilsConfig;
import com.google.common.collect.Collections2;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;

public class NPCHandler {

    @SubscribeEvent
    public void onEntityRender(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (!HypixelUtils.INSTANCE.isHypixel()) {
            return;
        }
        final LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();

        // hypixel marks npc uuids as version 2
        if (event.entity.getUniqueID().version() == 2 || (event.entity instanceof EntityVillager)) {
            if (HytilsConfig.npcHider && LocrawUtil.INSTANCE.getLocrawInfo() != null && !LocrawUtil.INSTANCE.isInGame()) {
                event.setCanceled(true);
            }
        } else if (HytilsConfig.hideNonNPCs && locraw != null && locraw.getGameType() == LocrawInfo.GameType.SKYBLOCK && !(event.entity instanceof EntityArmorStand && !event.entity.getCustomNameTag().toLowerCase().trim().isEmpty()) && event.entity instanceof EntityOtherPlayerMP) {
            event.setCanceled(true);
        }
    }

    public static Collection<NetworkPlayerInfo> hideTabNpcs(Collection<NetworkPlayerInfo> playerInfoCollection) {
        if (playerInfoCollection == null) return null;
        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (!HypixelUtils.INSTANCE.isHypixel() || !HytilsConfig.hideNpcsInTab) {
            return playerInfoCollection;
        } else {
            if (HytilsConfig.keepImportantNpcsInTab && (locraw == null || locraw.getGameType() == LocrawInfo.GameType.SKYBLOCK || locraw.getGameType() == LocrawInfo.GameType.REPLAY)) {
                return playerInfoCollection;
            }

            return Collections2.filter(playerInfoCollection, player -> player != null && player.getGameProfile().getId().version() != 2);
        }
    }
}
