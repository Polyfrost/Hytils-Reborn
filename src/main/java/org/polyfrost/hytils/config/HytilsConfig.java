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

package org.polyfrost.hytils.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator;
import cc.polyfrost.oneconfig.utils.Notifications;
import club.sk1er.lobbysounds.config.Sounds;
import club.sk1er.mods.autogg.AutoGG;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.handlers.chat.modules.modifiers.GameStartCompactor;
import org.polyfrost.hytils.util.DarkColorUtils;

import java.awt.Color;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class HytilsConfig extends Config {

    // Automatic

    @Switch(
        name = "Auto Start",
        description = "Join Hypixel immediately once the client has loaded to the main menu.",
        category = "General", subcategory = "Automatic", size = 2
    )
    public static boolean autoStart;

    @Switch(
        name = "Auto-Complete Play Commands",
        description = "Allows tab completion of /play commands.",
        category = "General", subcategory = "Automatic"
    )
    public static boolean autocompletePlayCommands = true;

    @Checkbox(
        name = "Limbo Play Helper",
        description = "When a /play or /requeue command is run in Limbo, this runs /l first and then the command.",
        category = "General", subcategory = "Automatic"
    )
    public static boolean limboPlayCommandHelper = true;

    @Info(
        text = "Auto Queue will require you to interact with the game in a way to prevent abuse.",
        category = "General", subcategory = "Automatic",
        type = InfoType.WARNING, size = 2
    )
    private static boolean autoQueueInfo;

    @Switch(
        name = "Auto Queue",
        description = "Automatically queues for another game once you win or die.",
        category = "General", subcategory = "Automatic"
    )
    public static boolean autoQueue;

    @Slider(
        name = "Auto Queue Delay",
        description = "Delays the execution of Auto Queue.\n§eMeasured in seconds.",
        category = "General", subcategory = "Automatic",
        min = 0, max = 10
    )
    public static int autoQueueDelay;

    @Switch(
        name = "Automatically Check GEXP",
        description = "Automatically check your GEXP after you win a Hypixel game.",
        category = "General", subcategory = "Automatic"
    )
    public static boolean autoGetGEXP;

    @DualOption(
        name = "GEXP Mode",
        category = "General", subcategory = "Automatic",
        description = "Choose which GEXP to get.",
        left = "Daily",
        right = "Weekly"
    )
    public static boolean gexpMode = false;

    @Switch(
        name = "Automatically Check Winstreak",
        description = "Automatically check your winstreak after you win a Hypixel game.",
        category = "General", subcategory = "Automatic"
    )
    public static boolean autoGetWinstreak;

    @Switch(
        name = "Notify Mining Fatigue",
        description = "Send a notification when you get mining fatigue.",
        category = "General", subcategory = "Potion Effects"
    )
    public static boolean notifyMiningFatigue = true;

    @Checkbox(
        name = "Disable Mining Fatigue Notification in SkyBlock",
        description = "Disable the mining fatigue notification in SkyBlock.",
        category = "General", subcategory = "Potion Effects"
    )
    public static boolean disableNotifyMiningFatigueSkyblock = true;

    // Chat

    @Switch(
        name = "Auto GG",
        description = "Send a \"gg\" message at the end of a game.",
        category = "Chat", subcategory = "Automatic"
    )
    public static boolean autoGG = true;

    @Switch(
        name = "Auto GG Second Message",
        description = "Enable a secondary message to send after your first GG.",
        category = "Chat", subcategory = "Automatic"
    )
    public static boolean autoGGSecondMessage;

    @Switch(
        name = "Casual Auto GG",
        description = "Send a \"gg\" message at the end of minigames/events that don't give out Karma, such as SkyBlock and The Pit events.",
        category = "Chat", subcategory = "Automatic"
    )
    public static boolean casualAutoGG = true;

    @Switch(
        name = "Anti GG",
        description = "Remove GG messages from chat.",
        category = "Chat", subcategory = "Automatic"
    )
    public static boolean antiGG;

    @Text(
        name = "Auto GG First Message",
        description = "Choose what message is said on game completion.",
        category = "Chat", subcategory = "Automatic"
    )
    public static String ggMessage = "gg";

    @Slider(
        name = "Auto GG First Phrase Delay",
        description = "Delay after the game ends to say the first message in seconds.",
        category = "Chat", subcategory = "Automatic",
        min = 0, max = 5
    )
    public static float autoGGFirstPhraseDelay = 1;

    @Text(
        name = "Auto GG Second Message",
        description = "Send a secondary message sent after the first GG message.",
        category = "Chat", subcategory = "Automatic"
    )
    public static String ggMessage2 = "Have a good day!";

    @Slider(
        name = "Auto GG Second Phrase Delay",
        description = "Delay after the game ends to say the second message in seconds.",
        category = "Chat", subcategory = "Automatic",
        min = 0, max = 5
    )
    public static float autoGGSecondPhraseDelay = 1;

    @Switch(
        name = "Auto GL",
        description = "Send a message 5 seconds before a Hypixel game starts.",
        category = "Chat", subcategory = "Automatic"
    )
    public static boolean autoGL;

    @Dropdown(
        name = "Auto GL Phrase",
        description = "Choose what message is said.",
        category = "Chat", subcategory = "Automatic",
        options = {"glhf", "Good Luck", "GL", "Have a good game!", "gl", "Good luck!"}
    )
    public static int glPhrase = 0;

    @Switch(
        name = "Anti GL",
        description = "Remove all GL messages from chat.",
        category = "Chat", subcategory = "Automatic"
    )
    public static boolean antiGL;

    @Switch(
        name = "Auto Friend",
        description = "Automatically accept friend requests.",
        category = "Chat", subcategory = "Automatic"
    )
    public static boolean autoFriend;

    @Switch(
        name = "Auto Chat Report Confirm",
        description = "Automatically confirms chat reports.",
        category = "Chat", subcategory = "Automatic"
    )
    public static boolean autoChatReportConfirm;

    @Switch(
        name = "Auto Party Warp Confirm",
        description = "Automatically confirms party warps.",
        category = "Chat", subcategory = "Automatic"
    )
    public static boolean autoPartyWarpConfirm;

    @Switch(
        name = "Auto Reply When AFK",
        description = "Automatically sends a reply to anyone who PMs you when you are AFK in Limbo.",
        category = "Chat", subcategory = "Automatic"
    )
    public static boolean autoReplyAfk;

    @Switch(
        name = "Game Status Restyle",
        description = "Replace common game status messages with a new style.\n§eExamples:\n§a§l+ §bSteve §e(§b1§e/§b12§e)\n§c§l- §bSteve§r\n§e§l* §aGame starts in §b§l5 §aseconds.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean gameStatusRestyle = true;

    @Switch(
        name = "Player Count Before Player Name",
        description = "Put the player count before the player name in game join/leave messages.\n§eExample: §a§l+ §e(§b1§e/§b12§e) §bSteve",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountBeforePlayerName = true;

    @Switch(
        name = "Player Count on Player Leave",
        description = "Include the player count when players leave.\n§eExample: §c§l- §bSteve §r§e(§b1§e/§b12§e)§r",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountOnPlayerLeave = true;

    @Switch(
        name = "Player Count Padding",
        description = "Place zeros at the beginning of the player count to align with the max player count.\n§eExample: §a§l+ §bSteve §e(§b001§e/§b100§e)",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean padPlayerCount = true;

    @Switch(
        name = "Trim Line Separators",
        description = "Prevent separators from overflowing onto the next chat line.",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean lineBreakerTrim = true;

    @Switch(
        name = "Clean Line Separators",
        description = "Change all line separator to become smoother.",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean cleanLineSeparator = true;

    @Switch(
        name = "White Chat",
        description = "Make nons' chat messages appear as the normal chat message color.",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean whiteChat;

    @Switch(
        name = "White Private Messages",
        description = "Make private messages appear as the normal chat message color.",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean whitePrivateMessages;

    @Switch(
        name = "Colored Friend/Guild Statuses",
        description = "Colors the join/leave status of friends and guild members.",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean coloredStatuses = true;

    @Switch(
        name = "Cleaner Game Counter",
        description = "Compacts counting announcements.\n§eExample: The game starts in 20 seconds!",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean cleanerGameStartAnnouncements = true;

    @Switch(
        name = "Short Channel Names",
        description = "Abbreviate chat channel names.\n§eExample: §2Guild §e-> §2G§e, §9Party §e-> §9P§e, §aFriend §e-> §aF",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean shortChannelNames;

    @Switch(
        name = "Short Private Message Channel Names",
        description = "Abbreviate private message channel names.\n§eExample: §dTo and §dFrom §e-> PM",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean pmShortChannelNames;

    @Switch(
        name = "Party Chat Swapper",
        description = "Automatically change to and out of a party channel when joining/leaving a party.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean chatSwapper;

    @Dropdown(
        name = "Chat Swapper Channel",
        description = "The channel to return to when leaving a party.",
        category = "Chat", subcategory = "Parties",
        options = {"ALL", "GUILD", "OFFICER"}
    )
    public static int chatSwapperReturnChannel;

    @Switch(
        name = "Swap Chatting Tab With Chat Swapper",
        description = " Automatically switch your Chatting chat tab when chat swapper swaps your chat channel.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean chattingIntegration;

    @Checkbox(
        name = "Remove All Chat Message",
        description = "Hide the \"You are now in the ALL channel\" message when auto-switching.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean hideAllChatMessage;

    @Switch(
        name = "Notify When Kicked From Game",
        description = "Notify in party chat when you are kicked from the game due to a connection issue.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean notifyWhenKick;

    @Checkbox(
        name = "Put Notify Message In Capital Letters",
        description = "Put the message in capital messages instead of proper formatting.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean putInCaps;

    @Switch(
        name = "Broadcast Achievements",
        description = "Announce in Guild chat when you get an achievement.",
        category = "Chat", subcategory = "Guild"
    )
    public static boolean broadcastAchievements;

    @Switch(
        name = "Broadcast Levelup",
        description = "Announce in Guild chat when you level up.",
        category = "Chat", subcategory = "Guild"
    )
    public static boolean broadcastLevelup;
    @Switch(
        name = "Guild Welcome Message",
        description = "Send a friendly welcome message when a player joins your guild.\n§eExample: §fWelcome to the guild Steve!",
        category = "Chat", subcategory = "Guild"
    )
    public static boolean guildWelcomeMessage;

    @Switch(
        name = "Thank Watchdog",
        description = "Compliment Watchdog when someone is banned, or a Watchdog announcement is sent.\n§eExample: §fThanks Watchdog!",
        category = "Chat", subcategory = "Watchdog"
    )
    public static boolean thankWatchdog;

    @Switch(
        name = "Non Speech Cooldown",
        description = "Show the amount of time remaining until you can speak if you are a non.\n§eExample: §eYour freedom of speech is on cooldown. Please wait 3 more seconds.",
        category = "Chat", subcategory = "Cooldown"
    )
    public static boolean preventNonCooldown;

    @Switch(
        name = "Shout Cooldown",
        description = "Show the amount of time remaining until /shout can be reused.\n§eExample: §eShout command is on cooldown. Please wait 30 more seconds.",
        category = "Chat", subcategory = "Cooldown"
    )
    public static boolean preventShoutingOnCooldown = true;

    @Switch(
        name = "Remove Karma Messages",
        description = "Remove Karma messages from the chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hideKarmaMessages;

    @Switch(
        name = "Hide Locraw Messages",
        description = "Hide locraw messages in chat (e.g {\"server\": \"something\"}).",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hideLocraw = true;

    @Switch(
        name = "Remove Lobby Statuses",
        description = "Remove lobby join messages from chat.\n§eExample: §b[MVP§c+§b] Steve §6joined the lobby!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean lobbyStatus;

    @Switch(
        name = "Remove Ticket Machine Rewards",
        description = "Remove ticket machine messages from chat and only show your own.\n§eExample: Steve has found a COMMON Figurine",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean ticketMachineAnnouncer;

    @Switch(
        name = "Remove Soul Well Announcements",
        description = "Remove soul well announcements from chat.\n§eExample: §b[MVP§c+§b] Steve §7has found a §6Bulldozer Perk I (Insane) §7in the §bSoul Well§7!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean soulWellAnnouncer;

    @Switch(
        name = "Remove Game Announcements",
        description = "Remove game announcements from chat.\n§eExample: A §e§lMega Skywars §bgame is available to join! §6§lCLICK HERE §bto join!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean gameAnnouncements;

    @Switch(
        name = "Remove Hype Limit Reminder",
        description = "Remove Hype limit reminders from chat.\n§eExample: §6You have reached your Hype limit...",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hypeLimitReminder;

    @Switch(
        name = "Player AdBlocker",
        description = "Remove spam messages from players, usually advertising something or begging for ranks.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean playerAdBlock;

    @Switch(
        name = "Remove BedWars Advertisements",
        description = "Remove player messages asking to join BedWars parties.\n§eExample: §b[MVP§c+§b] Steve§f: Join BedWars 2/4 party!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean bedwarsAdvertisements;

    @Switch(
        name = "Remove Friend/Guild Statuses",
        description = "Remove join/quit messages from friend/guild members.\n§eExample: §aFriend > §bSteve §ejoined.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean connectionStatus;

    @Switch(
        name = "Remove Guild MOTD",
        description = "Remove the guild Message Of The Day.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean guildMotd;

    @Switch(
        name = "Remove Chat Emojis",
        description = "Remove MVP++ chat emojis.\n§eExample: §c§lOOF",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean mvpEmotes;

    @Switch(
        name = "Remove Server Connected Messages",
        description = "Remove messages informing you of the lobby name you've just joined, or what lobby you're being sent to.\n§eExample: §bYou are currently connected to server §6mini104H§b.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean serverConnectedMessages;

    @Switch(
        name = "Remove Game Tips Messages",
        description = "Remove tips about the game you are playing.\n§eExample: §r§c§lTeaming is not allowed on Solo mode!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean gameTipMessages;

    @Switch(
        name = "Remove Auto Activated Quest Messages",
        description = "Remove automatically activated quest messages.\n§eExample: §aAutomatically activated: §6Daily Quest: Duels Winner",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean questsMessages;

    @Switch(
        name = "Remove Stats Messages",
        description = "Remove the \"view your stats\" messages.\n§eExample: §eClick to view the stats of your §bSkyWars§e game!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean statsMessages;

    @Switch(
        name = "Remove Curse of Spam Messages",
        description = "Hides the constant spam of Kali's curse of spam.\n§eExample: §eKALI HAS STRIKEN YOU WITH THE CURSE OF SPAM",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean curseOfSpam;

    @Switch(
        name = "Remove Bridge Self Goal Death Messages",
        description = "Hides the death message when you jump into your own goal in Bridge.\n§eExample: §cYou just jumped through your own goal, enjoy the void death! :)",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean bridgeOwnGoalDeath;

    @Switch(
        name = "Remove Duels No Stats Change Messages",
        description = "Hides the message explaining that your stats did not change for dueling through /duel or within in a party.\n§eExamples:\n§cYour stats did not change because you /duel'ed your opponent!\n§cYour stats did not change because you dueled someone in your party!\n§cNo stats will be affected in this round!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean duelsNoStatsChange;

    @Switch(
        name = "Remove Block Trail Disabled Messages",
        description = "Hides the message explaining that your duel's block trail cosmetic was disabled in specific gamemodes.\n§eExample: §cYour block trail aura is disabled in this mode!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean duelsBlockTrail;

    @Switch(
        name = "Remove SkyBlock Welcome Messages",
        description = "Removes \"§eWelcome to §aHypixel SkyBlock§e!§r\" messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean skyblockWelcome;

    @Switch(
        name = "Remove Gift Messages",
        description = "Removes \"§eThey have gifted §6x §eranks so far!§r\" messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean giftBlocker;

    @Switch(
        name = "Remove Seasonal Simulator Collection Messages",
        description = "Removes personal and global collected messages from chat for the Easter, Christmas, and Halloween variants.\n§eExamples:\n§aYou found a gift! §7(5 total)\n§b[MVP§c+§b] Steve§f §ehas reached §c20 §egifts!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean grinchPresents;

    @Switch(
        name = "Remove Earned Coins and Experience Messages",
        description = "Removes the earned coins and experience messages from chat.\n§eExamples:\n§b+25 Bed Wars Experience\n§6+10 coins!\n§aYou earned §2500 GEXP §afrom playing SkyBlock!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean earnedCoinsAndExp;

    @Switch(
        name = "Remove Replay Messages",
        description = "Removes replay messages from chat.\n§eExample: §6§aThis game has been recorded. §6Click here to watch the Replay!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean replayMessage;

    @Switch(
        name = "Remove Tip Messages",
        description = "Removes tip messages from chat.\n§eExample: §a§aYou tipped 5 players in 10 different games!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean tipMessage;

    @Switch(
        name = "Remove Online Status Messages",
        description = "Removes the online status messages from chat.\n§eExample: §6§lREMINDER: §r§6Your Online Status is currently set to §r§e§lAppear Offline",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean onlineStatus;

    @Switch(
        name = "Remove Main Lobby Fishing Announcements",
        description = "Removes Main Lobby Fishing announcements from chat when a player catches a special fish.\n§eExample: §b[MVP§c+§b] Steve§a caught §e§lNemo§a! Maybe he's lost again?",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean lobbyFishing;

    @Switch(
        name = "Remove Hot Potato Messages",
        description = "Removes Hot Potato messages from chat.\n§eExample: §c§lSteve burnt to a crisp due to a hot potato!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hotPotato;

    @Switch(
        name = "Remove Discord Safety Warning Messages",
        description = "Removes \"§4Please be mindful of Discord links in chat as they may pose a security risk\"",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean discordSafetyWarning;

    // AutoWB

    @Switch(
        name = "AutoWB",
        description = "Says configurable message to your friends/guild when they join.",
        category = "Chat", subcategory = "AutoWB"
    )
    public static boolean autoWB = false;

    @Switch(
        name = "Guild AutoWB",
        category = "Chat", subcategory = "AutoWB"
    )
    public static boolean guildAutoWB = true;

    @Switch(
        name = "Friend AutoWB",
        category = "Chat", subcategory = "AutoWB"
    )
    public static boolean friendsAutoWB = true;

    @Slider(
        name = "AutoWB Delay",
        category = "Chat", subcategory = "AutoWB",
        min = 2,
        max = 10
    )
    public static int autoWBCooldown = 2;

    @Text(
        name = "AutoWB Message",
        category = "Chat", subcategory = "AutoWB",
        size = 2
    )
    public static String autoWBMessage1 = "Welcome Back!";

    @Switch(
        name = "Random AutoWB Messages",
        category = "Chat", subcategory = "AutoWB"
    )
    public static boolean randomAutoWB = false;

    @Text(
        name = "First Random Message",
        category = "Chat", subcategory = "AutoWB",
        size = 2
    )
    public static String autoWBMessage2 = "Welcome back... General %player%";

    @Text(
        name = "Second Random Message",
        category = "Chat", subcategory = "AutoWB",
        size = 2
    )
    public static String autoWBMessage3 = "WB!";

    @Text(
        name = "Third Random Message",
        category = "Chat", subcategory = "AutoWB",
        size = 2
    )
    public static String autoWBMessage4 = "Greetings! %player%";

    @Text(
        name = "Fourth Random Message",
        category = "Chat", subcategory = "AutoWB",
        size = 2
    )
    public static String autoWBMessage5 = "Thanks for coming back to hell >:)";

    @Text(
        name = "Fifth Random Message",
        category = "Chat", subcategory = "AutoWB",
        size = 2
    )
    public static String autoWBMessage6 = "Its nice having you here today %player%";

    @Text(
        name = "Sixth Random Message",
        category = "Chat", subcategory = "AutoWB",
        size = 2
    )
    public static String autoWBMessage7 = "Yooooooooo Mr. %player%";

    @Text(
        name = "Seventh Random Message",
        category = "Chat", subcategory = "AutoWB",
        size = 2
    )
    public static String autoWBMessage8 = "Welcome back Padawan %player%";

    @Text(
        name = "Eighth Random Message",
        category = "Chat", subcategory = "AutoWB",
        size = 2
    )
    public static String autoWBMessage9 = "Welcome Back! <3";

    @Text(
        name = "Ninth Random Message",
        category = "Chat", subcategory = "AutoWB",
        size = 2
    )
    public static String autoWBMessage10 = "Thanks for coming to my TED talk.";

    // Tab

    @Dropdown(
        name = "Highlight Friends in Tab",
        description = "Add a star to the names of your Hypixel friends in tab.",
        category = "Tab", subcategory = "Highlighters",
        options = {"Off", "Left of Name", "Right of Name"}
    )
    public static int highlightFriendsInTab;

    @Dropdown(
        name = "Highlight Self in Tab",
        description = "Add a star to your name in tab.",
        category = "Tab", subcategory = "Highlighters",
        options = {"Off", "Left of Name", "Right of Name"}
    )
    public static int highlightSelfInTab;

    @Switch(
        name = "Hide NPCs in Tab",
        description = "Prevent NPCs from showing up in tab.",
        category = "Tab", subcategory = "Toggles"
    )
    public static boolean hideNpcsInTab = true;

    @Checkbox(
        name = "Don't Hide Important NPCs",
        description = "Keeps NPCs in tab in gamemodes like SkyBlock and Replay.",
        category = "Tab", subcategory = "Toggles"
    )
    public static boolean keepImportantNpcsInTab = true;

    @Switch(
        name = "Hide Guild Tags in Tab",
        description = "Prevent Guild tags from showing up in tab.",
        category = "Tab", subcategory = "Toggles"
    )
    public static boolean hideGuildTagsInTab;

    @Switch(
        name = "Hide Player Ranks in Tab",
        description = "Prevent player ranks from showing up in tab.",
        category = "Tab", subcategory = "Toggles"
    )
    public static boolean hidePlayerRanksInTab;

    @Switch(
        name = "Hide Ping in Tab",
        description = "Prevent ping from showing up in tab while playing games, since the value is misleading. Ping will remain visible in lobbies.",
        category = "Tab", subcategory = "Toggles"
    )
    public static boolean hidePingInTab = true;

    @Switch(
        name = "Cleaner Tab in SkyBlock",
        description = "Doesn't render player heads or ping for tab entries that aren't players in SkyBlock.",
        category = "Tab", subcategory = "Toggles"
    )
    public static boolean cleanerSkyblockTabInfo = true;

    @Switch(
        name = "Hide Advertisements in Tab",
        description = "Prevent Hypixel's advertisements from showing up in tab.\n§eExample: §aRanks, Boosters & MORE! §c§lSTORE.HYPIXEL.NET",
        category = "Tab", subcategory = "Toggles"
    )
    public static boolean hideAdsInTab = true;

    // Game

    @Switch(
        name = "Hide HUD Elements",
        description = "Hide HUD elements such as health, hunger, and armor bars where they are the same.",
        category = "Game", subcategory = "GUI"
    )
    public static boolean hideHudElements = false;

    @Switch(
        name = "Hide Advertisements in Bossbars",
        description = "Hide bossbars that advertise Hypixel.\n§eExample: §e§lPlaying §f§lSKYWARS §e§lon §f§lMC.HYPIXEL.NET",
        category = "Game", subcategory = "GUI"
    )
    public static boolean gameAdBossbar = true;

    @Switch(
        name = "Hardcore Hearts",
        description = "When your bed is broken/wither is killed in Bedwars/MiniWalls, set the heart style to Hardcore.",
        category = "Game", subcategory = "GUI"
    )
    public static boolean hardcoreHearts = true;

    @Switch(
        name = "Hide Game Starting Titles",
        description = "Hide titles such as gamemode names.\n§eExample: §4§lINSANE MODE",
        category = "Game", subcategory = "GUI"
    )
    public static boolean hideGameStartingTitles;

    @Switch(
        name = "Hide Game Ending Titles",
        description = "Hide titles that signify when the game has ended.\n§eExamples:\n§6§lVICTORY!\n§4§lGAME OVER!",
        category = "Game", subcategory = "GUI"
    )
    public static boolean hideGameEndingTitles;

    @Switch(
        name = "Hide Countdown Titles",
        description = "Hide countdowns that signify when a game is about to begin/end.",
        category = "Game", subcategory = "GUI"
    )
    public static boolean hideCountdownTitles;

    @Switch(
        name = "Hide Armor",
        description = "Hide armor in games where armor is always the same.",
        category = "Game", subcategory = "Entities"
    )
    public static boolean hideArmor = true;

    @Switch(
        name = "Hide Useless Game Nametags",
        description = "Hides unnecessary nametags such as those that say \"RIGHT CLICK\" or \"CLICK\" in SkyBlock, BedWars, SkyWars, and Duels, as well as other useless ones.",
        category = "Game", subcategory = "Entities"
    )
    public static boolean hideUselessArmorStandsGame;

    @Switch(
        name = "Notify When Blocks Run Out",
        description = "Pings you via a sound when your blocks are running out.",
        category = "Game", subcategory = "Sound"
    )
    public static boolean blockNotify;

    @Slider(
        name = "Block Number",
        description = "Modify the number of blocks you (don't?) have for the Notify When Blocks Run Out feature to work.",
        category = "Game", subcategory = "Sound",
        min = 4, max = 20
    )
    public static int blockNumber = 10;

    @Dropdown(
        name = "Block Notify Sound",
        description = "Choose what sound to play.",
        category = "Game", subcategory = "Sound",
        options = {"Hypixel Ding", "Golem Hit", "Blaze Hit", "Anvil Land", "Horse Death", "Ghast Scream", "Guardian Floop", "Cat Meow", "Dog Bark"}
    )
    public static int blockNotifySound = 0;

    @Switch(
        name = "Middle Waypoint Beacon in MiniWalls",
        description = "Adds a beacon at (0,0) when your MiniWither is dead in MiniWalls.",
        category = "Game", subcategory = "Arcade"
    )
    public static boolean miniWallsMiddleBeacon;

    @cc.polyfrost.oneconfig.config.annotations.Color(
        name = "MiniWalls Beacon Color",
        category = "Game", subcategory = "Arcade"
    )
    public static OneColor miniWallsMiddleBeaconColor = new OneColor(Color.RED);

    @Switch(
        name = "Hide Arcade Cosmetics",
        description = "Hide Arcade Cosmetics in Hypixel.",
        category = "Game", subcategory = "Arcade"
    )
    public static boolean hideArcadeCosmetics;

    @Switch(
        name = "Colored Beds",
        description = "Make beds a different color depending on the team they are on.",
        category = "Game", subcategory = "BedWars", size = 2
    )
    public static boolean coloredBeds = true;

    @Info(
        text = "Height Overlay requires Smooth Lightning to work.",
        category = "Game", subcategory = "BedWars",
        type = InfoType.ERROR
    )
    private static boolean heightOverlayInfo;

    @Info(
        text = "Height Overlay reloads chunks automatically when toggled.",
        category = "Game", subcategory = "BedWars",
        type = InfoType.WARNING
    )
    private static boolean heightOverlayInfo2;

    @Switch(
        name = "Height Overlay",
        description = "Make blocks that are in the Hypixel height limit a different color.",
        category = "Game", subcategory = "BedWars"
    )
    public static boolean heightOverlay;

    @Slider(
        name = "Height Overlay Tint Multiplier",
        description = "Adjust the tint multiplier.",
        category = "Game", subcategory = "BedWars",
        min = 1, max = 1000
    )
    public static int overlayAmount = 300;

    @Checkbox(
        name = "Edit Height Overlay Manually",
        description = "Enables the option to edit the height overlay manually.",
        category = "Game", subcategory = "BedWars"
    )
    public static boolean manuallyEditHeightOverlay;

    @Page(
        name = "Manual Height Overlay Editor",
        location = PageLocation.BOTTOM,
        category = "Game", subcategory = "BedWars"
    )
    public static BlockHighlightConfig blockHighlightConfig = new BlockHighlightConfig();

    @Switch(
        name = "Hide Actionbar in Dropper",
        description = "Hide the Actionbar in Dropper.",
        category = "Game", subcategory = "Dropper"
    )
    public static boolean hideDropperActionBar;

    @Switch(
        name = "Mute Hurt Sounds in Dropper",
        description = "Mute the sounds of other players failing in Dropper.",
        category = "Game", subcategory = "Dropper"
    )
    public static boolean muteDropperHurtSound;

    @Switch(
        name = "Lower Render Distance in Sumo",
        description = "Lowers render distance to your desired value in Sumo Duels.",
        category = "Game", subcategory = "Duels"
    )
    public static boolean sumoRenderDistance;

    @Slider(
        name = "Sumo Render Distance",
        description = "Choose your render distance.",
        category = "Game", subcategory = "Duels",
        min = 2, max = 5, step = 1
    )
    public static int sumoRenderDistanceAmount = 2;

    @Switch(
        name = "Hide Duels Cosmetics",
        description = "Hide Duels Cosmetics in Hypixel.",
        category = "Game", subcategory = "Duels"
    )
    public static boolean hideDuelsCosmetics;

    @Switch(
        name = "Mute Housing Music",
        description = "Prevent the Housing songs from being heard.",
        category = "Game", subcategory = "Housing"
    )
    public static boolean muteHousingMusic;

    @Switch(
        name = "Hide Actionbar in Housing",
        description = "Hide the Actionbar in Housing.",
        category = "Game", subcategory = "Housing"
    )
    public static boolean hideHousingActionBar;

    @Switch(
        name = "Pit Lag Reducer",
        description = "Hide entities at spawn while you are in the PVP area.",
        category = "Game", subcategory = "Pit"
    )
    public static boolean pitLagReducer = true;

    @Switch(
        name = "Remove Non-NPCs in SkyBlock",
        description = "Remove entities that are not NPCs in SkyBlock.",
        category = "Game", subcategory = "SkyBlock"
    )
    public static boolean hideNonNPCs;

    @Switch(
        name = "Highlight Opened Chests",
        description = "Highlight chests that have been opened.",
        category = "Game", subcategory = "SkyWars"
    )
    public static boolean highlightChests;

    @cc.polyfrost.oneconfig.config.annotations.Color(
        name = "Highlight Color",
        category = "Game", subcategory = "SkyWars"
    )
    public static OneColor highlightChestsColor = new OneColor(Color.RED);

    @Switch(
        name = "UHC Overlay",
        description = "Increase the size of dropped apples, golden apples, golden ingots, and player heads in UHC Champions and Speed UHC.",
        category = "Game", subcategory = "UHC"
    )
    public static boolean uhcOverlay;

    @Slider(
        name = "UHC Overlay Multiplier",
        description = "Adjust the multiplier.",
        category = "Game", subcategory = "UHC",
        min = 1f, max = 5f
    )
    public static float uhcOverlayMultiplier = 1f;

    @Switch(
        name = "UHC Middle Waypoint",
        description = "Adds a waypoint to signify (0,0).",
        category = "Game", subcategory = "UHC"
    )
    public static boolean uhcMiddleWaypoint;

    @Text(
        name = "UHC Middle Waypoint Text",
        description = "Text on waypoint.",
        category = "Game", subcategory = "UHC"
    )
    public static String uhcMiddleWaypointText = "0,0";

    // Lobby

    @Switch(
        name = "Hide Lobby NPCs",
        description = "Hide NPCs in the lobby.",
        category = "Lobby", subcategory = "NPCs"
    )
    public static boolean npcHider;

    @Switch(
        name = "Hide Useless Lobby Nametags",
        description = "Hides unnecessary nametags such as those that say \"RIGHT CLICK\" or \"CLICK TO PLAY\" in a lobby, as well as other useless ones.",
        category = "Lobby", subcategory = "NPCs"
    )
    public static boolean hideUselessArmorStands = true;

    @Switch(
        name = "Hide Lobby Bossbars",
        description = "Hide the bossbar in the lobby.",
        category = "Lobby", subcategory = "GUI"
    )
    public static boolean lobbyBossbar = true;

    @Switch(
        name = "Silent Lobby",
        description = "Prevent all sounds from playing when you are in a lobby.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean silentLobby;

    @Switch(
        name = "Disable Stepping Sounds",
        description = "Remove sounds created by stepping.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableSteppingSounds;

    @Switch(
        name = "Disable Slime Sounds",
        description = "Remove sounds created by slimes.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableSlimeSounds;

    @Switch(
        name = "Disable Dragon Sounds",
        description = "Remove sounds created by dragons.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableDragonSounds;

    @Switch(
        name = "Disable Wither Sounds",
        description = "Remove sounds created by withers & wither skeletons.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableWitherSounds;

    @Switch(
        name = "Disable Item Pickup Sounds",
        description = "Remove sounds created by picking up an item.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableItemPickupSounds;

    @Switch(
        name = "Disable Experience Orb Sounds",
        description = "Remove sounds created by experience orbs.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableExperienceOrbSounds;

    @Switch(
        name = "Disable Primed TNT Sounds",
        description = "Remove sounds created by primed TNT.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisablePrimedTntSounds;

    @Switch(
        name = "Disable Explosion Sounds",
        description = "Remove sounds created by explosions.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableExplosionSounds;

    @Switch(
        name = "Disable Delivery Man Sounds",
        description = "Remove sounds created by delivery man events.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableDeliveryManSounds;

    @Switch(
        name = "Disable Note Block Sounds",
        description = "Remove sounds created by note blocks.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableNoteBlockSounds;

    @Switch(
        name = "Disable Firework Sounds",
        description = "Remove sounds created by fireworks.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableFireworkSounds;

    @Switch(
        name = "Disable Levelup Sounds",
        description = "Remove sounds created by someone leveling up.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableLevelupSounds;

    @Switch(
        name = "Disable Arrow Sounds",
        description = "Remove sounds created by arrows.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableArrowSounds;

    @Switch(
        name = "Disable Bat Sounds",
        description = "Remove sounds created by bats.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableBatSounds;

    @Switch(
        name = "Disable Fire Sounds",
        description = "Remove sounds created by fire.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableFireSounds;

    @Switch(
        name = "Disable Enderman Sounds",
        description = "Remove sounds created by endermen.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableEndermanSounds;

    @Switch(
        name = "Disable Door Sounds",
        description = "Disable sounds caused by doors, trapdoors, and fence gates.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisableDoorSounds;

    @Switch(
        name = "Disable Portal Sounds",
        description = "Disable sounds caused by nether portals.",
        category = "Lobby", subcategory = "Sounds"
    )
    public static boolean lobbyDisablePortalSounds;

    @Switch(
        name = "Remove Limbo AFK Title",
        description = "Remove the AFK title when you get sent to limbo for being AFK.",
        category = "Lobby", subcategory = "Limbo"
    )
    public static boolean hideLimboTitle;

    @Switch(
        name = "Limbo Limiter",
        description = "While in Limbo, limit your framerate to reduce the load of the game on your computer.",
        category = "Lobby", subcategory = "Limbo"
    )
    public static boolean limboLimiter;

    @Slider(
        name = "Limbo Limiter FPS",
        description = "Set the maximal FPS while in Limbo.",
        category = "Lobby", subcategory = "Limbo",
        min = 1, max = 60
    )
    public static int limboFPS = 15;

    @Switch(
        name = "Limbo PM Ding",
        description = "While in Limbo, play the ding sound if you get a PM. Currently, Hypixel's option does not work in Limbo.",
        category = "Lobby", subcategory = "Limbo"
    )
    public static boolean limboDing = true;

    public static int configNumber = 0;

    @Exclude
    public static final ArrayList<String> wbMessages = new ArrayList<>();


    public HytilsConfig() {
        super(new Mod("Hytils Reborn", ModType.HYPIXEL, "/assets/hytils/hypixel.png", new VigilanceMigrator(new File(HytilsReborn.INSTANCE.oldModDir, "hytilsreborn.toml").getAbsolutePath())), "hytilsreborn.json");
        try {
            File modDir = HytilsReborn.INSTANCE.oldModDir;
            File oldModDir = new File(modDir.getParentFile(), "Hytilities Reborn");
            File oldConfig = new File(oldModDir, "hytilitiesreborn.toml");
            if (oldConfig.exists()) {
                FileUtils.writeStringToFile(new File(modDir, "hytilsreborn.toml"), FileUtils.readFileToString(oldConfig, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                if (!oldConfig.renameTo(new File(modDir, "hytilsreborn_backup.toml"))) {
                    Files.move(oldConfig.toPath(), modDir.toPath().resolve("hytilsreborn_backup.toml"), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialize();
        Class<?> autoGGClass = null;
        try {
            autoGGClass = Class.forName("club.sk1er.mods.autogg.config.AutoGGConfig");

            HytilsReborn.INSTANCE.isSk1erAutoGG = true;
        } catch (ClassNotFoundException ignored) {
        }

        if (configNumber != 3) { // Config version has not been set or is outdated
            if (configNumber == 1) {
                overlayAmount = 300;
            }
            if (configNumber <= 2) {
                if (autoGGClass != null) {
                    if (AutoGG.INSTANCE.getAutoGGConfig().isModEnabled()) {
                        autoGG = true;
                    }
                    if (AutoGG.INSTANCE.getAutoGGConfig().isSecondaryEnabled()) {
                        autoGGSecondMessage = true;
                    }
                    if (AutoGG.INSTANCE.getAutoGGConfig().isCasualAutoGGEnabled()) {
                        casualAutoGG = true;
                    }
                    if (AutoGG.INSTANCE.getAutoGGConfig().getAutoGGDelay() != 1) {
                        autoGGFirstPhraseDelay = AutoGG.INSTANCE.getAutoGGConfig().getAutoGGDelay();
                    }
                    if (AutoGG.INSTANCE.getAutoGGConfig().getSecondaryDelay() != 1) {
                        autoGGSecondPhraseDelay = AutoGG.INSTANCE.getAutoGGConfig().getSecondaryDelay();
                    }
                    if (AutoGG.INSTANCE.getAutoGGConfig().isAntiGGEnabled()) {
                        antiGG = true;
                    }
                    if (AutoGG.INSTANCE.getAutoGGConfig().isAntiKarmaEnabled()) {
                        hideKarmaMessages = true;
                    }

                    try {
                        Field sk1erEnabled = autoGGClass.getDeclaredField("autoGGEnabled");
                        sk1erEnabled.setAccessible(true);
                        sk1erEnabled.set(AutoGG.INSTANCE.getAutoGGConfig(), false);

                        AutoGG.INSTANCE.getAutoGGConfig().markDirty();
                        AutoGG.INSTANCE.getAutoGGConfig().writeData();
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    Notifications.INSTANCE.send("Hytils Reborn", "AutoGG settings have been migrated to Hytils Reborn. You can now configure them in the Hytils Reborn settings, and remove Sk1erLLC's AutoGG.", 5);
                }

                try {
                    Class.forName("club.sk1er.lobbysounds.config.Sounds");
                    boolean modified = false;
                    if (Sounds.DISABLE_SLIME_SOUNDS) {
                        lobbyDisableSlimeSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_DRAGON_SOUNDS) {
                        lobbyDisableDragonSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_WITHER_SOUNDS) {
                        lobbyDisableWitherSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_ITEM_PICKUP_SOUNDS) {
                        lobbyDisableItemPickupSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_EXPERIENCE_SOUNDS) {
                        lobbyDisableExperienceOrbSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_TNT_PRIME_SOUNDS) {
                        lobbyDisablePrimedTntSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_EXPLOSION_SOUNDS) {
                        lobbyDisableExplosionSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_DELIVERY_MAN_SOUNDS) {
                        lobbyDisableDeliveryManSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_NOTE_SOUNDS) {
                        lobbyDisableNoteBlockSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_FIREWORKS_SOUNDS) {
                        lobbyDisableFireworkSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_LEVELUP_SOUNDS) {
                        lobbyDisableLevelupSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_ARROW_SOUNDS) {
                        lobbyDisableArrowSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_BAT_SOUNDS) {
                        lobbyDisableBatSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_FIRE_SOUNDS) {
                        lobbyDisableFireSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_ENDERMEN_SOUNDS) {
                        lobbyDisableEndermanSounds = true;
                        modified = true;
                    }
                    if (Sounds.DISABLE_STEP_SOUNDS) {
                        lobbyDisableSteppingSounds = true;
                        modified = true;
                    }

                    if (Sounds.DISABLE_SLIME_SOUNDS &&
                        Sounds.DISABLE_DRAGON_SOUNDS &&
                        Sounds.DISABLE_WITHER_SOUNDS &&
                        Sounds.DISABLE_ITEM_PICKUP_SOUNDS &&
                        Sounds.DISABLE_EXPERIENCE_SOUNDS &&
                        Sounds.DISABLE_TNT_PRIME_SOUNDS &&
                        Sounds.DISABLE_EXPLOSION_SOUNDS &&
                        Sounds.DISABLE_DELIVERY_MAN_SOUNDS &&
                        Sounds.DISABLE_NOTE_SOUNDS &&
                        Sounds.DISABLE_FIREWORKS_SOUNDS &&
                        Sounds.DISABLE_LEVELUP_SOUNDS &&
                        Sounds.DISABLE_ARROW_SOUNDS &&
                        Sounds.DISABLE_BAT_SOUNDS &&
                        Sounds.DISABLE_FIRE_SOUNDS &&
                        Sounds.DISABLE_ENDERMEN_SOUNDS &&
                        Sounds.DISABLE_STEP_SOUNDS) {
                        silentLobby = true;
                        lobbyDisableDoorSounds = true;
                        lobbyDisablePortalSounds = true;
                    }

                    if (modified) {
                        Notifications.INSTANCE.send("Hytils Reborn", "Lobby Sounds settings have been migrated to Hytils Reborn. You can now configure them in the Hytils Reborn settings, and remove Sk1erLLC's Lobby Sounds.", 5);
                    }
                } catch (ClassNotFoundException ignored) {

                }
            }
            configNumber = 3; // set this to the current config version
            save();
        }

        addDependency("autoQueueDelay", "autoQueue");

        addDependency("gexpMode", "autoGetGEXP");

        addDependency("autoGGSecondMessage", "autoGG");
        addDependency("casualAutoGG", "autoGG");
        addDependency("autoGGMessage", "autoGG");
        addDependency("autoGGFirstPhraseDelay", "autoGG");
        addDependency("autoGGMessage2", "autoGG");
        addDependency("autoGGSecondPhraseDelay", "autoGG");

        Supplier<Boolean> autoGGEnabled = () -> !HytilsReborn.INSTANCE.isSk1erAutoGG || !AutoGG.INSTANCE.getAutoGGConfig().isModEnabled();

        addDependency("autoGG", "Sk1er's AutoGG Enabled", autoGGEnabled);
        addDependency("autoGGSecondMessage", "Sk1er's AutoGG Enabled", autoGGEnabled);
        addDependency("casualAutoGG", "Sk1er's AutoGG Enabled", autoGGEnabled);
        addDependency("autoGGMessage", "Sk1er's AutoGG Enabled", autoGGEnabled);
        addDependency("autoGGFirstPhraseDelay", "Sk1er's AutoGG Enabled", autoGGEnabled);
        addDependency("autoGGMessage2", "Sk1er's AutoGG Enabled", autoGGEnabled);
        addDependency("autoGGSecondPhraseDelay", "Sk1er's AutoGG Enabled", autoGGEnabled);
        addDependency("antiGG", "Sk1er's AutoGG Enabled", autoGGEnabled);

        addDependency("glPhrase", "autoGL");

        addDependency("guildAutoWB", "autoWB");
        addDependency("friendsAutoWB", "autoWB");
        addDependency("autoWBCooldown", "autoWB");
        addDependency("randomAutoWB", "autoWB");

        for (String property : Lists.newArrayList("autoWBMessage1", "autoWBMessage2", "autoWBMessage3", "autoWBMessage4", "autoWBMessage5", "autoWBMessage6", "autoWBMessage7", "autoWBMessage8", "autoWBMessage9", "autoWBMessage10")) {
            addListener(property, this::setWBMessages);
            addDependency(property, "autoWB");
        }
        setWBMessages();

        addDependency("disableNotifyMiningFatigueSkyblock", "notifyMiningFatigue");

        addDependency("chatSwapperReturnChannel", "chatSwapper");
        addDependency("chattingIntegration", "chatSwapper");
        addDependency("hideAllChatMessage", "chatSwapper");

        addListener("cleanerGameStartAnnouncements", () -> {
            if (!cleanerGameStartAnnouncements) GameStartCompactor.lastMessage = null;
        });

        addDependency("blockNumber", "blockNotify");
        addDependency("blockNotifySound", "blockNotify");
        addDependency("blockNotifySound", "blockNotify");

        addDependency("keepImportantNpcsInTab", "hideNpcsInTab");

        addDependency("highlightChestsColor", "highlightChests");

        addDependency("uhcOverlayMultiplier", "uhcOverlay");

        addDependency("uhcMiddleWaypointText", "uhcMiddleWaypoint");

        addDependency("miniWallsMiddleBeaconColor", "miniWallsMiddleBeacon");

        addDependency("sumoRenderDistanceAmount", "sumoRenderDistance");

        addDependency("overlayAmount", "heightOverlay");

        addDependency("putInCaps", "notifyWhenKick");

        addListener("coloredBeds", () -> Minecraft.getMinecraft().renderGlobal.loadRenderers());

        addListener("heightOverlay", () -> Minecraft.getMinecraft().renderGlobal.loadRenderers());
        addListener("overlayAmount", () -> {
            DarkColorUtils.invalidateCache();
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        });

        //addDependency("editHeightOverlay", "heightOverlay");
        addDependency("manuallyEditHeightOverlay", "heightOverlay");
        //addDependency("editHeightOverlay", "manuallyEditHeightOverlay");

        Arrays.asList(
            "lobbyDisableSteppingSounds", "lobbyDisableSlimeSounds", "lobbyDisableDragonSounds", "lobbyDisableWitherSounds",
            "lobbyDisableItemPickupSounds", "lobbyDisableExperienceOrbSounds", "lobbyDisablePrimedTntSounds",
            "lobbyDisableExplosionSounds", "lobbyDisableDeliveryManSounds", "lobbyDisableMysteryBoxSounds",
            "lobbyDisableFireworkSounds", "lobbyDisableLevelupSounds", "lobbyDisableArrowSounds", "lobbyDisableBatSounds",
            "lobbyDisableFireSounds", "lobbyDisableEndermanSounds", "lobbyDisableDoorSounds", "lobbyDisablePortalSounds"
        ).forEach(property -> addDependency(property, "Silent Lobby", () -> !silentLobby));
    }

    public void hideTabulous() {
        hideNpcsInTab = false;
        keepImportantNpcsInTab = false;
        hideGuildTagsInTab = false;
        hidePlayerRanksInTab = false;
        hidePingInTab = false;
        cleanerSkyblockTabInfo = false;
        hideAdsInTab = false;
        save();
        addDependency("hideNpcsInTab", "Tabulous", () -> false);
        addDependency("keepImportantNpcsInTab", "Tabulous", () -> false);
        addDependency("hideGuildTagsInTab", "Tabulous", () -> false);
        addDependency("hidePlayerRanksInTab", "Tabulous", () -> false);
        addDependency("hidePingInTab", "Tabulous", () -> false);
        addDependency("cleanerSkyblockTabInfo", "Tabulous", () -> false);
        addDependency("hideAdsInTab", "Tabulous", () -> false);
    }

    private void setWBMessages() {
        wbMessages.clear();
        wbMessages.addAll(Arrays.asList(autoWBMessage1, autoWBMessage2, autoWBMessage3, autoWBMessage4, autoWBMessage5, autoWBMessage6, autoWBMessage7, autoWBMessage8, autoWBMessage9, autoWBMessage10));
    }
}
