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
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.polyfrost.oneconfig.api.event.v1.events.ChatReceiveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class AutoPartyWarpConfirm implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ChatReceiveEvent event) {
        final String message = event.getFullyUnformattedMessage();
        if (message.equals(getLanguage().autoPartyWarpConfirm)) {
            event.cancelled = true;
            Multithreading.schedule(() -> Minecraft.getMinecraft().thePlayer.sendChatMessage("/p warp"), 1500, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoPartyWarpConfirm;
    }

    @Override
    public int getPriority() {
        return 3;
    }
}
