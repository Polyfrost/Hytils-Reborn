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

package cc.woverflow.hytils.config;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.util.ColorUtils;
import com.google.common.collect.Lists;
import gg.essential.api.EssentialAPI;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import kotlin.jvm.functions.Function0;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

public class HytilsConfig extends Vigilant {

    // API

    @Property(
        type = PropertyType.TEXT, name = "API Key",
        description = "The API Key, for some features that require accessing to the Hypixel API such as the Auto GEXP and winstreak features.",
        category = "API",
        protectedText = true
    )
    public static String apiKey = "";

    // Automatic

    @Property(
        type = PropertyType.SWITCH, name = "Automatically Get API Key",
        description = "Automatically get the API Key from /api new.",
        category = "Automatic", subcategory = "API"
    )
    public static boolean autoGetAPI = true;

    @Property(
        type = PropertyType.SWITCH, name = "Auto Start",
        description = "Join Hypixel immediately once the client has loaded to the main menu.",
        category = "Automatic", subcategory = "General"
    )
    public static boolean autoStart;

    @Property(
        type = PropertyType.SWITCH, name = "Auto Queue",
        description = "Automatically queues for another game once you win or die.\n§eThis will require you to interact with the game in a way to prevent abuse.",
        category = "Automatic", subcategory = "Game"
    )
    public static boolean autoQueue;

    @Property(
        type = PropertyType.SLIDER, name = "Auto Queue Delay",
        description = "Delays the execution of Auto Queue.\n§eMeasured in seconds.",
        category = "Automatic", subcategory = "Game",
        max = 10
    )
    public static int autoQueueDelay;

    @Property(
        type = PropertyType.SWITCH, name = "Auto-Complete Play Commands",
        description = "Allows tab completion of /play commands.",
        category = "Automatic", subcategory = "Game"
    )
    public static boolean autocompletePlayCommands;

    @Property(
        type = PropertyType.SWITCH, name = "Limbo Play Helper",
        description = "When a /play command is run in Limbo, this runs /l first and then the command.",
        category = "Automatic", subcategory = "Game"
    )
    public static boolean limboPlayCommandHelper;

    @Property(
        type = PropertyType.SWITCH, name = "Auto GL",
        description = "Send a message 5 seconds before a Hypixel game starts.",
        category = "Automatic", subcategory = "AutoGL"
    )
    public static boolean autoGL;

    @Property(
        type = PropertyType.SELECTOR, name = "Auto GL Phrase",
        description = "Choose what message is said.",
        category = "Automatic", subcategory = "AutoGL",
        options = {"glhf", "Good Luck", "GL", "Have a good game!", "gl", "Good luck!"}
    )
    public static int glPhrase = 0;

    @Property(
        type = PropertyType.SWITCH, name = "Anti GL",
        description = "Remove all GL messages from chat.",
        category = "Automatic", subcategory = "AutoGL"
    )
    public static boolean antiGL;

    @Property(
        type = PropertyType.SWITCH, name = "Auto Friend",
        description = "Automatically accept friend requests.",
        category = "Automatic", subcategory = "Social"
    )
    public static boolean autoFriend;

    @Property(
        type = PropertyType.SWITCH, name = "Automatically Check GEXP",
        description = "Automatically check your GEXP after you win a Hypixel game.\n§4Requires an API Key.",
        category = "Automatic", subcategory = "Stats"
    )
    public static boolean autoGetGEXP;

    @Property(
        type = PropertyType.SELECTOR, name = "GEXP Mode",
        description = "Choose which GEXP to get.",
        category = "Automatic", subcategory = "Stats",
        options = {"Daily", "Weekly"}
    )
    public static int gexpMode = 0;

    @Property(
        type = PropertyType.SWITCH, name = "Automatically Check Winstreak",
        description = "Automatically check your winstreak after you win a Hypixel game.\n§4Requires an API Key.",
        category = "Automatic", subcategory = "Stats"
    )
    public static boolean autoGetWinstreak;

    // Chat

