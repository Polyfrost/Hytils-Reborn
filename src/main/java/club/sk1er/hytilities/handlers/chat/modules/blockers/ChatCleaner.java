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

package club.sk1er.hytilities.handlers.chat.modules.blockers;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatReceiveModule;
import club.sk1er.hytilities.handlers.game.GameType;
import club.sk1er.hytilities.handlers.language.LanguageData;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * todo: split up this class into separate modules
 */
public class ChatCleaner implements ChatReceiveModule {
    /**
     * Hypixel uses two different linebreakers for some reason.
     * Top one for info in gamemodes and the bottom one for f.ex. the help menu for /party
     * §r§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬§r
     * §9§m---------------------§r
     * This regex also grabs the bold color code so that it can resize it properly.
     */
    private final Pattern lineBreakerPattern = Pattern.compile("\\b(§l)?\\b([-▬])+");

    @Override
    public int getPriority() {
        return -1;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final LanguageData language = getLanguage();
        final LocrawInformation locrawInformation = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

        if (HytilitiesConfig.lobbyStatus) {
            for (String messages : language.chatCleanerJoinMessageTypes) {
                if (message.contains(messages)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        if (HytilitiesConfig.mvpEmotes) {
            Matcher matcher = language.chatCleanerMvpEmotesRegex.matcher(event.message.getFormattedText());

            if (matcher.find(0)) {
                event.message = new ChatComponentText(event.message.getFormattedText().replaceAll(language.chatCleanerMvpEmotesRegex.pattern(), ""));
                return;
            }
        }

        if (HytilitiesConfig.lineBreakerTrim) {
            message = event.message.getFormattedText();
            Matcher regex = lineBreakerPattern.matcher(message);
            if (regex.find()) {
                String linebreak = regex.group();
                int chatWidth = Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatWidth();
                String newLineBreaker = Minecraft.getMinecraft().fontRendererObj.trimStringToWidth(linebreak, chatWidth);
                message = regex.replaceAll(newLineBreaker);
                event.message = new ChatComponentText(message);
                return;
            }

        }

        if (HytilitiesConfig.mysteryBoxAnnouncer) {
            Matcher matcher = language.chatCleanerMysteryBoxFindRegex.matcher(message);

            if (matcher.matches()) {
                String player = matcher.group("player");
                boolean playerBox = !player.contains(Minecraft.getMinecraft().thePlayer.getName());

                if (!playerBox || !player.startsWith("You")) {
                    event.setCanceled(true);
                    return;
                }
            } else if (message.startsWith("[Mystery Box]")) {
                event.setCanceled(true);
                return;
            }
        }

        if (HytilitiesConfig.gameAnnouncements) {
            if (language.chatCleanerGameAnnouncementRegex.matcher(message).matches()) {
                event.setCanceled(true);
                return;
            }
        }

        if (HytilitiesConfig.hypeLimitReminder && message.startsWith(language.chatCleanerHypeLimit)) {
            event.setCanceled(true);
            return;
        }

        if (HytilitiesConfig.soulWellAnnouncer) {
            if (language.chatCleanerSoulWellFindRegex.matcher(message).matches()) {
                event.setCanceled(true);
                return;
            }
        }

        if (locrawInformation != null) {
            if (HytilitiesConfig.bedwarsAdvertisements && locrawInformation.getGameType() == GameType.BED_WARS) {
                if (language.chatCleanerBedwarsPartyAdvertisementRegex.matcher(message).find()) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        if (HytilitiesConfig.connectionStatus && language.chatCleanerConnectionStatusRegex.matcher(message).matches()) {
            event.setCanceled(true);
        }

        if (HytilitiesConfig.curseOfSpam) {
            if (message.equals("KALI HAS STRIKEN YOU WITH THE CURSE OF SPAM")) {
                event.setCanceled(true);
            }
        }
    }
}
