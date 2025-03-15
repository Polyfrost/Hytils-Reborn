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

package org.polyfrost.hytils.handlers.render;

import dev.deftu.omnicore.client.OmniClient;
import net.hypixel.data.type.GameType;
import net.minecraft.world.World;
import org.polyfrost.oneconfig.api.event.v1.events.PlayerInteractEvent;
import org.polyfrost.oneconfig.api.event.v1.events.PostWorldRenderEvent;
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.events.TitleEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import org.polyfrost.hytils.util.WaypointUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChestHighlighter {
    private final List<BlockPos> highlightedChestPositions = new CopyOnWriteArrayList<>();

    @Subscribe
    public void onInteract(PlayerInteractEvent event) {
        if (!HytilsConfig.highlightChests || event.getType() != PlayerInteractEvent.Type.BLOCK || isNotSupported()) {
            return;
        }

        World world = OmniClient.getWorld();
        if (world == null) {
            return; // Should never happen
        }

        BlockPos pos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileEntityChest)) {
            return;
        }

        switch (event.getAction()) {
            case RIGHT:
                if (!highlightedChestPositions.contains(pos)) {
                    highlightedChestPositions.add(pos);
                }

                break;
            case LEFT:
                highlightedChestPositions.remove(pos);
                break;
        }
    }

    @Subscribe
    public void onWorldChange(WorldEvent.Unload event) {
        highlightedChestPositions.clear();
    }

    @Subscribe
    public void onTitle(TitleEvent event) {
        if (EnumChatFormatting.getTextWithoutFormattingCodes(event.getSubtitle()).equals("All chests have been refilled!")) {
            highlightedChestPositions.clear();
        }
    }

    @Subscribe
    public void onWorldRendered(PostWorldRenderEvent event) {
        if (!HytilsConfig.highlightChests) return;
        if (isNotSupported()) return;
        if (highlightedChestPositions.isEmpty())
            return;
        for (TileEntity entity : Minecraft.getMinecraft().theWorld.loadedTileEntityList) {
            if (entity instanceof TileEntityChest) {
                BlockPos pos = entity.getPos();
                if (!highlightedChestPositions.contains(pos)) continue;
                WaypointUtil.drawBoundingBox(event, pos, HytilsConfig.highlightChestsColor);
            }
        }
    }

    private boolean isNotSupported() {
        return !HypixelUtils.getLocation().getGameType().isPresent() || (HypixelUtils.getLocation().getGameType().get() != GameType.SKYWARS && HypixelUtils.getLocation().getGameType().get() != GameType.SURVIVAL_GAMES && (HypixelUtils.getLocation().getGameType().get() != GameType.DUELS || !HypixelUtils.getLocation().getMode().orElse("").contains("_SW_")));
    }
}
