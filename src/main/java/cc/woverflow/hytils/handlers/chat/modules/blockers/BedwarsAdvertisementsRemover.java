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

package cc.woverflow.hytils.handlers.chat.modules.blockers;

import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class BedwarsAdvertisementsRemover implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        LocrawInfo locrawInformation = getLocraw();
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText().toLowerCase());
        if ((message.startsWith("-") && message.endsWith("-")) || (message.startsWith("▬") && message.endsWith("▬")) || (message.startsWith("≡") && message.endsWith("≡")) || (!message.contains(": ")) || (message.contains(Minecraft.getMinecraft().getSession().getUsername().toLowerCase()))) return;
        if (locrawInformation != null && locrawInformation.getGameType() == LocrawInfo.GameType.BEDWARS && !LocrawUtil.INSTANCE.isInGame()
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
