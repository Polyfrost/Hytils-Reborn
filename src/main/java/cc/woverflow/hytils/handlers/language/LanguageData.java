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

package cc.woverflow.hytils.handlers.language;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Data class for storing the Regex's for each Hypixel language.
 *
 * @author Koding
 */
@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "DuplicatedCode"})
public class LanguageData {
    private boolean hasInitialized = false;

    /**
     * GSON deserialization fields which are loaded in when the file is parsed.
     */
    public String autoQueuePrefixGlobal = "^(?:You died! .+|YOU DIED! .+|You have been eliminated!|You won! .+)$";

    public String autoFriendPattern = "\u00A7m----------------------------------------------------Friend request from (?<name>.+)\\[ACCEPT\\] - \\[DENY\\] - \\[IGNORE\\].*";

    private String chatCleanerJoinNormal = "joined the lobby";
    private String chatCleanerJoinHalloween = "spooked into the lobby";
    private String chatCleanerJoinChristmas = "(?:sled|slid) into the lobby";
    private String chatCleanerMysteryBoxFind = "^.+ (?<player>\\w{1,16}) found (?:a|an) (?:.{5} Mystery Box!|.+ in a .+!)$";
    private String chatCleanerSoulWellFind = "^.+ has found .+ in the Soul Well!$";
    private String chatCleanerGameAnnouncement = "^\u27A4 A .+ game is (?:available to join|starting in .+ seconds)! CLICK HERE to join!$";
    private String chatCleanerBedwarsPartyAdvertisement = "(?<number>[1-7]/[2-8])";
    private String chatCleanerConnectionStatus = "^(?:Friend|F|Guild|G) > (?<player>\\w{1,16}) (?:joined|left)\\.$";
    private String chatCleanerMvpEmotes = "\u00a7r\u00a7(?:c\u2764|6\u272e|a\u2714|c\u2716|b\u2615|e\u279c|e\u00af\\\\_\\(\u30c4\\)_/\u00af|c\\(\u256F\u00B0\u25A1\u00B0\uFF09\u256F\u00a7r\u00a7f\uFE35\u00a7r\u00a77 \u253B\u2501\u253B|d\\( \uFF9F\u25E1\uFF9F\\)/|a1\u00a7r\u00a7e2\u00a7r\u00a7c3|b\u2609\u00a7r\u00a7e_\u00a7r\u00a7b\u2609|e\u270E\u00a7r\u00a76\\.\\.\\.|a\u221A\u00a7r\u00a7e\u00a7l\\(\u00a7r\u00a7a\u03C0\u00a7r\u00a7a\u00a7l\\+x\u00a7r\u00a7e\u00a7l\\)\u00a7r\u00a7a\u00a7l=\u00a7r\u00a7c\u00a7lL|e@\u00a7r\u00a7a'\u00a7r\u00a7e-\u00a7r\u00a7a'|6\\(\u00a7r\u00a7a0\u00a7r\u00a76\\.\u00a7r\u00a7ao\u00a7r\u00a7c\\?\u00a7r\u00a76\\)|b\u0F3C\u3064\u25D5_\u25D5\u0F3D\u3064|e\\(\u00a7r\u00a7b'\u00a7r\u00a7e-\u00a7r\u00a7b'\u00a7r\u00a7e\\)\u2283\u00a7r\u00a7c\u2501\u00a7r\u00a7d\u2606\uFF9F\\.\\*\uFF65\uFF61\uFF9F|e\u2694|a\u270C|c\u00a7lOOF|e\u00a7l<\\('O'\\)>)\u00a7r";
    public String chatCleanerHypeLimit = "  \u27A4 You have reached your Hype limit!";
    private String chatGiftBlocker = "They have gifted \\d+ ranks so far!";
    private String chatCleanerGrinchPresents = "(?:You found (?:an egg|a gift|a candy)! .\\d{1,3} total.|^\\W{0,3}\\w{0,}\\S{0,3}\\s{0,1}\\w{1,16} has reached \\d{2,3} (?:gifts|eggs|candy)!)";
    private String chatCleanerEarnedCoinsAndExp = "^(?:\\W\\d+ .* Experience.*|\\W\\d+ coins!.*|You earned \\d+ GEXP from playing.+!|.+ just earned .+ as a Guild Level Reward!)"; //.* at the end for modifiers
    public String chatCleanerReplayRecorded = "This game has been recorded. Click here to watch the Replay!";
    private String chatCleanerTip = "(?:You tipped \\d+ (?:player|players) in \\d+ (?:game|different games)!|You already tipped everyone that has boosters active, so there isn't anybody to be tipped right now!)";
    private String chatCleanerOnlineStatus = "REMINDER: Your Online Status is currently set to (?:Appear Offline|Busy|Away)";
    private String chatCleanerGameTips = "^(?:If you get disconnected use /rejoin to join back in the game\\.|You may use /mmreport <skin name> to chat report in this mode!|Teaming with the .+ is not allowed!|Teaming is not allowed.+|Cross Teaming / Teaming with other teams is not allowed!|Cross-teaming is not allowed! Report cross-teamers using /report.|Cages opened! FIGHT!|Queued! Use the bed to return to lobby!|Queued! Use the bed to cancel!|You can use /ic <message> to communicate with your fellow infected!||To leave .+, type /lobby)";
    private String chatCleanerStats = "Click to view the stats of your .* game!";

