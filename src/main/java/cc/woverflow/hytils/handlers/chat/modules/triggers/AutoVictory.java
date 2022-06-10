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
import cc.woverflow.hytils.events.TitleEvent;
import cc.woverflow.hytils.handlers.cache.PatternHandler;
import cc.woverflow.hytils.handlers.chat.ChatReceiveResetModule;
import cc.woverflow.hytils.util.HypixelAPIUtils;
import cc.woverflow.hytils.util.locraw.LocrawInformation;
import gg.essential.api.EssentialAPI;
import gg.essential.api.utils.Multithreading;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.regex.Pattern;

public class AutoVictory implements ChatReceiveResetModule {
    private boolean victoryDetected = false;

    public AutoVictory() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoGetWinstreak || HytilsConfig.autoGetGEXP;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        String unformattedText = UTextComponent.Companion.stripFormatting(event.message.getUnformattedText());
        if (PatternHandler.INSTANCE.gameEnd.size() != 0) {
            if (!victoryDetected) { // prevent victories being detected twice
                Multithreading.runAsync(() -> { //run this async as getting from the API normally would freeze minecraft
                    for (Pattern triggers : PatternHandler.INSTANCE.gameEnd) {
                        if (triggers.matcher(unformattedText).matches()) {
                            doNotification();
                            return;
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void onTitle(TitleEvent event) {
        if (!victoryDetected) {
            final String title = EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle().toLowerCase(Locale.ENGLISH));
            if (title.equals("victory!") || title.equals("game over") || title.equals("game over!") || title.endsWith(" wins!")) {
                Multithreading.runAsync(this::doNotification);
            }
        }
    }

    private void doNotification() {
        victoryDetected = true;
        if (HytilsConfig.autoGetGEXP) {
            if (!HytilsConfig.gexpMode) {
                try {
                    if (HypixelAPIUtils.getGEXP()) {
                        EssentialAPI.getNotifications()
                            .push(
                                HytilsReborn.MOD_NAME,
                                "You currently have " + HypixelAPIUtils.gexp + " daily guild EXP."
                            );
                        return;
                    }
                } catch (Exception ignored) {

                }
                EssentialAPI.getNotifications()
                    .push(HytilsReborn.MOD_NAME, "There was a problem trying to get your GEXP.");
            } else {
                try {
                    if (HypixelAPIUtils.getWeeklyGEXP()) {
                        EssentialAPI.getNotifications()
                            .push(
                                HytilsReborn.MOD_NAME,
                                "You currently have " + HypixelAPIUtils.gexp + " weekly guild EXP."
                            );
                        return;
                    }
                } catch (Exception ignored) {

                }
                EssentialAPI.getNotifications()
                    .push(HytilsReborn.MOD_NAME, "There was a problem trying to get your GEXP.");
            }
        }
        if (isSupportedMode(HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation()) && HytilsConfig.autoGetWinstreak) {
            try {
                if (HypixelAPIUtils.getWinstreak()) {
                    EssentialAPI.getNotifications().push(
                        HytilsReborn.MOD_NAME,
                        "You currently have a " + HypixelAPIUtils.winstreak + " winstreak."
                    );
                    return;
                }
            } catch (Exception ignored) {

            }
            EssentialAPI.getNotifications()
                .push(HytilsReborn.MOD_NAME, "There was a problem trying to get your winstreak.");
        }
    }

    @Override
    public void reset() {
        victoryDetected = false;
        Minecraft.getMinecraft().ingameGUI.displayedTitle = "";
        Minecraft.getMinecraft().ingameGUI.displayedSubTitle = "";
    }

    private boolean isSupportedMode(LocrawInformation locraw) {
        if (locraw != null && locraw.getGameType() != null) {
            switch (locraw.getGameType()) {
                case BED_WARS:
                case SKY_WARS:
                case DUELS:
                    return true;
            }
        }
        return false;
    }
}
