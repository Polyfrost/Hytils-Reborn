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
import net.wyvest.hytilities.handlers.game.GameType;
import net.wyvest.hytilities.handlers.language.LanguageData;
import gg.essential.api.utils.Multithreading;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.wyvest.hytilities.util.locraw.LocrawInformation;
import org.jetbrains.annotations.NotNull;

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
        if (!HytilitiesConfig.autoQueue) {
            return;
        }

        final LanguageData language = getLanguage();
        final String message = getStrippedMessage(event.message);
        if (message.startsWith(language.autoQueuePrefix)) {
            for (IChatComponent component : event.message.getSiblings()) {
                final String compMsg = getStrippedMessage(event.message).trim();
                if (compMsg.equals(language.autoQueueClick)) { // TODO: HELP DOES NOT WORK
                    this.command = component.getChatStyle().getChatClickEvent().getValue();
                }
            }
        }
        LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (locraw != null && locraw.getGameType() == GameType.BED_WARS) {
            if (message.startsWith(language.autoQueuePrefixBedwars)) {
                switch (locraw.getGameMode()) {
                    case "BEDWARS_EIGHT_ONE": // Solo
                        this.command = "/play bedwars_eight_one";
                    case "BEDWARS_EIGHT_TWO": // Doubles
                        this.command = "/play bedwars_eight_two";
                    case "BEDWARS_FOUR_THREE": // Threes
                        this.command = "/play bedwars_four_three";
                    case "BEDWARS_FOUR_FOUR": // Fours
                        this.command = "/play bedwars_four_four"; // 4v4
                    case "BEDWARS_EIGHT_TWO_VOIDLESS": // Voidless Doubles
                        this.command = "/play bedwars_eight_two_voidless";
                    case "BEDWARS_FOUR_FOUR_VOIDLESS": // Voidless Fours
                        this.command = "/play bedwars_four_four_voidless";
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.autoQueue;
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
                Hytilities.INSTANCE.getCommandQueue().queue(this.command);
                this.sentCommand = true;
                this.command = null;
            }

        }, HytilitiesConfig.autoQueueDelay, TimeUnit.SECONDS);
    }
}
