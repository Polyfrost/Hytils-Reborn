/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
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

package net.wyvest.hytilities.handlers.chat.modules.triggers;

import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.handlers.chat.ChatReceiveModule;
import net.wyvest.hytilities.util.HypixelAPIUtils;
import gg.essential.api.utils.Multithreading;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class AutoAPI implements ChatReceiveModule {

    private final int apiKeyMessageLength = "Your new API key is ".length();

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        String unformattedText = UTextComponent.Companion.stripFormatting(event.message.getUnformattedText());
        if (unformattedText.startsWith("Your new API key is ")) {
            String tempApiKey = unformattedText.substring(apiKeyMessageLength);
            Multithreading.runAsync(() -> { //run this async as getting from the API normally would freeze minecraft
                if (!HypixelAPIUtils.isValidKey(tempApiKey)
                ) {
                    Hytilities.INSTANCE.sendMessage(EnumChatFormatting.RED + "The API Key was invalid! Please try running the command again.");
                } else {
                    // if the api key is valid add the key to the configuration and save it
                    HytilitiesConfig.apiKey = tempApiKey;
                    Hytilities.INSTANCE.getConfig().markDirty();
                    Hytilities.INSTANCE.getConfig().writeData();
                    Hytilities.INSTANCE.sendMessage(EnumChatFormatting.GREEN + "Your API Key has been automatically configured.");
                }
            });
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.autoGetAPI;
    }
}
