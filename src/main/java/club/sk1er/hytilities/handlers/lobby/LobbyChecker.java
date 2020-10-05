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
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Pattern;

public class LobbyChecker {

    private final Pattern lobbyPattern = Pattern.compile("(.+)?lobby(.+)");
    private boolean lobbyStatus;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void retrieveLocraw(ClientChatReceivedEvent event) {
        if (!MinecraftUtils.isHypixel() || Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation() == null) {
            this.lobbyStatus = false;
            return;
        }

        this.lobbyStatus = this.lobbyPattern.matcher(Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation().getServerId()).matches();
    }

    public boolean playerIsInLobby() {
        return this.lobbyStatus;
    }
}
