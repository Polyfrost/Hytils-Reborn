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

package club.sk1er.hytilities.handlers.chat.events;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.events.HypixelLevelupEvent;
import club.sk1er.hytilities.handlers.chat.ChatReceiveModule;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;

public class LevelupEvent implements ChatReceiveModule {
    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        String unformattedText = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

        Matcher matcher = getLanguage().hypixelLevelUpRegex.matcher(unformattedText.trim());
        if (matcher.find()) {
            String level = matcher.group("level");

            if (StringUtils.isNumeric(level)) {
                MinecraftForge.EVENT_BUS.post(new HypixelLevelupEvent(Integer.parseInt(level)));
            }
        }
    }

    @Override
    public boolean isReceiveModuleEnabled() {
        return true;
    }

    @SubscribeEvent
    public void levelUpEvent(HypixelLevelupEvent event) {
        if (HytilitiesConfig.broadcastLevelup) {
            Hytilities.INSTANCE.getCommandQueue().queue("/gchat Levelup! I am now Hypixel Level: " + event.getLevel() + "!");
        }
    }
}
