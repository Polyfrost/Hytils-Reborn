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

package club.sk1er.hytilities.handlers.lobby;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import gg.essential.api.EssentialAPI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.regex.Pattern;

public class LobbyChecker {
    private final Pattern lobbyPattern = Pattern.compile("(.+)?lobby(.+)");
    private boolean lobbyStatus;
    private int tick;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        final LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !EssentialAPI.getMinecraftUtil().isHypixel() || this.tick >= 52 || locraw == null) {
            return;
        }

        if (++this.tick == 50) {
            this.lobbyStatus = this.lobbyPattern.matcher(locraw.getServerId()).matches();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.tick = 0;
    }

    public boolean playerIsInLobby() {
        return this.lobbyStatus;
    }
}
