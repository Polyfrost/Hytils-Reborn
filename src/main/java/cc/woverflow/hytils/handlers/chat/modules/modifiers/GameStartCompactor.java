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

package cc.woverflow.hytils.handlers.chat.modules.modifiers;

import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import cc.woverflow.hytils.mixin.GuiNewChatAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;

public class GameStartCompactor implements ChatReceiveModule {

    @Override
    public int getPriority() {
        return 7;
    }

    // Used to determine if the property has changed. TODO: Once ModCore 2 is out, have a listener for this.
    private boolean prevConfigValue = HytilsConfig.cleanerGameStartAnnouncements;
    private IChatComponent lastMessage = null;

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final Matcher gameStartMatcher = getLanguage().chatRestylerGameStartCounterStyleRegex.matcher(event.message.getUnformattedText());
        final Matcher chatRestylerMatcher = getLanguage().chatRestylerGameStartCounterOutputStyleRegex.matcher(event.message.getFormattedText());
        if (gameStartMatcher.matches() || (HytilsConfig.gameStatusRestyle && chatRestylerMatcher.matches())) {
            if (lastMessage != null) {
                final GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                final List<IChatComponent> oldTimerLines = GuiUtilRenderComponents.splitText(lastMessage, MathHelper.floor_float((float) chat.getChatWidth() / chat.getChatScale()), Minecraft.getMinecraft().fontRendererObj, false, false);
                final GuiNewChatAccessor accessor = ((GuiNewChatAccessor) chat);
                for (int i = accessor.getDrawnChatLines().size() - 1; i >= 0; i--) { // tries to find the last message in drawn chat lines, if found, it removes them
                    final ChatLine drawnLine = accessor.getDrawnChatLines().get(i);
                    for (IChatComponent oldTimerLine : oldTimerLines) {
                        if (drawnLine.getChatComponent().getFormattedText().equals(oldTimerLine.getFormattedText())) {
                            accessor.getDrawnChatLines().remove(i);
                            oldTimerLines.remove(oldTimerLine);
                            break;
                        }
                    }
                }
            }

            lastMessage = event.message.createCopy();
        }
    }

    @Override
    public boolean isEnabled() {
        // don't affect messages sent when the config value was false (or was true and was set to false later)
        if (!prevConfigValue && HytilsConfig.cleanerGameStartAnnouncements) {
            prevConfigValue = true;
            lastMessage = null;
        }

        return HytilsConfig.cleanerGameStartAnnouncements;
    }

}
