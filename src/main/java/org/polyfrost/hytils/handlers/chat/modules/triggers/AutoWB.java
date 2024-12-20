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

package org.polyfrost.hytils.handlers.chat.modules.triggers;

import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.polyfrost.oneconfig.api.event.v1.events.ChatReceiveEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

public class AutoWB implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ChatReceiveEvent event) {
        String msg = event.message.getFormattedText().trim();
        Matcher matcher = HytilsReborn.INSTANCE.getLanguageHandler().getCurrent().chatRestylerStatusPatternRegex.matcher(msg);
        if (matcher.matches()) {
            final String chatType;
            String status = EnumChatFormatting.getTextWithoutFormattingCodes(matcher.group("status"));
            String name = EnumChatFormatting.getTextWithoutFormattingCodes(matcher.group("player"));
            String type = EnumChatFormatting.getTextWithoutFormattingCodes(matcher.group("type"));
            if (StringUtils.isBlank(status) || StringUtils.isBlank(name) || StringUtils.isBlank(type)) {
                return;
            }
            if (!status.equals("joined")) return;
            if (StringUtils.startsWithAny(type, "Guild", "G")) {
                if (HytilsConfig.guildAutoWB) {
                    chatType = "/gc ";
                } else {
                    return;
                }
            } else if (StringUtils.startsWithAny(type, "Friend", "F")) {
                if (HytilsConfig.friendsAutoWB) {
                    chatType = "/msg " + name + " ";
                } else {
                    return;
                }
            } else {
                return;
            }
            String message = HytilsConfig.autoWBMessage1.replace("%player%", name);
            if (HytilsConfig.randomAutoWB) {
                try {
                    Multithreading.schedule(() -> Minecraft.getMinecraft().thePlayer.sendChatMessage(chatType + getNextMessage(name)), HytilsConfig.autoWBCooldown, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Multithreading.schedule(() -> Minecraft.getMinecraft().thePlayer.sendChatMessage(chatType + message), HytilsConfig.autoWBCooldown, TimeUnit.SECONDS);
            }
        }
    }

    private String getNextMessage(String name) {
        if (HytilsConfig.wbMessages.stream().allMatch(StringUtils::isBlank))
            return HytilsConfig.autoWBMessage1.replace("%player%", name);
        String newMessage = HytilsConfig.wbMessages.get(ThreadLocalRandom.current().nextInt(0, 11));
        if (StringUtils.isBlank(newMessage)) {
            return getNextMessage(name);
        } else {
            return newMessage.replace("%player%", name);
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoWB;
    }

    @Override
    public int getPriority() {
        return -6;
    }
}