    private String connectedServerConnectMessage = "^(You are currently connected to server \\S+)|(Sending you to \\S+.{3}!)|(Sending to server \\S+.{3})|(Warping you to your SkyBlock island...)|(Warping...)|(Sending a visit request...)|(Finding player...)|(Request join for (?:Hub|Dungeon Hub) .{2,4} \\S+.{3})|(Found an in-progress .+ game! Teleporting you to \\S+.{3}...)$";

    private String achievementPattern = "a>> {3}Achievement Unlocked: (?<achievement>.+) {3}<<a";
    private String levelUpPattern = "You are now Hypixel Level (?<level>\\d+)!";
    private String guildPlayerJoinPattern = "^(?:\\[.*] )?(?<player>\\S{1,16}) joined the guild!$";

    private String chatRestylerGameJoinStyle = "^§r§e§r§(?<color>[\\da-f])(?:§k)?(?<player>\\w{1,16})§r§r§r§e has joined (?<amount>.+)!§r§e§r$";
    private String chatRestylerGameLeaveStyle = "^§r§e§r§(?<color>[\\da-f])(?:§k)?(?<player>\\w{1,16})§r§r§r§e has quit!§r§e§r$";
    private String chatRestylerGameStartCounterStyle = "^(?<title>(The game starts in|Cages open in:|You will respawn in|The Murderer gets their sword in|You get your sword in|The alpha infected will be chosen in|Kill contracts will be issued in|The Murderers get their swords in)) (?<time>\\d{1,3}) (?<unit>(seconds?!))$"; // TODO: translate "Cages open in:" to french (also translate to chatRestylerGameStartCounterOutputStyle)
    private String chatRestylerGameStartCounterOutputStyle = "^\u00a7e\u00a7l\\* \u00a7a(The game starts in|Cages open in:|You will respawn in|The Murderer gets their sword in|You get your sword in|The alpha infected will be chosen in|Kill contracts will be issued in|The Murderers get their swords in) \u00a7b\u00a7l\\d{1,3} \u00a7aseconds?!\u00a7r$";
    private String chatRestylerFormattedPaddingPattern = "\\(§r§b(\\d{1,2})§r§r§r§e/§r§b(\\d{1,3})§r§r§r§e\\)";
    private String chatRestylerPartyPattern = "^((?:\\u00a7r)?\\u00a7\\w)(Party )(\\u00a7\\w>)";
    private String chatRestylerGuildPattern = "^((?:\\u00a7r)?\\u00a7\\w)(Guild >)";
    private String chatRestylerFriendPattern = "^((?:\\u00a7r)?\\u00a7\\w)(Friend >)";
    private String chatRestylerOfficerPattern = "^((?:\\u00a7r)?\\u00a7\\w)(Officer >)";
    private String chatRestylerStatusPattern = "^(?<type>(?:§aFriend|§a§aF|§2Guild|§2§2G)) > (§r|§r§r){1,2}(?<player>§[\\da-f]\\w{1,16}) §r§e(?<status>(?:joined|left))\\.§r$";

