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

package net.wyvest.hytilities.handlers.lobby.npc;

import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.asm.GuiPlayerTabOverlayTransformer;
import com.google.common.collect.Collections2;
import gg.essential.api.EssentialAPI;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wyvest.hytilities.handlers.game.GameType;
import net.wyvest.hytilities.util.locraw.LocrawInformation;
import org.objectweb.asm.tree.ClassNode;

import java.util.Collection;

public class NPCHider {

    @SubscribeEvent
    public void onEntityRender(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (!HytilitiesConfig.npcHider || !EssentialAPI.getMinecraftUtil().isHypixel() || !Hytilities.INSTANCE.getLobbyChecker().playerIsInLobby()) {
            return;
        }

        // hypixel marks npc uuids as version 2, also hide the Quest Master
        if (event.entity.getUniqueID().version() == 2 || (event.entity instanceof EntityVillager)) {
            event.setCanceled(true);
        }
    }

    /**
     * Used in {@link GuiPlayerTabOverlayTransformer#transform(ClassNode, String)}
     */
    @SuppressWarnings("unused")
    public static Collection<NetworkPlayerInfo> hideTabNpcs(Collection<NetworkPlayerInfo> playerInfoCollection) {
        LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (!EssentialAPI.getMinecraftUtil().isHypixel() || !HytilitiesConfig.hideNpcsInTab) {
            if (!HytilitiesConfig.keepImportantNpcsInTab && (locraw == null || locraw.getGameType() == GameType.SKYBLOCK || locraw.getGameType() == GameType.REPLAY)) {
                return playerInfoCollection;
            }
        } else {
            return Collections2.filter(playerInfoCollection, player -> player != null && player.getGameProfile().getId().version() != 2);
        }
        return playerInfoCollection;
    }
}
