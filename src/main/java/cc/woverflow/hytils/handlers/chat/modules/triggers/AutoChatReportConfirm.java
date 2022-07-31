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

package cc.woverflow.hytils.handlers.chat.modules.triggers;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class AutoChatReportConfirm implements ChatReceiveModule {

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().equals("Please type /report confirm to log your report for staff review.")) {
            event.setCanceled(true);
            Multithreading.schedule(() -> Minecraft.getMinecraft().thePlayer.sendChatMessage("/report confirm"), 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoChatReportConfirm;
    }
}
