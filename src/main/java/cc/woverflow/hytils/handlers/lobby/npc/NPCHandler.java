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

package cc.woverflow.hytils.handlers.lobby.npc;

import cc.woverflow.hytils.config.HytilsConfig;
import com.google.common.collect.Collections2;
import gg.essential.api.EssentialAPI;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.handlers.game.GameType;
import cc.woverflow.hytils.util.locraw.LocrawInformation;

import java.util.Collection;

public class NPCHandler {

    @SubscribeEvent
    public void onEntityRender(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (!EssentialAPI.getMinecraftUtil().isHypixel()) {
            return;
        }
        final LocrawInformation locraw = HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation();

        // hypixel marks npc uuids as version 2
        if (event.entity.getUniqueID().version() == 2 || (event.entity instanceof EntityVillager)) {
            if (HytilsConfig.npcHider && HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby()) {
                event.setCanceled(true);
            }
        } else if (HytilsConfig.hideNonNPCs && locraw != null && locraw.getGameType() == GameType.SKYBLOCK && !(event.entity instanceof EntityArmorStand && !event.entity.getCustomNameTag().toLowerCase().trim().isEmpty())) {
            event.setCanceled(true);
        }
    }

    public static Collection<NetworkPlayerInfo> hideTabNpcs(Collection<NetworkPlayerInfo> playerInfoCollection) {
        LocrawInformation locraw = HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (!EssentialAPI.getMinecraftUtil().isHypixel() || !HytilsConfig.hideNpcsInTab) {
            return playerInfoCollection;
        } else {
            if (HytilsConfig.keepImportantNpcsInTab && (locraw == null || locraw.getGameType() == GameType.SKYBLOCK || locraw.getGameType() == GameType.REPLAY)) {
                return playerInfoCollection;
            }

            return Collections2.filter(playerInfoCollection, player -> player != null && player.getGameProfile().getId().version() != 2);
        }
    }
}
