/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
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

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class AdBlocker implements ChatReceiveModule {

    @Override
    public int getPriority() {
        return -3;
    }

    /**
     * [/](party join or join party) or (p join) or (party me) or (guild join or join guild) or (g join)
     * <p>
     * Blocks twitch.tv youtube.com/youtu.be
     * <p>
     * ah + visit are common words "ah yes" would flag best to keep /ah and /visit for now?
     * <p>
     * Blocks (/visit|ah playername) or (visit /playername) or common strings (visit me|duel me|my ah|my smp)
     * <p>
     * https://regexr.com/5ct10 current tests
     * <p>
     * Blocks advertisements to join German SkyBlock guilds
     * <p>
     * Blocks stuff related with lowballing
     * <p>
     * TODO: add more phrases to regex
     */
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final String message = event.message.getUnformattedText().toLowerCase(Locale.ENGLISH);
        if ((message.startsWith("-") && message.endsWith("-")) || (message.startsWith("▬") && message.endsWith("▬")) || (message.startsWith("≡") && message.endsWith("≡")) || (message.startsWith("?") && message.endsWith("?")) || (!message.contains(": ")) || (message.contains(Minecraft.getMinecraft().getSession().getUsername().toLowerCase())))
            return;
        if (getLanguage().chatCommonAdvertisementsRegex.matcher(message).find(0)) {
            event.setCanceled(true);
        }

        if (!HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby()) return;
        if (getLanguage().chatRankBeggingRegex.matcher(message).find(0)) {
            event.setCanceled(true);
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.playerAdBlock;
    }
}
