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

package org.polyfrost.hytils.handlers.language;

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
    private String autoQueuePrefixGlobal = "^(?:You died! .+|YOU DIED! .+|You have been eliminated!|You won! .+|YOU WON! .+)$";

    private String autoFriendPattern = "Friend request from (?<name>.+)\\[ACCEPT] - \\[DENY] - \\[IGNORE].*";
    private String autoAfkReplyPattern = "^From (\\[.+?] )?(.+?): .+$";

    private String chatCleanerKarmaMessages = "^\\+(?<karma>\\d)+ Karma!$";
    private String chatCleanerJoin = "(?:sled into|slid into|joined|spooked into) the lobby";
    private String chatCleanerTicketAnnouncer = "^(?<player>(?!You )\\w{1,16} )has found an? .+$";
    private String chatCleanerSoulWellFind = "^.+ has found .+ in the Soul Well!$";
    private String chatCleanerGameAnnouncement = "^\u27a4 A .+ game is (?:available to join|starting in .+ seconds)! CLICK HERE to join!$";
    private String chatCleanerBedwarsPartyAdvertisement = "(?!.+: )(([1-8]/[1-8]|[1-8]v[1-8]|[2-8]s)|(any|rbw|ranked))";
    private String chatCleanerConnectionStatus = "^(?:Friend|F|Guild|G) > (?<player>\\w{1,16}) (?:joined|left)\\.$";
    private String chatCleanerMvpEmotes = "\u00a7r\u00a7(?:c\u2764|6\u272e|a\u2714|c\u2716|b\u2615|e\u279c|e\u00af\\\\_\\(\u30c4\\)_/\u00af|c\\(\u256f\u00b0\u25a1\u00b0\uff09\u256f\u00a7r\u00a7f\ufe35\u00a7r\u00a77 \u253b\u2501\u253b|d\\( \uff9f\u25e1\uff9f\\)/|a1\u00a7r\u00a7e2\u00a7r\u00a7c3|b\u2609\u00a7r\u00a7e_\u00a7r\u00a7b\u2609|e\u270e\u00a7r\u00a76\\.\\.\\.|a\u221a\u00a7r\u00a7e\u00a7l\\(\u00a7r\u00a7a\u03c0\u00a7r\u00a7a\u00a7l\\+x\u00a7r\u00a7e\u00a7l\\)\u00a7r\u00a7a\u00a7l=\u00a7r\u00a7c\u00a7lL|e@\u00a7r\u00a7a'\u00a7r\u00a7e-\u00a7r\u00a7a'|6\\(\u00a7r\u00a7a0\u00a7r\u00a76\\.\u00a7r\u00a7ao\u00a7r\u00a7c\\?\u00a7r\u00a76\\)|b\u0f3c\u3064\u25d5_\u25d5\u0f3d\u3064|e\\(\u00a7r\u00a7b'\u00a7r\u00a7e-\u00a7r\u00a7b'\u00a7r\u00a7e\\)\u2283\u00a7r\u00a7c\u2501\u00a7r\u00a7d\u2606\uff9f\\.\\*\uff65\uff61\uff9f|e\u2694|a\u270c|c\u00a7lOOF|e\u00a7l<\\('O'\\)>)\u00a7r";
    public String chatCleanerHypeLimit = "  \u27a4 You have reached your Hype limit!";
    private String chatGiftBlocker = "They have gifted \\d+ (?:rank|ranks) so far!";
    private String chatCommonAdvertisements = "(?!.+: )(/?(((party join|join party)|p join|(guild join)|(join guild)|g join) \\w{1,16})|/?(party me|visit me|duel me|my ah|my smp)|(twitch.tv)|(youtube.com|youtu.be)|(/(visit|ah) \\w{1,16}|(visit /\\w{1,16})|(/gift)|(gilde)|(lowballing|lowbaling|lowvaling|lowvaluing|lowballer)))";
    private String chatRankBegging = "(?!.+: )([^\\[](vip|mvp|mpv|vpi)|(please|pls|plz|rank ?up|rank ?upgrade)|(buy|upgrade|gift|give) (rank|me)|(gifting|gifters)|( beg |begging|beggers))";
    private String chatCleanerGrinchPresents = "(?:You found (?:an egg|a gift|a candy)! .\\d{1,3} total.|^\\W{0,3}\\w{0,}\\S{0,3}\\s{0,1}\\w{1,16} has reached \\d{2,3} (?:gifts|eggs|candy)!)";
    private String connectedServerConnectMessage = "^(You are currently connected to server \\S+)|(Sending you to \\S+!)|(Sending you to \\S+)|(Sending to server \\S+)|(SERVER FOUND! Sending to \\S+!)|(Warping you to your SkyBlock island\\.{3})|(Warping\\.{3})|(Sending a visit request\\.{3})|(Finding player\\.{3})|(Request join for (?:Hub|Dungeon Hub) (?:.{2,4} \\S+|\\S+))|(Found an in-progress .+ game! Teleporting you to \\S+)|(Returning you to the lobby!)$";
    private String chatCleanerEarnedCoinsAndExp = "^(?:\\W\\d+ .* Experience.*|\\W\\d+ Soul.*|\\W\\d+ coins.*|.*\\W\\d+ Event EXP.*|You earned \\d+ GEXP from playing.+!|.+ just earned .+ as a Guild Level Reward!)"; //.* at the end for modifiers
    private String chatCleanerReplayRecorded = "This game has been recorded\\. Click here to watch the Replay!";
    private String chatCleanerTip = "(?:You (?:tipped|\\(anonymously\\) tipped) (\\d+ (?:player|players)(?: in \\d+ (?:game|different games)!)|\\w{1,16} in .+!|\\d+ (?:player|players)!)|You were tipped by \\d+ (?:player|players) in the last \\S+!|You already tipped everyone that has boosters active, so there isn't anybody to be tipped right now!|You've already tipped that person today in .+! Try another user!|You've already tipped someone in the past hour in .+! Wait a bit and try again!)";
    private String chatCleanerOnlineStatus = "REMINDER: Your Online Status is currently set to (?:Appear Offline|Busy|Away)";
    private String chatCleanerGameTips = "^(?:If you get disconnected use /rejoin to join back in the game\\.|You may use /mmreport <skin name> to chat report in this mode!|Teaming with the .+ is not allowed!|Teaming is not allowed.+|Cross Teaming / Teaming with other teams is not allowed!|Cross-teaming is not allowed! Report cross-teamers using /report\\.|Cages opened! FIGHT!|Queued! Use the bed to return to lobby!|Queued! Use the bed to cancel!|You can use /ic <message> to communicate with your fellow infected!|To leave .+, type /lobby|Consider sharing some of your resources with your team mates by clicking the Banker NPC at your base\\.|You didn't pick up any more \\S+ because you have too much on you!|As a Spectator, you can talk in chat with fellow Spectators\\.|Contents of .+ Ender Chest have been dropped into their fountain\\.|Alive players cannot see dead players' chat\\.|The game has started!|You have 15s to spread out before it starts!|You will respawn next round!|Jumping before dropping can sometimes give you an advantage!|You can skip the level if you fail too many times on easy or medium difficulties!|DROP!|Reset location!|Dropper is currently in the Prototype Lobby, please report bugs at https:\\/\\/hypixel\\.net\\/bugs!)";
    private String chatCleanerStats = "Click to view the stats of your .* game!";
    private String chatCleanerLobbyFishingAnnouncement = "(?<rank>\\[\\S+] )?(?<player>(?!You )\\w{1,16} )caught .+";
    private String chatCleanerHotPotato = "\\w{1,16} burnt to a crisp due to a hot potato!";
    private String chatCleanerDuelsNoStatsChange = "Your stats did not change because you /duel'ed your opponent!|Your stats did not change because you dueled someone in your party!|No stats will be affected in this round!";
    public String chatCleanerBridgeOwnGoalDeath = "You just jumped through your own goal, enjoy the void death! :)";
    public String chatCleanerCurseOfSpam = "KALI HAS STRIKEN YOU WITH THE CURSE OF SPAM";
    public String chatCleanerDuelsBlockTrail = "Your block trail aura is disabled in this mode!";
    public String chatCleanerSkyblockWelcome = "Welcome to Hypixel SkyBlock!";
    public String chatDiscordSafetyWarning = "Please be mindful of Discord links in chat as they may pose a security risk";

    private String achievementPattern = "a>> {3}Achievement Unlocked: (?<achievement>.+) {3}<<a";
    private String levelUpPattern = "You are now Hypixel Level (?<level>\\d+)!";
    private String guildPlayerJoinPattern = "^(?:\\[.*] )?(?<player>\\S{1,16}) joined the guild!$";

    private String chatRestylerGameJoinStyle = "^\u00a7r\u00a7e\u00a7r\u00a7(?<color>[\\da-f])(?:\u00a7k)?(?<player>\\w{1,16})\u00a7r\u00a7r\u00a7r\u00a7e has joined (?<amount>.+)!\u00a7r\u00a7e\u00a7r$";
    private String chatRestylerGameLeaveStyle = "^\u00a7r\u00a7e\u00a7r\u00a7(?<color>[\\da-f])(?:\u00a7k)?(?<player>\\w{1,16})\u00a7r\u00a7r\u00a7r\u00a7e has quit!\u00a7r\u00a7e\u00a7r$";
    private String chatRestylerGameStartCounterStyle = "^(?<title>(The game starts in|Cages open in:|You will respawn in|The Murderer gets their sword in|You get your sword in|The alpha infected will be chosen in|Kill contracts will be issued in|The Murderers get their swords in|You can start shooting in|The door opens in)) (?<time>\\d{1,3}) (?<unit>(seconds?!))(?: .\\d+.|)$";
    private String chatRestylerGameStartCounterOutputStyle = "^\u00a7e\u00a7l\\* \u00a7a(The game starts in|Cages open in:|You will respawn in|The Murderer gets their sword in|You get your sword in|The alpha infected will be chosen in|Kill contracts will be issued in|The Murderers get their swords in|You can start shooting in|The door opens in) \u00a7b\u00a7l\\d{1,3} \u00a7aseconds?!\u00a7r$";
    private String chatRestylerFormattedPaddingPattern = "\\(\u00a7r\u00a7b(\\d{1,2})\u00a7r\u00a7r\u00a7r\u00a7e/\u00a7r\u00a7b(\\d{1,3})\u00a7r\u00a7r\u00a7r\u00a7e\\)";
    private String chatRestylerPartyPattern = "^((?:\u00a7r)?\u00a7\\w)(Party )(\\u00a7\\w>)";
    private String chatRestylerGuildPattern = "^((?:\u00a7r)?\u00a7\\w)(Guild >)";
    private String chatRestylerFriendPattern = "^((?:\u00a7r)?\u00a7\\w)(Friend >)";
    private String chatRestylerOfficerPattern = "^((?:\u00a7r)?\u00a7\\w)(Officer >)";
    private String chatRestylerStatusPattern = "^(?<type>(?:\u00a7aFriend|\u00a7a\u00a7aF|\u00a72Guild|\u00a72\u00a72G)) > (\u00a7r|\u00a7r\u00a7r){1,2}(?<player>\u00a7[\\da-f]\\w{1,16}) \u00a7r\u00a7e(?<status>(?:joined|left))\\.\u00a7r$";

    private String autoChatSwapperPartyStatus = "^(?:You have been kicked from the party by (?:\\[.+] )?\\w{1,16}|(?:\\[.+] )?\\w{1,16} has disbanded the party!|You left the party.)$";
    private String autoChatSwapperPartyStatus2 = "^(?:You have joined (?:\\[.+] )?(?:.*)|Party Members(?:\\[.+] )?\\w{1,100}|(?:\\[.+] )?\\w{1,100} joined the(?:.*) party(?:.*))$";
    private String autoChatSwapperChannelSwap = "^You are now in the (?<channel>ALL|GUILD|OFFICER) channel$";
    public String autoChatSwapperAlreadyInChannel = "You're already in this channel!";

    private String whiteChatNonMessage = "(?<prefix>.+)\u00a77: (?<message>.*)";
    private String privateMessageWhiteChat = "^(?<type>\u00a7dTo|\u00a7dFrom) (?<prefix>.+): \u00a7r(?<message>\u00a77.*)(?:\u00a7r)?$";

    private String silentRemovalLeaveMessage = "(?:Friend|Guild) > (?<player>\\w{1,16}) left\\.";

    public String noSpectatorCommands = "You are not allowed to use commands as a spectator!";

    public String cannotShoutBeforeSkywars = "You can't shout until the game has started!";
    public String cannotShoutBeforeGame = "You can't use /shout before the game has started.";
    public String cannotShoutAfterGame = "You can't use /shout after the game has finished.";

    private String hypixelLevelUp = "You are now Hypixel Level (?<level>\\d+)!";

    private String casualGameEnd = "^(?:MINOR EVENT! .+ in .+ ended|DRAGON EGG OVER! Earned [\\d,]+XP [\\d,]g clicking the egg \\d+ times|GIANT CAKE! Event ended! Cake's gone!|PIT EVENT ENDED: .+ \\[INFO\\])$";
    private String cancelGgMessages = "^(?:.* )?(?:\\[.+] )?\\w{1,16}(?: .+)?: (?:❤|gg|GG|gf|Good Game|Good Fight|Good Round! :D|Have a good day!|<3|AutoGG By Sk1er!|AutoGG By Hytils Reborn!|gf|Good Fight|Good Round|:D|Well Played!|wp)$";
    private String cancelGlMessages = "(?!.+: )(gl|glhf|good luck|have a good game|autogl by sk1er)";

    public String autoChatReportConfirm = "Please type /report confirm to log your report for staff review.";
    public String autoPartyWarpConfirm = "Some players are still in-game, run the command again to confirm warp!";

    public String tabFooterAdvertisement = "\u00a7aRanks, Boosters & MORE! \u00a7r\u00a7c\u00a7lSTORE.HYPIXEL.NET";
    public String tabHeaderAdvertisement = "\u00a7bYou are playing on \u00a7r\u00a7e\u00a7lMC.HYPIXEL.NET";

    private String gameBossbarAdvertisement = "\u00a7e\u00a7lPlaying \u00a7f\u00a7l.+ \u00a7e\u00a7lon \u00a7\\S\u00a7lMC\\.HYPIXEL\\.NET\u00a7r";

    /**
     * Cached values which use the messages read from the config file.
     * Particularly Regexes.
     */
    public Pattern autoQueuePrefixGlobalRegex;

    public Pattern autoFriendPatternRegex;
    public Pattern autoAfkReplyPatternRegex;

    public Pattern chatCleanerKarmaMessagesRegex;
    public Pattern chatCleanerJoinRegex;
    public Pattern chatCleanerTicketAnnouncerRegex;
    public Pattern chatCleanerSoulWellFindRegex;
    public Pattern chatCleanerGameAnnouncementRegex;
    public Pattern chatCleanerBedwarsPartyAdvertisementRegex;
    public Pattern chatCleanerConnectionStatusRegex;
    public Pattern chatCleanerMvpEmotesRegex;
    public Pattern chatGiftBlockerRegex;
    public Pattern chatCommonAdvertisementsRegex;
    public Pattern chatRankBeggingRegex;
    public Pattern chatCleanerGrinchPresentsRegex;
    public Pattern connectedServerConnectMessageRegex;
    public Pattern chatCleanerEarnedCoinsAndExpRegex;
    public Pattern chatCleanerReplayRecordedRegex;
    public Pattern chatCleanerTipRegex;
    public Pattern chatCleanerOnlineStatusRegex;
    public Pattern chatCleanerGameTipsRegex;
    public Pattern chatCleanerStatsRegex;
    public Pattern chatCleanerLobbyFishingAnnouncementRegex;
    public Pattern chatCleanerHotPotatoRegex;
    public Pattern chatCleanerDuelsNoStatsChangeRegex;

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

    public Pattern casualGameEndRegex;
    public Pattern cancelGgMessagesRegex;
    public Pattern cancelGlMessagesRegex;

    public Pattern gameBossbarAdvertisementRegex;

    /**
     * Compiles all the required patterns and caches them for later use.
     */
    public void initialize() {
        if (!hasInitialized) {
            hasInitialized = true;

            autoQueuePrefixGlobalRegex = Pattern.compile(autoQueuePrefixGlobal);

            autoFriendPatternRegex = Pattern.compile(autoFriendPattern);
            autoAfkReplyPatternRegex = Pattern.compile(autoAfkReplyPattern);

            chatCleanerKarmaMessagesRegex = Pattern.compile(chatCleanerKarmaMessages);
            chatCleanerJoinRegex = Pattern.compile(chatCleanerJoin);
            chatCleanerTicketAnnouncerRegex = Pattern.compile(chatCleanerTicketAnnouncer);
            chatCleanerSoulWellFindRegex = Pattern.compile(chatCleanerSoulWellFind);
            chatCleanerGameAnnouncementRegex = Pattern.compile(chatCleanerGameAnnouncement);
            chatCleanerBedwarsPartyAdvertisementRegex = Pattern.compile(chatCleanerBedwarsPartyAdvertisement);
            chatCleanerConnectionStatusRegex = Pattern.compile(chatCleanerConnectionStatus);
            chatCleanerMvpEmotesRegex = Pattern.compile(chatCleanerMvpEmotes);
            chatGiftBlockerRegex = Pattern.compile(chatGiftBlocker);
            chatCommonAdvertisementsRegex = Pattern.compile(chatCommonAdvertisements, Pattern.CASE_INSENSITIVE);
            chatRankBeggingRegex = Pattern.compile(chatRankBegging, Pattern.CASE_INSENSITIVE);
            chatCleanerGrinchPresentsRegex = Pattern.compile(chatCleanerGrinchPresents);
            connectedServerConnectMessageRegex = Pattern.compile(connectedServerConnectMessage);
            chatCleanerEarnedCoinsAndExpRegex = Pattern.compile(chatCleanerEarnedCoinsAndExp);
            chatCleanerReplayRecordedRegex = Pattern.compile(chatCleanerReplayRecorded);
            chatCleanerTipRegex = Pattern.compile(chatCleanerTip);
            chatCleanerOnlineStatusRegex = Pattern.compile(chatCleanerOnlineStatus);
            chatCleanerGameTipsRegex = Pattern.compile(chatCleanerGameTips);
            chatCleanerStatsRegex = Pattern.compile(chatCleanerStats);
            chatCleanerLobbyFishingAnnouncementRegex = Pattern.compile(chatCleanerLobbyFishingAnnouncement);
            chatCleanerHotPotatoRegex = Pattern.compile(chatCleanerHotPotato);
            chatCleanerDuelsNoStatsChangeRegex = Pattern.compile(chatCleanerDuelsNoStatsChange);

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

            casualGameEndRegex = Pattern.compile(casualGameEnd);
            cancelGgMessagesRegex = Pattern.compile(cancelGgMessages);
            cancelGlMessagesRegex = Pattern.compile(cancelGlMessages, Pattern.CASE_INSENSITIVE);

            gameBossbarAdvertisementRegex = Pattern.compile(gameBossbarAdvertisement);
        }

    }
}
