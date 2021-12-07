/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  Sk1er LLC
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

package club.sk1er.hytilities.handlers.chat.modules.triggers;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatReceiveModule;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class AutoChatReportConfirm implements ChatReceiveModule {

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().equals("Please type /report confirm to log your report for staff review.")) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/report confirm");
            event.setCanceled(true);
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.autoChatReportConfirm;
    }
}