    private String autoChatSwapperPartyStatus = "^(?:You have been kicked from the party by (?:\\[.+] )?\\w{1,16}|(?:\\[.+] )?\\w{1,16} has disbanded the party!|You left the party(?:\\[.+] )?\\w{0,16}|(?:\\[.+] )?\\w{1,100}The party was disbanded(?:\\[.+] )?\\w{1,100}.)$";
    private String autoChatSwapperPartyStatus2 = "^(?:You have joined (?:\\[.+] )?(?:.*)|Party Members(?:\\[.+] )?\\w{1,100}|(?:\\[.+] )?\\w{1,100} joined the(?:.*) party(?:.*))$";
    private String autoChatSwapperChannelSwap = "^You are now in the (?<channel>ALL|GUILD|OFFICER|PARTY) channel$";
    public String autoChatSwapperAlreadyInChannel = "You're already in this channel!";

    private String whiteChatNonMessage = "(?<prefix>.+)§7: (?<message>.*)";
    private String privateMessageWhiteChat = "^(?<type>§dTo|§dFrom) (?<prefix>.+): §r(?<message>§7.*)(?:§r)?$";

    public String limboLimiterSpawned = "You were spawned in Limbo.";
    public String limboLimiterAfk = "You are AFK. Move around to return from AFK.";

    private String silentRemovalLeaveMessage = "(?:Friend|Guild) > (?<player>\\w{1,16}) left\\.";

    public String noSpectatorCommands = "You are not allowed to use commands as a spectator!";

    public String cannotShoutBeforeSkywars = "You can't shout until the game has started!";
    public String cannotShoutBeforeGame = "You can't use /shout before the game has started.";
    public String cannotShoutAfterGame = "You can't use /shout after the game has finished.";

    private String hypixelLevelUp = "You are now Hypixel Level (?<level>\\d+)!";

    /**
     * Cached values which use the messages read from the config file.
     * Particularly Regexes.
     */
    public Pattern autoQueuePrefixGlobalRegex;

    public Pattern autoFriendPatternRegex;

    public Set<Pattern> chatCleanerJoinMessageTypes;

    public Pattern chatCleanerMysteryBoxFindRegex;
    public Pattern chatCleanerSoulWellFindRegex;
    public Pattern chatCleanerGameAnnouncementRegex;
    public Pattern chatCleanerBedwarsPartyAdvertisementRegex;
    public Pattern chatCleanerConnectionStatusRegex;
    public Pattern chatCleanerMvpEmotesRegex;
    public Pattern chatGiftBlockerRegex;
    public Pattern chatCleanerGrinchPresentsRegex;
    public Pattern chatCleanerEarnedCoinsAndExpRegex;
    public Pattern chatCleanerReplayRecordedRegex;
    public Pattern chatCleanerTipRegex;
    public Pattern chatCleanerOnlineStatusRegex;
    public Pattern chatCleanerGameTipsRegex;
    public Pattern chatCleanerStatsRegex;

    public Pattern connectedServerConnectMessageRegex;

    public Pattern achievementRegex;
    public Pattern levelUpRegex;
    public Pattern guildPlayerJoinRegex;

    public Pattern chatRestylerGameJoinStyleRegex;
    public Pattern chatRestylerGameLeaveStyleRegex;
    public Pattern chatRestylerGameStartCounterStyleRegex;
    public Pattern chatRestylerGameStartCounterOutputStyleRegex;
    public Pattern chatRestylerFormattedPaddingPatternRegex;
    public Pattern chatRestylerPartyPatternRegex;
    public Pattern chatRestylerGuildPatternRegex;
    public Pattern chatRestylerFriendPatternRegex;
    public Pattern chatRestylerOfficerPatternRegex;
    public Pattern chatRestylerStatusPatternRegex;

    public Pattern autoChatSwapperPartyStatusRegex;
    public Pattern autoChatSwapperPartyStatusRegex2;
    public Pattern autoChatSwapperChannelSwapRegex;

    public Pattern whiteChatNonMessageRegex;
    public Pattern privateMessageWhiteChatRegex;
    public Pattern silentRemovalLeaveMessageRegex;

