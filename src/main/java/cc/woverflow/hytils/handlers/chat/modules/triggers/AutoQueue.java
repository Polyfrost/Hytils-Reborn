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
import cc.woverflow.hytils.handlers.language.LanguageData;
import cc.woverflow.hytils.util.locraw.LocrawInformation;
import gg.essential.api.utils.Multithreading;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AutoQueue implements ChatReceiveModule {

    /**
     * We want this to activate early so that it catches the queue message.
     */
    @Override
    public int getPriority() {
        return -11;
    }

    private String command = null;
    private boolean sentCommand;

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (!HytilsConfig.autoQueue) {
            return;
        }

        final LanguageData language = getLanguage();
        final String message = getStrippedMessage(event.message);
        LocrawInformation locraw = HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (message.startsWith(language.autoQueuePrefixBedwars) || message.startsWith(language.autoQueuePrefix) || message.startsWith(language.autoQueuePrefixMurderMystery) ||
            message.startsWith(language.autoQueuePrefixMurderMysteryAssassins1) || message.startsWith(language.autoQueuePrefixMurderMysteryAssassins2) ||
            message.startsWith(language.autoQueuePrefixMurderMysteryAssassins3) || message.startsWith(language.autoQueuePrefixMurderMysteryAssassins4) ||
            message.startsWith(language.autoQueuePrefixMurderMysteryInfected) && locraw != null) {
            this.command = "/play " + locraw.getGameMode().toLowerCase(Locale.ENGLISH);
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoQueue;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (this.command != null) {
            this.switchGame();
        }
    }

    @SubscribeEvent
    public void onMouseEvent(InputEvent.MouseInputEvent event) {
        if (this.command != null) {
            this.switchGame();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        // stop the command from being spammed, to prevent chat from filling with "please do not spam commands"
        if (event.world.provider.getDimensionId() == 0 && this.sentCommand) {
            this.sentCommand = false;
        }
    }

    private void switchGame() {
        Multithreading.schedule(() -> {
            if (!this.sentCommand) {
                HytilsReborn.INSTANCE.getCommandQueue().queue(this.command);
                this.sentCommand = true;
                this.command = null;
            }

        }, HytilsConfig.autoQueueDelay, TimeUnit.SECONDS);
    }
}
