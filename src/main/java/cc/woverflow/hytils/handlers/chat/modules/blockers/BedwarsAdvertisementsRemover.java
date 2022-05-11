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

package cc.woverflow.hytils.handlers.chat.modules.blockers;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import cc.woverflow.hytils.util.locraw.LocrawInformation;
import cc.woverflow.hytils.handlers.game.GameType;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class BedwarsAdvertisementsRemover implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        LocrawInformation locrawInformation = HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation();
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        if ((message.startsWith("-") && message.endsWith("-")) || (message.startsWith("▬") && message.endsWith("▬")) || (message.startsWith("≡") && message.endsWith("≡")) || (message.startsWith("?") && message.endsWith("?")) || (message.startsWith("they have gifted") && message.endsWith("so far!")))
            return;
        if (locrawInformation != null && locrawInformation.getGameType() == GameType.BED_WARS && HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby()
            && getLanguage().chatCleanerBedwarsPartyAdvertisementRegex.matcher(message).find()) {
            event.setCanceled(true);
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.bedwarsAdvertisements;
    }

    @Override
    public int getPriority() {
        return -1;
    }
}
