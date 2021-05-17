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

package club.sk1er.hytilities.handlers.chat.modules.modifiers;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatReceiveModule;
import club.sk1er.hytilities.handlers.language.LanguageData;
import club.sk1er.hytilities.handlers.lobby.limbo.LimboLimiter;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
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
        if (HytilitiesConfig.padPlayerCount) {
            return StringUtils.repeat('0', String.valueOf(maxPlayerCount).length() - n.length()) + n;
        } else {
            return n;
        }
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final String message = event.message.getFormattedText().trim();
        final String unformattedMessage = event.message.getUnformattedText().trim();
        final List<IChatComponent> siblings = event.message.getSiblings();

        final LanguageData language = getLanguage();
        Matcher joinMatcher = language.chatRestylerGameJoinStyleRegex.matcher(message);

        if (joinMatcher.matches()) {
            String amount = joinMatcher.group("amount").replaceAll("(?i)\u00a7[\\da-fk-or]", "");
            String[] amounts = amount.substring(1, amount.length() - 1).split("/");
            playerCount = Integer.parseInt(amounts[0]);
            maxPlayerCount = Integer.parseInt(amounts[1]);
        }

        if (HytilitiesConfig.shortChannelNames) {
            Matcher partyMatcher = language.chatRestylerPartyPatternRegex.matcher(message);
            Matcher guildMatcher = language.chatRestylerGuildPatternRegex.matcher(message);
            Matcher friendMatcher = language.chatRestylerFriendPatternRegex.matcher(message);
            Matcher officerMacher = language.chatRestylerOfficerPatternRegex.matcher(message);
            if (partyMatcher.find()) {
                event.message = colorMessage(message.replaceAll(language.chatRestylerPartyPatternRegex.pattern(),
                    partyMatcher.group(1) + "P " + partyMatcher.group(3)));
            } else if (guildMatcher.find()) {
                event.message = colorMessage(message.replaceAll(language.chatRestylerGuildPatternRegex.pattern(),
                    guildMatcher.group(1) + "G >"));
            } else if (friendMatcher.find()) {
                event.message = colorMessage(message.replaceAll(language.chatRestylerFriendPatternRegex.pattern(),
                    friendMatcher.group(1) + "F >"));
            } else if (officerMacher.find()) {
                event.message = colorMessage(message.replaceAll(language.chatRestylerOfficerPatternRegex.pattern(),
                    officerMacher.group(1) + "O >"));
            }
        }

        // Currently unformattedMessage doesn't need to be changed but I'm leaving these in, commented, in case it's
        // changed in the future and they need to be padded.
        if (HytilitiesConfig.padPlayerCount) {
            Matcher mf = language.chatRestylerFormattedPaddingPatternRegex.matcher(message);
//            Matcher mu = unformattedPaddingPattern.matcher(unformattedMessage);
            if (mf.find(0)) { // this only matches a small part so we need find()
                mf.replaceAll("(§r§b" + pad(mf.group(1)) + "§r§e/§r§b" + mf.group(2) + "§r§e)");
//                uf.replaceAll("(" + pad(mu.group(1)) + "/" + mu.group(2) + ")");

                joinMatcher = language.chatRestylerGameJoinStyleRegex.matcher(message); // recalculate since we padded
                event.message = new ChatComponentText(message);
            }
        }

        if (HytilitiesConfig.gameStatusRestyle) { // todo: all the code following this might have room for optimization, should be looked into
            if (joinMatcher.matches()) {
                if (HytilitiesConfig.playerCountBeforePlayerName) {
                    event.message = colorMessage("&a&l+ &e" + joinMatcher.group("amount")
                        + " &" + joinMatcher.group("color") + (message.contains("§k") ? "§k" : "") + joinMatcher.group("player"));
                } else {
                    event.message = colorMessage("&a&l+ &" + joinMatcher.group("color") + (message.contains("§k") ? "§k" : "") + joinMatcher.group("player") + " &r&e" +
                        joinMatcher.group("amount"));
                }
            } else {
                Matcher leaveMatcher = language.chatRestylerGameLeaveStyleRegex.matcher(message);
                if (leaveMatcher.matches()) {
                    if (HytilitiesConfig.playerCountOnPlayerLeave) {
                        if (HytilitiesConfig.playerCountBeforePlayerName) {
                            event.message = colorMessage("&c&l- &e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount +
                                "&e) &" + leaveMatcher.group("color") + (message.contains("§k") ? "§k" : "") + leaveMatcher.group("player"));
                        } else {
                            event.message = colorMessage("&c&l- &" + leaveMatcher.group("color") + (message.contains("§k") ? "§k" : "") +
                                leaveMatcher.group("player") + " &r&e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e)");
                        }
                    } else {
                        event.message = colorMessage("&c&l- &" + leaveMatcher.group("color") + (message.contains("§k") ? "§k" : "") + leaveMatcher.group("player"));
                    }
                } else {
                    Matcher startCounterMatcher = language.chatRestylerGameStartCounterStyleRegex.matcher(unformattedMessage);

                    if (startCounterMatcher.matches()) {
                        // if the format (below) is changed, remember to update the regex for it (chatRestylerGameStartCounterOutputStyle)
                        event.message = colorMessage("&e&l* &a" + (startCounterMatcher.group("title")) + " &b&l" + startCounterMatcher.group("time") + " &a" + startCounterMatcher.group("unit"));
                    } else {
                        if ("We don't have enough players! Start cancelled.".equals(unformattedMessage)) {
                            event.message = colorMessage("&e&l* &cStart cancelled.");
                        }
                    }
                }
            }
        } else {
            if (HytilitiesConfig.playerCountOnPlayerLeave) {
                Matcher leaveMater = language.chatRestylerGameLeaveStyleRegex.matcher(message);
                if (leaveMater.matches()) {
                    if (HytilitiesConfig.playerCountBeforePlayerName) {
                        event.message = colorMessage("&e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e) " + message);
                    } else {
                        event.message = colorMessage(message.substring(0, message.length() - 3) + " &e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e)!");
                    }
                    return;
                }
            }
            if (HytilitiesConfig.playerCountBeforePlayerName) {
                if (joinMatcher.matches()) {
                    event.message = colorMessage("&e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e) " + message.split(" \\(")[0] + "!");
                }
            }
        }

        Hytilities.INSTANCE.getChatHandler().fixStyling(event.message, siblings);
    }
}