    public Pattern hypixelLevelUpRegex;

    /**
     * Compiles all the required patterns and caches them for later use.
     */
    public void initialize() {
        if (!hasInitialized) {
            hasInitialized = true;

            autoQueuePrefixGlobalRegex = Pattern.compile(autoQueuePrefixGlobal);

            autoFriendPatternRegex = Pattern.compile(autoFriendPattern);

            chatCleanerJoinMessageTypes = Sets.newHashSet(Pattern.compile(chatCleanerJoinNormal), Pattern.compile(chatCleanerJoinHalloween), Pattern.compile(chatCleanerJoinChristmas));

            chatCleanerMysteryBoxFindRegex = Pattern.compile(chatCleanerMysteryBoxFind);
            chatCleanerSoulWellFindRegex = Pattern.compile(chatCleanerSoulWellFind);
            chatCleanerGameAnnouncementRegex = Pattern.compile(chatCleanerGameAnnouncement);
            chatCleanerBedwarsPartyAdvertisementRegex = Pattern.compile(chatCleanerBedwarsPartyAdvertisement);
            chatCleanerConnectionStatusRegex = Pattern.compile(chatCleanerConnectionStatus);
            chatCleanerMvpEmotesRegex = Pattern.compile(chatCleanerMvpEmotes);
            chatGiftBlockerRegex = Pattern.compile(chatGiftBlocker);
            chatCleanerGrinchPresentsRegex = Pattern.compile(chatCleanerGrinchPresents);
            chatCleanerEarnedCoinsAndExpRegex = Pattern.compile(chatCleanerEarnedCoinsAndExp);
            chatCleanerReplayRecordedRegex = Pattern.compile(chatCleanerReplayRecorded);
            chatCleanerTipRegex = Pattern.compile(chatCleanerTip);
            chatCleanerOnlineStatusRegex = Pattern.compile(chatCleanerOnlineStatus);
            chatCleanerGameTipsRegex = Pattern.compile(chatCleanerGameTips);
            chatCleanerStatsRegex = Pattern.compile(chatCleanerStats);

            connectedServerConnectMessageRegex = Pattern.compile(connectedServerConnectMessage);

            achievementRegex = Pattern.compile(achievementPattern);
            levelUpRegex = Pattern.compile(levelUpPattern);
            guildPlayerJoinRegex = Pattern.compile(guildPlayerJoinPattern);

            chatRestylerGameJoinStyleRegex = Pattern.compile(chatRestylerGameJoinStyle);
            chatRestylerGameLeaveStyleRegex = Pattern.compile(chatRestylerGameLeaveStyle);
            chatRestylerGameStartCounterStyleRegex = Pattern.compile(chatRestylerGameStartCounterStyle);
            chatRestylerGameStartCounterOutputStyleRegex = Pattern.compile(chatRestylerGameStartCounterOutputStyle);
            chatRestylerFormattedPaddingPatternRegex = Pattern.compile(chatRestylerFormattedPaddingPattern);
            chatRestylerPartyPatternRegex = Pattern.compile(chatRestylerPartyPattern);
            chatRestylerGuildPatternRegex = Pattern.compile(chatRestylerGuildPattern);
            chatRestylerFriendPatternRegex = Pattern.compile(chatRestylerFriendPattern);
            chatRestylerOfficerPatternRegex = Pattern.compile(chatRestylerOfficerPattern);
            chatRestylerStatusPatternRegex = Pattern.compile(chatRestylerStatusPattern);

            autoChatSwapperPartyStatusRegex = Pattern.compile(autoChatSwapperPartyStatus);
            autoChatSwapperPartyStatusRegex2 = Pattern.compile(autoChatSwapperPartyStatus2);
            autoChatSwapperChannelSwapRegex = Pattern.compile(autoChatSwapperChannelSwap);

            whiteChatNonMessageRegex = Pattern.compile(whiteChatNonMessage);
            privateMessageWhiteChatRegex = Pattern.compile(privateMessageWhiteChat);
            silentRemovalLeaveMessageRegex = Pattern.compile(silentRemovalLeaveMessage);

            hypixelLevelUpRegex = Pattern.compile(hypixelLevelUp);
        }

    }
}
