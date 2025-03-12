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

package org.polyfrost.hytils.handlers.chat.modules.modifiers;

import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import org.polyfrost.hytils.mixin.GuiNewChatAccessor;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class GameStartCompactor implements ChatReceiveModule {

    @Override
    public int getPriority() {
        return 7;
    }

    public static IChatComponent lastMessage = null;

    @Override
    public void onMessageReceived(@NotNull ChatEvent.Receive event) {
        IChatComponent message = VanillaConverter.toVanillaText(event.getMessage());
        final Matcher gameStartMatcher = getLanguage().chatRestylerGameStartCounterStyleRegex.matcher(event.getFullyUnformattedMessage());
        final Matcher chatRestylerMatcher = getLanguage().chatRestylerGameStartCounterOutputStyleRegex.matcher(message.getFormattedText());
        if (gameStartMatcher.matches() || (HytilsConfig.gameStatusRestyle && chatRestylerMatcher.matches())) {
            if (lastMessage != null) {
                final GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                final GuiNewChatAccessor accessor = ((GuiNewChatAccessor) chat);
                final List<IChatComponent> oldTimerLines = GuiUtilRenderComponents.splitText(lastMessage, MathHelper.floor_float((float) chat.getChatWidth() / chat.getChatScale()), Minecraft.getMinecraft().fontRendererObj, false, false);
                removeChatLines(accessor.getChatLines(), Lists.newArrayList(lastMessage));
                removeChatLines(accessor.getDrawnChatLines(), oldTimerLines);
            }

            lastMessage = message.createCopy();
        }
    }

    private void removeChatLines(List<ChatLine> currentLines, List<IChatComponent> oldTimerLines) {
        Iterator<ChatLine> iterator = currentLines.iterator();
        while (iterator.hasNext()) {
            final ChatLine drawnLine = iterator.next();
            if (drawnLine.getChatComponent().getFormattedText().contains(lastMessage.getFormattedText())) {
                iterator.remove();
                return;
            }
            for (IChatComponent oldTimerLine : oldTimerLines) {
                if (drawnLine.getChatComponent().getFormattedText().contains(oldTimerLine.getFormattedText())) {
                    iterator.remove();
                    oldTimerLines.remove(oldTimerLine);
                    break;
                }
            }
            if (oldTimerLines.isEmpty()) return;
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.cleanerGameStartAnnouncements;
    }

}