    @Property(
        type = PropertyType.SWITCH, name = "Hide Locraw Messages",
        description = "Hide locraw messages in chat (e.g {\"server\": \"something\"}).",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hideLocraw = true;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Lobby Statuses",
        description = "Remove lobby join messages from chat.\n§eExample: §b[MVP§c+§b] Steve §6joined the lobby!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean lobbyStatus;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Mystery Box Rewards",
        description = "Remove mystery box messages from chat and only show your own.\n§eExample: §b✦ §b[MVP§c+§b] Steve §ffound a §6Legendary Hype Train Gadget§f!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean mysteryBoxAnnouncer;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Soul Well Announcements",
        description = "Remove soul well announcements from chat.\n§eExample: §b[MVP§c+§b] Steve §7has found a §6Bulldozer Perk I (Insane) §7in the §bSoul Well§7!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean soulWellAnnouncer;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Game Announcements",
        description = "Remove game announcements from chat.\n§eExample: §b➤ A §e§lMega Skywars §bgame is available to join! §6§lCLICK HERE §bto join!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean gameAnnouncements;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Hype Limit Reminder",
        description = "Remove Hype limit reminders from chat.\n§eExample: §f➤ §6You have reached your Hype limit...",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hypeLimitReminder;

    @Property(
        type = PropertyType.SWITCH, name = "Player AdBlocker",
        description = "Remove spam messages from players, usually advertising something or begging for ranks.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean playerAdBlock;

    @Property(
        type = PropertyType.SWITCH, name = "Remove BedWars Advertisements",
        description = "Remove player messages asking to join BedWars parties.\n§eExample: §7[1✫] §b[MVP§c+§b] Steve§f: Join BedWars 2/4 party!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean bedwarsAdvertisements;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Friend/Guild Statuses",
        description = "Remove join/quit messages from friend/guild members.\n§eExample: §aFriend > §bSteve §ejoined.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean connectionStatus;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Guild MOTD",
        description = "Remove the guild Message Of The Day.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean guildMotd;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Chat Emojis",
        description = "Remove MVP++ chat emojis.\n§eExample: §c§lOOF",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean mvpEmotes;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Server Connected Messages",
        description = "Remove messages informing you of the lobby name you've just joined, or what lobby you're being sent to.\n§eExample: §bYou are currently connected to server §6mini104H§b.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean serverConnectedMessages;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Game Tips Messages",
        description = "Remove tips about the game you are playing.\n§eExample: §r§c§lTeaming is not allowed on Solo mode!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean gameTipMessages;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Auto Activated Quest Messages",
        description = "Remove automatically activated quest messages.\n§eExample: §aAutomatically activated: §6Daily Quest: Duels Winner",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean questsMessages;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Stats Messages",
        description = "Remove the \"view your stats\" messages.\n§eExample: §eClick to view the stats of your §bSkyWars§e game!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean statsMessages;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Curse of Spam Messages",
        description = "Hides the constant spam of Kali's curse of spam.\n§eExample: §eKALI HAS STRIKEN YOU WITH THE CURSE OF SPAM",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean curseOfSpam;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Bridge Self Goal Death Messages",
        description = "Hides the death message when you jump into your own goal in Bridge.\n§eExample: §cYou just jumped through your own goal, enjoy the void death! :)",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean bridgeOwnGoalDeath;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Duels No Stats Change Messages",
        description = "Hides the message explaining that your stats did not change for dueling through /duel or within in a party.\n§eExamples:\n§cYour stats did not change because you /duel'ed your opponent!\n§cYour stats did not change because you dueled someone in your party!\n§cNo stats will be affected in this round!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean duelsNoStatsChange;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Block Trail Disabled Messages",
        description = "Hides the message explaining that your duel's block trail cosmetic was disabled in specific gamemodes.\n§eExample: §cYour block trail aura is disabled in this mode!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean duelsBlockTrail;

    @Property(
        type = PropertyType.SWITCH, name = "Remove SkyBlock Welcome Messages",
        description = "Removes \"§eWelcome to §aHypixel SkyBlock§e!§r\" messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean skyblockWelcome;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Gift Messages",
        description = "Removes \"§eThey have gifted §6x §eranks so far!§r\" messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean giftBlocker;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Seasonal Simulator Collection Messages",
        description = "Removes personal and global collected messages from chat for the Easter, Christmas, and Halloween variants.\n§eExamples:\n§aYou found a gift! §7(5 total)\n§b[MVP§c+§b] Steve§f §ehas reached §c20 §egifts!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean grinchPresents;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Earned Coins and Experience Messages",
        description = "Removes the earned coins and experience messages from chat.\n§eExamples:\n§b+25 Bed Wars Experience\n§6+10 coins!\n§aYou earned §2500 GEXP §afrom playing SkyBlock!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean earnedCoinsAndExp;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Replay Messages",
        description = "Removes replay messages from chat.\n§eExample: §6§aThis game has been recorded. §6Click here to watch the Replay!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean replayMessage;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Tip Messages",
        description = "Removes tip messages from chat.\n§eExample: §a§aYou tipped 5 players in 10 different games!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean tipMessage;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Online Status Messages",
        description = "Removes the online status messages from chat.\n§eExample: §6§lREMINDER: §r§6Your Online Status is currently set to §r§e§lAppear Offline",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean onlineStatus;

    @Property(
        type = PropertyType.SWITCH, name = "Trim Line Separators",
        description = "Prevent separators from overflowing onto the next chat line.",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean lineBreakerTrim = true;

    @Property(
        type = PropertyType.SWITCH, name = "Clean Line Separators",
        description = "Change all line separator to become smoother.\n§eExample:§r ----- §4-> §r§m-----§r",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean cleanLineSeparator = true;

    @Property(
        type = PropertyType.SWITCH, name = "White Chat",
        description = "Make nons' chat messages appear as the normal chat message color.\n§eExample: §7Steve: Hey! §e-> §7Player§f: Hey!",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean whiteChat;

    @Property(
        type = PropertyType.SWITCH, name = "White Private Messages",
        description = "Make private messages appear as the normal chat message color.\n§eExample: §dFrom §b[MVP§c+§b] Steve§7: hey! §e-> §dFrom §b[MVP§c+§b] Player§f: hey!",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean whitePrivateMessages;

    @Property(
        type = PropertyType.SWITCH, name = "Colored Friend/Guild Statuses",
        description = "Colors the join/leave status of friends and guild members.\n§eExamples:\n§a§aF > §bSteve§7 §r§ejoined. -> §a§aF > §bSteve§7 §ajoined§e.§r\n§a§2G > §bSteve§7 §r§eleft. -> §a§2G > §bSteve§7 §cleft§e.§r",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean coloredStatuses;

    @Property(
        type = PropertyType.SWITCH, name = "Cleaner Game Start Counter",
        description = "Compacts game start announcements.\n§eExample: The game starts in 20 seconds!",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean cleanerGameStartAnnouncements;

    @Property(
        type = PropertyType.SWITCH, name = "Short Channel Names",
        description = "Abbreviate chat channel names.\n§eExample: §2Guild §e-> §2G§e, §9Party §e-> §9P§e, §aFriend §e-> §aF",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean shortChannelNames;

    @Property(
        type = PropertyType.SWITCH, name = "Game Status Restyle",
        description = "Replace common game status messages with a new style.\n§eExamples:\n§a§l+ §bSteve §e(§b1§e/§b12§e)\n§c§l- §bSteve§r\n§e§l* §aGame starts in §b§l5 §aseconds.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean gameStatusRestyle;

    @Property(
        type = PropertyType.SWITCH, name = "Player Count Before Player Name",
        description = "Put the player count before the player name in game join/leave messages.\n§eExample: §a§l+ §e(§b1§e/§b12§e) §bSteve",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountBeforePlayerName;

    @Property(
        type = PropertyType.SWITCH, name = "Player Count on Player Leave",
        description = "Include the player count when players leave.\n§eExample: §c§l- §bSteve §r§e(§b1§e/§b12§e)§r",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountOnPlayerLeave;

    @Property(
        type = PropertyType.SWITCH, name = "Player Count Padding",
        description = "Place zeros at the beginning of the player count to align with the max player count.\n§eExample: §a§l+ §bSteve §e(§b001§e/§b100§e)",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean padPlayerCount;

    @Property(
        type = PropertyType.SWITCH, name = "Party Chat Swapper",
        description = "Automatically change to and out of a party channel when joining/leaving a party.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean chatSwapper;

    @Property(
        type = PropertyType.SELECTOR, name = "Chat Swapper Channel",
        description = "The channel to return to when leaving a party.",
        category = "Chat", subcategory = "Parties",
        options = {"ALL", "GUILD", "OFFICER"}
    )
    public static int chatSwapperReturnChannel;

    @Property(
        type = PropertyType.SWITCH, name = "Swap Chatting Tab With Chat Swapper",
        description = "Automatically switch your Chatting chat tab when chat swapper swaps your chat channel.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean chattingIntegration;

    @Property(
        type = PropertyType.SWITCH, name = "Remove All Chat Message",
        description = "Hide the \"§aYou are now in the §6ALL§a channel§r\" message when auto-switching.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean hideAllChatMessage;

    @Property(
        type = PropertyType.SWITCH, name = "Thank Watchdog",
        description = "Compliment Watchdog when someone is banned, or a Watchdog announcement is sent.\n§eExample: §fThanks Watchdog!",
        category = "Chat", subcategory = "Watchdog"
    )
    public static boolean thankWatchdog;

    @Property(
        type = PropertyType.SWITCH, name = "Auto Chat Report Confirm",
        description = "Automatically confirms chat reports.",
        category = "Automatic", subcategory = "Chat"
    )
    public static boolean autoChatReportConfirm;

    @Property(
        type = PropertyType.SWITCH, name = "Auto Party Warp Confirm",
        description = "Automatically confirms party warps.",
        category = "Automatic", subcategory = "Chat"
    )
    public static boolean autoPartyWarpConfirm;

    @Property(
        type = PropertyType.SWITCH, name = "Guild Welcome Message",
        description = "Send a friendly welcome message when a player joins your guild.\n§eExample: §fWelcome to the guild Steve!",
        category = "Chat", subcategory = "Guild"
    )
    public static boolean guildWelcomeMessage;

    // AutoWB

    @Property(
        type = PropertyType.SWITCH,
        name = "AutoWB",
        description = "Says configurable message to your friends/guild when they join.",
        category = "Chat", subcategory = "AutoWB"
    )
    public static boolean autoWB = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Guild AutoWB",
        description = "Sends an AutoWB message if a guild member joins.",
        category = "Chat", subcategory = "AutoWB"
    )
    public static boolean guildAutoWB = true;

    @Property(
        type = PropertyType.SWITCH,
        name = "Friend AutoWB",
        description = "Sends an AutoWB message if a friend joins.",
        category = "Chat", subcategory = "AutoWB"
    )
    public static boolean friendsAutoWB = true;

    @Property(
        type = PropertyType.SLIDER,
        name = "AutoWB Delay",
        description = "The delay before an AutoWB message is sent.",
        category = "Chat", subcategory = "AutoWB",
        min = 2,
        max = 10
    )
    public static int autoWBCooldown = 2;

    @Property(
        type = PropertyType.TEXT,
        name = "AutoWB Message",
        description = "Changes the primary AutoWB message that gets sent.",
        category = "Chat", subcategory = "AutoWB"
    )
    public static String autoWBMessage1 = "Welcome Back!";

    @Property(
        type = PropertyType.SWITCH,
        name = "Random AutoWB Messages",
        description = "Enables random messages.",
        category = "Chat", subcategory = "AutoWB"
    )
    public static boolean randomAutoWB = false;

    @Property(
        type = PropertyType.TEXT,
        name = "First Random Message",
        description = "The random message that has a chance to be send (leave blank to disable).",
        category = "Chat", subcategory = "AutoWB"
    )
    public static String autoWBMessage2= "Welcome back... General %player%";

    @Property(
        type = PropertyType.TEXT,
        name = "Second Random Message",
        description = "The random message that has a chance to be send (leave blank to disable).",
        category = "Chat", subcategory = "AutoWB"
    )
    public static String autoWBMessage3= "WB!";

    @Property(
        type = PropertyType.TEXT,
        name = "Third Random Message",
        description = "The random message that has a chance to be send (leave blank to disable).",
        category = "Chat", subcategory = "AutoWB"
    )
    public static String autoWBMessage4= "Greetings! %player%";

    @Property(
        type = PropertyType.TEXT,
        name = "Fourth Random Message",
        description = "The random message that has a chance to be send (leave blank to disable).",
        category = "Chat", subcategory = "AutoWB"
    )
    public static String autoWBMessage5= "Thanks for coming back to hell >:)";

    @Property(
        type = PropertyType.TEXT,
        name = "Fifth Random Message",
        description = "The random message that has a chance to be send (leave blank to disable).",
        category = "Chat", subcategory = "AutoWB"
    )
    public static String autoWBMessage6 = "Its nice having you here today %player%";

    @Property(
        type = PropertyType.TEXT,
        name = "Sixth Random Message",
        description = "The random message that has a chance to be send (leave blank to disable).",
        category = "Chat", subcategory = "AutoWB"
    )
    public static String autoWBMessage7 = "Yooooooooo Mr. %player%";

    @Property(
        type = PropertyType.TEXT,
        name = "Seventh Random Message",
        description = "The random message that has a chance to be send (leave blank to disable).",
        category = "Chat", subcategory = "AutoWB"
    )
    public static String autoWBMessage8 = "Welcome back Padawan %player%";

    @Property(
        type = PropertyType.TEXT,
        name = "Eighth Random Message",
        description = "The random message that has a chance to be send (leave blank to disable).",
        category = "Chat", subcategory = "AutoWB"
    )
    public static String autoWBMessage9 = "Welcome Back! <3";

    @Property(
        type = PropertyType.TEXT,
        name = "Ninth Random Message",
        description = "The random message that has a chance to be send (leave blank to disable).",
        category = "Chat", subcategory = "AutoWB"
    )
    public static String autoWBMessage10 = "Thanks for coming to my TED talk.";



    @Property(
        type = PropertyType.SWITCH, name = "Shout Cooldown",
        description = "Show the amount of time remaining until /shout can be reused.\n§eExample: §eShout command is on cooldown. Please wait 30 more seconds.",
        category = "Chat", subcategory = "Cooldown"
    )
    public static boolean preventShoutingOnCooldown;

    @Property(
        type = PropertyType.SWITCH, name = "Non Speech Cooldown",
        description = "Show the amount of time remaining until you can speak if you are a non.\n§eExample: §eYour freedom of speech is on cooldown. Please wait 3 more seconds.\n§4Requires an API Key.",
        category = "Chat", subcategory = "Cooldown"
    )
    public static boolean preventNonCooldown;

    // General

    @Property(
        type = PropertyType.SWITCH, name = "Notify Mining Fatigue",
        description = "Send a notification when you get mining fatigue.",
        category = "General", subcategory = "Potion Effects"
    )
    public static boolean notifyMiningFatigue;

    @Property(
        type = PropertyType.SWITCH, name = "Disable Mining Fatigue Notification in SkyBlock",
        description = "Disable the mining fatigue notification in SkyBlock.",
        category = "General", subcategory = "Potion Effects"
    )
    public static boolean disableNotifyMiningFatigueSkyblock;

    @Property(
        type = PropertyType.SWITCH, name = "Hide NPCs in Tab",
        description = "Prevent NPCs from showing up in tab.",
        category = "General", subcategory = "Tab"
    )
    public static boolean hideNpcsInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Don't Hide Important NPCs",
        description = "Keeps NPCs in tab in gamemodes like SkyBlock and Replay.",
        category = "General", subcategory = "Tab"
    )
    public static boolean keepImportantNpcsInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Guild Tags in Tab",
        description = "Prevent Guild tags from showing up in tab.",
        category = "General", subcategory = "Tab"
    )
    public static boolean hideGuildTagsInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Player Ranks in Tab",
        description = "Prevent player ranks from showing up in tab.",
        category = "General", subcategory = "Tab"
    )
    public static boolean hidePlayerRanksInTab;

    @Property(
        type = PropertyType.SELECTOR, name = "Highlight Friends In Tab",
        description = "Add a star to the names of your Hypixel friends in tab.",
        category = "General", subcategory = "Tab",
        options = {"Off", "Left of Name", "Right of Name"}
    )
    public static int highlightFriendsInTab;

    @Property(
        type = PropertyType.SELECTOR, name = "Highlight Self In Tab",
        description = "Add a star to your name in tab.",
        category = "General", subcategory = "Tab",
        options = {"Off", "Left of Name", "Right of Name"}
    )
    public static int highlightSelfInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Cleaner Tab in SkyBlock",
        description = "Doesn't render player heads or ping for tab entries that aren't players in SkyBlock.",
        category = "General", subcategory = "Tab"
    )
    public static boolean cleanerSkyblockTabInfo;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Ping in Tab",
        description = "Prevent ping from showing up in tab while playing games, since the value is misleading. Ping will remain visible in lobbies.",
        category = "General", subcategory = "Tab"
    )
    public static boolean hidePingInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Broadcast Achievements",
        description = "Announce in Guild chat when you get an achievement.",
        category = "General", subcategory = "Guilds"
    )
    public static boolean broadcastAchievements;

    @Property(
        type = PropertyType.SWITCH, name = "Broadcast Levelup",
        description = "Announce in Guild chat when you level up.",
        category = "General", subcategory = "Guilds"
    )
    public static boolean broadcastLevelup;

    // Game

    @Property(
        type = PropertyType.SWITCH, name = "Notify When Kicked From Game",
        description = "Notify in party chat when you are kicked from the game due to a connection issue.",
        category = "Game", subcategory = "Chat"
    )
    public static boolean notifyWhenKick;

    @Property(
        type = PropertyType.SWITCH, name = "Put Notify Message In Capital Letters",
        description = "Put the message in capital messages instead of proper formatting.",
        category = "Game", subcategory = "Chat"
    )
    public static boolean putInCaps;

    @Property(
        type = PropertyType.SWITCH, name = "Highlight Opened Chests",
        description = "Highlight chests that have been opened.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean highlightChests;

    @Property(
        type = PropertyType.COLOR, name = "Highlight Color",
        description = "Highlight chests that have been opened.",
        category = "Game", subcategory = "Visual"
    )
    public static Color highlightChestsColor = Color.RED;

    @Property(
        type = PropertyType.SWITCH, name = "UHC Overlay",
        description = "Increase the size of dropped apples, golden apples, golden ingots, and player heads in UHC Champions and Speed UHC.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean uhcOverlay;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, name = "UHC Overlay Multiplier",
        description = "Adjust the multiplier.",
        category = "Game", subcategory = "Visual",
        minF = 1f, maxF = 5f
    )
    public static float uhcOverlayMultiplier = 1f;

    @Property(
        type = PropertyType.SWITCH, name = "UHC Middle Waypoint",
        description = "Adds a waypoint to signify (0,0).",
        category = "Game", subcategory = "Visual"
    )
    public static boolean uhcMiddleWaypoint;

    @Property(
        type = PropertyType.TEXT, name = "UHC Middle Waypoint Text",
        description = "Text on waypoint.",
        category = "Game", subcategory = "Visual"
    )
    public static String uhcMiddleWaypointText = "0,0";

    @Property(
        type = PropertyType.SWITCH, name = "Lower Render Distance in Sumo",
        description = "Lowers render distance to your desired value in Sumo Duels.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean sumoRenderDistance;

    @Property(
        type = PropertyType.SLIDER, name = "Sumo Render Distance",
        description = "Choose your render distance.",
        category = "Game", subcategory = "Visual",
        min = 1, max = 5
    )
    public static int sumoRenderDistanceAmount = 2;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Armor",
        description = "Hide armor in games where armor is always the same.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideArmor;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Useless Game Nametags",
        description = "Hides unnecessary nametags such as those that say \"§eRIGHT CLICK§r\" or \"§eCLICK§r\" in SkyBlock, BedWars, SkyWars, and Duels, as well as other useless ones.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideUselessArmorStandsGame;

    @Property(
        type = PropertyType.SWITCH, name = "Hardcore Hearts",
        description = "When your bed is broken/wither is killed in Bedwars/The Walls, set the heart style to Hardcore.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hardcoreHearts;

    @Property(
        type = PropertyType.SWITCH, name = "Pit Lag Reducer",
        description = "Hide entities at spawn while you are in the PVP area.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean pitLagReducer;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Game Starting Titles",
        description = "Hide titles such as the countdown when a game is about to begin and gamemode names.\n§eExample: §4§lINSANE MODE",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideGameStartingTitles;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Game Ending Titles",
        description = "Hide titles that signify when the game has ended.\n§eExamples:\n§6§lVICTORY!\n§4§lGAME OVER!",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideGameEndingTitles;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Game Ending Countdown Titles",
        description = "Hide titles that signify the time left in a game.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideGameEndingCountdownTitles;

    @Property(
        type = PropertyType.SWITCH, name = "Height Overlay",
        description = "Make blocks that are in the Hypixel height limit a different colour.\n§eReloads chunks automatically when toggled on and off.\n§4Requires Smooth Lighting.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean heightOverlay;

    @Property(
        type = PropertyType.SLIDER, name = "Height Overlay Tint Multiplier",
        description = "Adjust the tint multiplier.",
        category = "Game", subcategory = "Visual",
        max = 1000
    )
    public static int overlayAmount = 300;

    @Property(
        type = PropertyType.SWITCH, name = "Edit Height Overlay Manually",
        description = "Enabled the option to edit the height overlay manually.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean manuallyEditHeightOverlay;

    @Property(
        type = PropertyType.BUTTON, name = "Manual Height Overlay Editor",
        description = "Click the button to edit the height limit manually.",
        category = "Game", subcategory = "Visual"
    )
    public static void editHeightOverlay() {
       if (manuallyEditHeightOverlay) {
           EssentialAPI.getGuiUtil().openScreen(HytilsReborn.INSTANCE.getBlockConfig().gui());
       } else {
           EssentialAPI.getNotifications().push("Hytils Reborn", "You do not have manual height overlay enabled!");
       }
    }

    @Property(
        type = PropertyType.SWITCH, name = "Hide Duels Cosmetics",
        description = "Hide Duels Cosmetics in Hypixel.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideDuelsCosmetics;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Actionbar in Housing",
        description = "Hide the Actionbar in Housing.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideHousingActionBar;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Non-NPCs in SkyBlock",
        description = "Remove all non-NPC entities (besides yourself) in SkyBlock.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideNonNPCs;

    @Property(
        type = PropertyType.SWITCH, name = "Middle Waypoint Beacon in MiniWalls",
        description = "Adds a beacon at (0,0) when your MiniWither is dead in MiniWalls.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean miniWallsMiddleBeacon;

    @Property(
        type = PropertyType.COLOR, name = "MiniWalls Beacon Color",
        description = "Set the color of the beacon.",
        category = "Game", subcategory = "Visual"
    )
    public static Color miniWallsMiddleBeaconColor = Color.BLUE;

    @Property(
        type = PropertyType.SWITCH, name = "Mute Housing Music",
        description = "Prevent the Housing songs from being heard.",
        category = "Game", subcategory = "Sound"
    )
    public static boolean muteHousingMusic;

    @Property(
        type = PropertyType.SWITCH, name = "Notify When Blocks Run Out",
        description = "Pings you via a sound when your blocks are running out.",
        category = "Game", subcategory = "Sound"
    )
    public static boolean blockNotify;

    @Property(
        type = PropertyType.NUMBER, name = "Block Number",
        description = "Modify the number of blocks you (don't?) have for the Notify When Blocks Run Out feature to work.",
        category = "Game", subcategory = "Sound",
        min = 4, max = 20
    )
    public static int blockNumber = 10;

    @Property(
        type = PropertyType.SELECTOR, name = "Block Notify Sound",
        description = "Choose what sound to play.",
        category = "Game", subcategory = "Sound",
        options = {"Hypixel Ding", "Golem Hit", "Blaze Hit", "Anvil Land", "Horse Death", "Ghast Scream", "Guardian Floop", "Cat Meow", "Dog Bark"}
    )
    public static int blockNotifySound = 0;

    // Lobby

    @Property(
        type = PropertyType.SWITCH, name = "Hide Lobby NPCs",
        description = "Hide NPCs in the lobby.",
        category = "Lobby", subcategory = "Entities"
    )
    public static boolean npcHider;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Useless Lobby Nametags",
        description = "Hides unnecessary nametags such as those that say \"§eRIGHT CLICK§r\" or \"§eCLICK TO PLAY§r\" in a lobby, as well as other useless ones.",
        category = "Lobby", subcategory = "Entities"
    )
    public static boolean hideUselessArmorStands;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Limbo AFK Title",
        description = "Remove the AFK title when you get sent to limbo for being AFK.",
        category = "Lobby", subcategory = "General"
    )
    public static boolean hideLimboTitle;

    @Property(
        type = PropertyType.SWITCH, name = "Limbo Limiter",
        description = "While in Limbo, limit your framerate to reduce the load of the game on your computer.",
        category = "Lobby", subcategory = "General"
    )
    public static boolean limboLimiter;

    @Property(
        type = PropertyType.SLIDER, name = "Limbo Limiter FPS",
        description = "While in Limbo, limit your framerate to reduce the load of the game on your computer.",
        category = "Lobby", subcategory = "General",
        min = 1, max = 60
    )
    public static int limboFPS = 15;

    @Property(
        type = PropertyType.SWITCH, name = "Limbo PM Ding",
        description = "While in Limbo, play the ding sound if you get a PM. Currently, Hypixel's option does not work in Limbo.",
        category = "Lobby", subcategory = "General"
    )
    public static boolean limboDing;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Lobby Bossbars",
        description = "Hide the bossbar in the lobby.",
        category = "Lobby", subcategory = "GUI"
    )
    public static boolean lobbyBossbar;

    @Property(
        type = PropertyType.SWITCH, name = "Mystery Box Star",
        description = "Shows what star a mystery box is in the Mystery Box Vault, Orange stars are special boxes.",
        category = "Lobby", subcategory = "GUI"
    )
    public static boolean mysteryBoxStar;

    @Property(
        type = PropertyType.NUMBER, name = "DO NOT MODIFY THIS VALUE",
        description = "Config version",
        category = "Updater",
        hidden = true
    )
    public static int configNumber = 0;

    public static final ArrayList<String> wbMessages = new ArrayList<>();


    public HytilsConfig() {
        super(new File(HytilsReborn.INSTANCE.modDir, "hytilsreborn.toml"));
        try {
            File modDir = HytilsReborn.INSTANCE.modDir;
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

        if (configNumber != 2) { // Config version has not been set or is outdated
            if (configNumber == 1) {
                overlayAmount = 300;
            }
            configNumber = 2; // set this to the current config version
            markDirty();
            writeData();
        }

        addDependency("autoQueueDelay", "autoQueue");

        addDependency("gexpMode", "autoGetGEXP");

        addDependency("glPhrase", "autoGL");

        addDependency("guildAutoWB", "autoWB");
        addDependency("friendsAutoWB", "autoWB");
        addDependency("autoWBCooldown", "autoWB");
        addDependency("randomAutoWB", "autoWB");

        for (String property : Lists.newArrayList("autoWBMessage1", "autoWBMessage2", "autoWBMessage3", "autoWBMessage4", "autoWBMessage5", "autoWBMessage6", "autoWBMessage7", "autoWBMessage8", "autoWBMessage9", "autoWBMessage10")) {
            registerListener(property, (a) -> setWBMessages());
            addDependency(property, "autoWB");
        }
        setWBMessages();

        addDependency("disableNotifyMiningFatigueSkyblock", "notifyMiningFatigue");

        addDependency("chatSwapperReturnChannel", "chatSwapper");
        addDependency("chattingIntegration", "chatSwapper");
        addDependency("hideAllChatMessage", "chatSwapper");

        addDependency("playerCountBeforePlayerName", "gameStatusRestyle");
        addDependency("playerCountOnPlayerLeave", "gameStatusRestyle");
        addDependency("padPlayerCount", "gameStatusRestyle");

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

        registerListener("heightOverlay", (funny) -> {
            if (funny != null) {
                heightOverlay = (boolean) funny;
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
            }
        });
        registerListener("overlayAmount", (funny) -> {
            if (funny != null) {
                overlayAmount = (int) funny;
                ColorUtils.invalidateCache();
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
            }
        });

        //addDependency("editHeightOverlay", "heightOverlay");
        addDependency("manuallyEditHeightOverlay", "heightOverlay");
        //addDependency("editHeightOverlay", "manuallyEditHeightOverlay");
    }

    public void hideTabulous() {
        Function0<Boolean> yes = () -> true;
        hideNpcsInTab = false;
        keepImportantNpcsInTab = false;
        hideGuildTagsInTab = false;
        hidePlayerRanksInTab = false;
        hidePingInTab = false;
        cleanerSkyblockTabInfo = false;
        markDirty();
        writeData();
        hidePropertyIf("hideNpcsInTab", yes);
        hidePropertyIf("keepImportantNpcsInTab", yes);
        hidePropertyIf("hideGuildTagsInTab", yes);
        hidePropertyIf("hidePlayerRanksInTab", yes);
        hidePropertyIf("hidePingInTab", yes);
        hidePropertyIf("cleanerSkyblockTabInfo", yes);
    }

    private void setWBMessages() {
        wbMessages.clear();
        wbMessages.addAll(Arrays.asList(autoWBMessage1, autoWBMessage2, autoWBMessage3, autoWBMessage4, autoWBMessage5, autoWBMessage6, autoWBMessage7, autoWBMessage8, autoWBMessage9, autoWBMessage10));
    }
}
