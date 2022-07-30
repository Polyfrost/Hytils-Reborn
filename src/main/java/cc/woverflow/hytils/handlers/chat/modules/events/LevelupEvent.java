/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
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

package cc.woverflow.hytils.handlers.chat.modules.events;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.events.HypixelLevelupEvent;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

public class LevelupEvent implements ChatReceiveModule {

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final String unformattedText = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        final Matcher matcher = getLanguage().hypixelLevelUpRegex.matcher(unformattedText.trim());
        if (matcher.find()) {
            if (unformattedText.contains(":")) return; // TODO: This is a temporary solution to prevent regular player messages from triggering it. Fix the regex!
            final String level = matcher.group("level");
            if (StringUtils.isNumeric(level)) {
                MinecraftForge.EVENT_BUS.post(new HypixelLevelupEvent(Integer.parseInt(level)));
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.broadcastLevelup;
    }

    @Override
    public int getPriority() {
        return -3;
    }

    @SubscribeEvent
    public void levelUpEvent(HypixelLevelupEvent event) {
        HytilsReborn.INSTANCE.getCommandQueue().queue("/gchat Levelup! I am now Hypixel Level: " + event.getLevel() + "!");
    }
}
