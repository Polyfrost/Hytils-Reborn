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

package org.polyfrost.hytils.handlers.chat.modules.modifiers;

import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import org.polyfrost.hytils.handlers.language.LanguageData;
import org.polyfrost.hytils.handlers.lobby.limbo.LimboLimiter;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;

public class DefaultChatRestyler implements ChatReceiveModule {

    private static int playerCount = -1;
    private static int maxPlayerCount = -1;

    /**
     * Normally this wouldn't be static but it has to be called from a static method so it has to be static.
     * As long as we don't make multiple DefaultChatRestyler objects it should be fine.
     * (Called by {@link LimboLimiter#onWorldChange(WorldEvent.Unload)})
     */
    public static void reset() {
        playerCount = maxPlayerCount = -1;
    }

    private static String pad(String n) {
        if (HytilsConfig.padPlayerCount) {
            return StringUtils.repeat('0', String.valueOf(maxPlayerCount).length() - n.length()) + n;
        } else {
            return n;
        }
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public void onMessageReceived(@NotNull ChatEvent.Receive event) {
        IChatComponent component = event.getMessage();
        String message = component.getFormattedText().trim();
        final String unformattedMessage = event.getFullyUnformattedMessage().trim();
        final List<IChatComponent> siblings = component.getSiblings();

        final LanguageData language = getLanguage();
        Matcher joinMatcher = language.chatRestylerGameJoinStyleRegex.matcher(message);

        if (joinMatcher.matches()) {
            String amount = joinMatcher.group("amount").replaceAll("(?i)\u00a7[\\da-fk-or]", "");
            String[] amounts = amount.substring(1, amount.length() - 1).split("/");
            playerCount = Integer.parseInt(amounts[0]);
            maxPlayerCount = Integer.parseInt(amounts[1]);
        }

        if (HytilsConfig.shortChannelNames) {
            Matcher partyMatcher = language.chatRestylerPartyPatternRegex.matcher(message);
            Matcher guildMatcher = language.chatRestylerGuildPatternRegex.matcher(message);
            Matcher friendMatcher = language.chatRestylerFriendPatternRegex.matcher(message);
            Matcher officerMatcher = language.chatRestylerOfficerPatternRegex.matcher(message);
            if (partyMatcher.find()) {
                event.setMessage(shortenChannelName(event.getMessage(), language.chatRestylerPartyPatternRegex.pattern(),
                    partyMatcher.group(1) + "P " + partyMatcher.group(3), false));
            } else if (guildMatcher.find()) {
                event.setMessage(shortenChannelName(event.getMessage(), language.chatRestylerGuildPatternRegex.pattern(),
                    guildMatcher.group(1) + "G >", true));
            } else if (friendMatcher.find()) {
                event.setMessage(shortenChannelName(event.getMessage(), language.chatRestylerFriendPatternRegex.pattern(),
                    friendMatcher.group(1) + "F >", true));
            } else if (officerMatcher.find()) {
                event.setMessage(shortenChannelName(event.getMessage(), language.chatRestylerOfficerPatternRegex.pattern(),
                    officerMatcher.group(1) + "O >", false));
            }
        }

        if (HytilsConfig.pmShortChannelNames) {
            Matcher privateMessageToMatcher = language.chatRestylerPrivateMessageToPatternRegex.matcher(message);
            Matcher privateMessageFromMatcher = language.chatRestylerPrivateMessageFromPatternRegex.matcher(message);
            if (privateMessageToMatcher.find()) {
                event.setMessage(shortenChannelName(event.getMessage(), language.chatRestylerPrivateMessageToPatternRegex.pattern(),
                    "§d" + "PM >" + privateMessageToMatcher.group(3) + ":" + privateMessageToMatcher.group(4), true));
            } else if (privateMessageFromMatcher.find()) {
                event.setMessage(shortenChannelName(event.getMessage(), language.chatRestylerPrivateMessageFromPatternRegex.pattern(),
                    "§5" + "PM <" + privateMessageFromMatcher.group(3) + ":" + privateMessageFromMatcher.group(4), true));
            }
        }

        if (HytilsConfig.coloredStatuses) {
            Matcher statusMatcher = getLanguage().chatRestylerStatusPatternRegex.matcher(component.getFormattedText().trim());
            if (statusMatcher.matches()) {
                final String status = statusMatcher.group("status");
                if (status.equalsIgnoreCase("joined")) {
                    event.setMessage(colorMessage(statusMatcher.group("type") + " > &r" + statusMatcher.group("player") + " &r&ajoined&e."));
                } else if (status.equalsIgnoreCase("left")) {
                    event.setMessage(colorMessage(statusMatcher.group("type") + " > &r" + statusMatcher.group("player") + " &r&cleft&e."));
                }
            }
        }

        // Currently unformattedMessage doesn't need to be changed but I'm leaving these in, commented, in case it's
        // changed in the future and they need to be padded.
        if (HytilsConfig.padPlayerCount) {
            Matcher mf = language.chatRestylerFormattedPaddingPatternRegex.matcher(message);
            // Matcher mu = unformattedPaddingPattern.matcher(unformattedMessage); regex: \\((\\d{1,2})/(\\d{1,3})\\)

            if (mf.find(0)) { // this only matches a small part so we need find()
                message = message.replaceAll(language.chatRestylerFormattedPaddingPatternRegex.toString(), "(§r§b" + pad(mf.group(1)) + "§r§r§r§e/§r§b" + mf.group(2) + "§r§r§r§e)");
                // message = message.replaceAll(unformattedPaddingPattern.toString(), "(" + pad(mu.group(1)) + "/" + mu.group(2) + ")");

                joinMatcher = language.chatRestylerGameJoinStyleRegex.matcher(message); // recalculate since we padded
                event.setMessage(new ChatComponentText(message));
            }
        }

        if (HytilsConfig.gameStatusRestyle) { // TODO: all the code following this might have room for optimization, should be looked into
            if (joinMatcher.matches()) {
                if (HytilsConfig.playerCountBeforePlayerName) {
                    event.setMessage(colorMessage("&a&l+ &e" + joinMatcher.group("amount")
                        + " &" + joinMatcher.group("color") + (message.contains("§k") ? "§k" : "") + joinMatcher.group("player")));
                } else {
                    event.setMessage(colorMessage("&a&l+ &" + joinMatcher.group("color") + (message.contains("§k") ? "§k" : "") + joinMatcher.group("player") + " &r&e" +
                        joinMatcher.group("amount")));
                }
            } else {
                Matcher leaveMatcher = language.chatRestylerGameLeaveStyleRegex.matcher(message);
                if (leaveMatcher.matches()) {
                    if (HytilsConfig.playerCountOnPlayerLeave) {
                        if (HytilsConfig.playerCountBeforePlayerName) {
                            event.setMessage(colorMessage("&c&l- &e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount +
                                "&e) &" + leaveMatcher.group("color") + (message.contains("§k") ? "§k" : "") + leaveMatcher.group("player")));
                        } else {
                            event.setMessage(colorMessage("&c&l- &" + leaveMatcher.group("color") + (message.contains("§k") ? "§k" : "") +
                                leaveMatcher.group("player") + " &r&e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e)"));
                        }
                    } else {
                        event.setMessage(colorMessage("&c&l- &" + leaveMatcher.group("color") + (message.contains("§k") ? "§k" : "") + leaveMatcher.group("player")));
                    }
                } else {
                    Matcher startCounterMatcher = language.chatRestylerGameStartCounterStyleRegex.matcher(unformattedMessage);

                    if (startCounterMatcher.matches()) {
                        // if the format (below) is changed, remember to update the regex for it (chatRestylerGameStartCounterOutputStyle)
                        event.setMessage(colorMessage("&e&l* &a" + (startCounterMatcher.group("title")) + " &b&l" + startCounterMatcher.group("time") + " &a" + startCounterMatcher.group("unit")));
                    } else {
                        if ("We don't have enough players! Start cancelled.".equals(unformattedMessage)) {
                            event.setMessage(colorMessage("&e&l* &cStart cancelled."));
                        } else if ("We don't have enough players! Start delayed.".equals(unformattedMessage)) {
                            event.setMessage(colorMessage("&e&l* &cStart delayed."));
                        }
                    }
                }
            }
        } else {
            if (HytilsConfig.playerCountOnPlayerLeave) {
                Matcher leaveMater = language.chatRestylerGameLeaveStyleRegex.matcher(message);
                if (leaveMater.matches()) {
                    if (HytilsConfig.playerCountBeforePlayerName) {
                        event.setMessage(colorMessage("&e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e) " + message));
                    } else {
                        event.setMessage(colorMessage(message.substring(0, message.length() - 3) + " &e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e)!"));
                    }
                    return;
                }
            }
            if (HytilsConfig.playerCountBeforePlayerName) {
                if (joinMatcher.matches()) {
                    event.setMessage(colorMessage("&e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e) " + message.split(" \\(")[0] + "!"));
                }
            }
        }

        HytilsReborn.INSTANCE.getChatHandler().fixStyling(event.getMessage(), siblings);
    }

    /**
     * Handles the replacement of channel names
     * Loops through all siblings to find a replacement
     *
     * @param message              The message being modified
     * @param pattern              The regular expression to check the message and it's components against
     * @param replacement          The text that replaces what is matched by the regular expression
     * @param checkParentComponent Whether or not to check the parent chat component
     */
    private ChatComponentText shortenChannelName(IChatComponent message, String pattern, String replacement, boolean checkParentComponent) {
        String originalText = message.getUnformattedTextForChat();
        if (checkParentComponent && !originalText.contains("\u00a7")) {
            originalText = (message.getChatStyle().getFormattingCode() + originalText + EnumChatFormatting.RESET).replaceAll(pattern, replacement);
        }
        ChatComponentText copy = (ChatComponentText) new ChatComponentText(originalText).setChatStyle(message.getChatStyle());
        for (IChatComponent sibling : message.getSiblings()) {
            copy.appendSibling(new ChatComponentText(sibling.getUnformattedTextForChat().replaceAll(pattern, replacement)).setChatStyle(sibling.getChatStyle()));
        }
        return copy;
    }
}
