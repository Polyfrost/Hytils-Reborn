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

package cc.woverflow.hytils.handlers.chat.modules.triggers;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import gg.essential.api.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

public class AutoWB implements ChatReceiveModule {

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoWB;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        String msg = event.message.getFormattedText().trim();
        Matcher matcher = HytilsReborn.INSTANCE.getLanguageHandler().getCurrent().chatRestylerStatusPatternRegex.matcher(msg);
        if (matcher.matches()) {
            String chatType = null;
            String name = matcher.group("player");
            if ((matcher.group("type").equals("§2Guild") || matcher.group("type").equals("§2§2G")) && matcher.group("status").equals("joined")) {
                if (HytilsConfig.guildAutoWB) {
                    chatType = "/gc ";
                } else {
                    return;
                }
            }
            if ((matcher.group("type").equals("§aFriend") || matcher.group("type").equals("§a§aF")) && matcher.group("status").equals("joined")) {
                if (HytilsConfig.friendsAutoWB) {
                    chatType = "/msg " + name + " ";
                } else {
                    return;
                }
            }
            String message = HytilsConfig.autoWBMessage1.replace("%player%", name);
            final String finalChatType = chatType;
            if (HytilsConfig.randomAutoWB) {
                try {
                    Multithreading.schedule(() -> Minecraft.getMinecraft().thePlayer.sendChatMessage(finalChatType + getNextMessage(name)), HytilsConfig.autoWBCooldown, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Multithreading.schedule(() -> Minecraft.getMinecraft().thePlayer.sendChatMessage(finalChatType + message), HytilsConfig.autoWBCooldown, TimeUnit.SECONDS);
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
}
