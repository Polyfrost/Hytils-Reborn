/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
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

package cc.woverflow.hytils.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.util.DarkColorUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import java.awt.Color;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

public class HytilsConfig extends Config {

    // API

    @Text(
        name = "API Key",
        description = "Automatically get the API Key from /api new.",
        category = "API",
        secure = true
    )
    @HypixelKey
    public static String apiKey = "";

    // Automatic

    @Switch(
        name = "Auto Start",
        description = "Join Hypixel immediately once the client has loaded to the main menu.",
        category = "Automatic", subcategory = "General"
    )
    public static boolean autoStart;

    @Switch(
        name = "Auto Queue",
        description = "Automatically queues for another game once you win or die. (This will require you to interact with the game in a way to prevent abuse)",
        category = "Automatic", subcategory = "Game"
    )
    public static boolean autoQueue;

    @Slider(
        name = "Auto Queue Delay",
        category = "Automatic", subcategory = "Game",
        min = 0, max = 10
    )
    public static int autoQueueDelay;

    @Switch(
        name = "Auto-Complete Play Commands",
        description = "Allows tab completion of /play commands.",
        category = "Automatic", subcategory = "Game"
    )
    public static boolean autocompletePlayCommands;

    @Switch(
        name = "Limbo Play Helper",
        description = "When a /play command is run in Limbo, this runs /l first and then the command.",
        category = "Automatic", subcategory = "Game"
    )
    public static boolean limboPlayCommandHelper;

    @Switch(
        name = "Auto GL",
        description = "Send a message 5 seconds before a Hypixel game starts.",
        category = "Automatic", subcategory = "AutoGL"
    )
    public static boolean autoGL;

    @Dropdown(
        name = "Auto GL Phrase",
        category = "Automatic", subcategory = "AutoGL",
        options = {"glhf", "Good Luck", "GL", "Have a good game!", "gl", "Good luck!"}
    )
    public static int glPhrase = 0;

    @Switch(
        name = "Anti GL",
        description = "Remove all GL messages from chat.",
        category = "Automatic", subcategory = "AutoGL"
    )
    public static boolean antiGL;

    @Switch(
        name = "Auto Friend",
        description = "Automatically accept friend requests.",
        category = "Automatic", subcategory = "Social"
    )
    public static boolean autoFriend;

    @Switch(
        name = "Automatically Check GEXP",
        description = "Automatically check your GEXP after you win a Hypixel game.",
        category = "Automatic", subcategory = "Stats"
    )
    public static boolean autoGetGEXP;

    @DualOption(
        name = "GEXP Mode",
        category = "Automatic", subcategory = "Stats",
        left = "Daily",
        right = "Weekly"
    )
    public static boolean gexpMode = false;

    @Switch(
        name = "Automatically Check Winstreak",
        description = "Automatically check your winstreak after you win a Hypixel game.",
        category = "Automatic", subcategory = "Stats"
    )
    public static boolean autoGetWinstreak;

    // Chat

    @Switch(
        name = "Hide Locraw Messages",
        description = "Hide Locraw Messages.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hideLocraw = true;

    @Switch(
        name = "Remove Lobby Statuses",
        description = "Remove lobby join messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean lobbyStatus;

    @Switch(
        name = "Remove Mystery Box Rewards",
        description = "Remove others mystery box messages from chat and only show your own.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean mysteryBoxAnnouncer;

    @Switch(
        name = "Remove Soul Well Announcements",
        description = "Remove soul well announcements from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean soulWellAnnouncer;

    @Switch(
        name = "Remove Game Announcements",
        description = "Remove game announcements from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean gameAnnouncements;

    @Switch(
        name = "Remove Hype Limit Reminder",
        description = "Remove Hype limit reminders from chat.",
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
        description = "Remove player messages asking to join BedWars parties.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean bedwarsAdvertisements;

    @Switch(
        name = "Remove Friend/Guild Statuses",
        description = "Remove join/quit messages from friend/guild members.",
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
        description = "Remove MVP++ chat emojis.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean mvpEmotes;

    @Switch(
        name = "Remove Server Connected Messages",
        description = "Remove messages informing you of the lobby name you've just joined, or what lobby you're being sent to.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean serverConnectedMessages;

    @Switch(
        name = "Remove Game Tips Messages",
        description = "Remove messages informing you of how to play the game you're currently in.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean gameTipMessages;

    @Switch(
        name = "Remove Auto Activated Quest Messages",
        description = "Remove automatically activated quest messages.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean questsMessages;

    @Switch(
        name = "Remove Stats Messages",
        description = "Remove messages informing you if you want to view your stats after a game.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean statsMessages;

    @Switch(
        name = "Remove Curse of Spam Messages",
        description = "Hides the constant spam of Kali's curse of spam.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean curseOfSpam;

    @Switch(
        name = "Remove Bridge Self Goal Death Messages",
        description = "Hides the death message when you jump into your own goal in Bridge.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean bridgeOwnGoalDeath;

    @Switch(
        name = "Remove Duels No Stats Change Messages",
        description = "Hides the message explaining that your stats did not change for dueling through /duel or within in a party.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean duelsNoStatsChange;

    @Switch(
        name = "Remove Block Trail Disabled Messages",
        description = "Hides the message explaining that your duel's block trail cosmetic was disabled in specific gamemodes.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean duelsBlockTrail;

    @Switch(
        name = "Remove SkyBlock Welcome Messages",
        description = "Removes \"Welcome to Hypixel SkyBlock!\" messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean skyblockWelcome;

    @Switch(
        name = "Remove Gift Messages",
        description = "Removes \"They have gifted x so far!\" messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean giftBlocker;

    @Switch(
        name = "Remove Seasonal Simulator Collection Messages",
        description = "Removes personal and global collected messages from chat for the Easter, Christmas, and Halloween variants.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean grinchPresents;

    @Switch(
        name = "Remove Earned Coins and Experience Messages",
        description = "Removes the earned coins and experience messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean earnedCoinsAndExp;

    @Switch(
        name = "Remove Replay Messages",
        description = "Removes replay messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean replayMessage;

    @Switch(
        name = "Remove Tip Messages",
        description = "Removes tip messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean tipMessage;

    @Switch(
        name = "Remove Online Status Messages",
        description = "Removes the online status messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean onlineStatus;

    @Switch(
        name = "Remove Main Lobby Fishing Announcements",
        description = "Removes Main Lobby Fishing announcements from chat when a player catches a special fish.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean lobbyFishing;

    @Switch(
        name = "Remove Hot Potato Messages",
        description = "Removes Hot Potato messages from chat.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hotPotato;

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
    public static boolean coloredStatuses;

    @Switch(
        name = "Cleaner Game Start Counter",
        description = "Compacts game start announcements.",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean cleanerGameStartAnnouncements;

    @Switch(
        name = "Short Channel Names",
        description = "Abbreviate chat channel names.",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean shortChannelNames;

    @Switch(
        name = "Game Status Restyle",
        description = "Replace common game status messages with a new style.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean gameStatusRestyle;

    @Switch(
        name = "Player Count Before Player Name",
        description = "Put the player count before the player name in game join/leave messages.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountBeforePlayerName;

    @Switch(
        name = "Player Count on Player Leave",
        description = "Include the player count when players leave.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountOnPlayerLeave;

    @Switch(
        name = "Player Count Padding",
        description = "Place zeros at the beginning of the player count to align with the max player count.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean padPlayerCount;

    @Switch(
        name = "Party Chat Swapper",
        description = "Automatically change to and out of a party channel when joining/leaving a party.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean chatSwapper;

    @Dropdown(
        name = "Chat Swapper Channel",
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

    @Switch(
        name = "Remove All Chat Message",
        description = "Hide the \"You are now in the ALL channel\" message when auto-switching.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean hideAllChatMessage;

    @Switch(
        name = "Thank Watchdog",
        description = "Compliment Watchdog when someone is banned, or a Watchdog announcement is sent.",
        category = "Chat", subcategory = "Watchdog"
    )
    public static boolean thankWatchdog;

    @Switch(
        name = "Auto Chat Report Confirm",
        description = "Automatically confirms chat reports.",
        category = "Automatic", subcategory = "Chat"
    )
    public static boolean autoChatReportConfirm;

    @Switch(
        name = "Auto Party Warp Confirm",
        description = "Automatically confirms party warps.",
        category = "Automatic", subcategory = "Chat"
    )
    public static boolean autoPartyWarpConfirm;

    @Switch(
        name = "Guild Welcome Message",
        description = "Send a friendly welcome message when a player joins your guild.",
        category = "Chat", subcategory = "Guild"
    )
    public static boolean guildWelcomeMessage;

    @Switch(
        name = "Shout Cooldown",
        description = "Show the amount of time remaining until /shout can be reused.",
        category = "Chat", subcategory = "Cooldown"
    )
    public static boolean preventShoutingOnCooldown;

    @Switch(
        name = "Non Speech Cooldown",
        description = "Show the amount of time remaining until you can speak if you are a non.",
        category = "Chat", subcategory = "Cooldown"
    )
    public static boolean preventNonCooldown;

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

    // General

    @Switch(
        name = "Notify Mining Fatigue",
        description = "Send a notification when you get mining fatigue.",
        category = "General", subcategory = "Potion Effects"
    )
    public static boolean notifyMiningFatigue;

    @Switch(
        name = "Disable Mining Fatigue Notification in SkyBlock",
        description = "Disable the mining fatigue notification in SkyBlock.",
        category = "General", subcategory = "Potion Effects"
    )
    public static boolean disableNotifyMiningFatigueSkyblock;

    @Switch(
        name = "Hide NPCs in Tab",
        description = "Prevent NPCs from showing up in tab.",
        category = "General", subcategory = "Tab"
    )
    public static boolean hideNpcsInTab;

    @Switch(
        name = "Don't Hide Important NPCs",
        description = "Keeps NPCs in tab in gamemodes like SkyBlock and Replay.",
        category = "General", subcategory = "Tab"
    )
    public static boolean keepImportantNpcsInTab;

    @Switch(
        name = "Hide Guild Tags in Tab",
        description = "Prevent Guild tags from showing up in tab.",
        category = "General", subcategory = "Tab"
    )
    public static boolean hideGuildTagsInTab;

    @Switch(
        name = "Hide Player Ranks in Tab",
        description = "Prevent player ranks from showing up in tab.",
        category = "General", subcategory = "Tab"
    )
    public static boolean hidePlayerRanksInTab;

    @Dropdown(
        name = "Highlight Friends In Tab",
        description = "Add a star to the names of your Hypixel friends in tab.",
        category = "General", subcategory = "Tab",
        options = {"Off", "Left of Name", "Right of Name"}
    )
    public static int highlightFriendsInTab;

    @Dropdown(
        name = "Highlight Self In Tab",
        description = "Add a star to your name in tab.",
        category = "General", subcategory = "Tab",
        options = {"Off", "Left of Name", "Right of Name"}
    )
    public static int highlightSelfInTab;

    @Switch(
        name = "Cleaner Tab in SkyBlock",
        description = "Doesn't render player heads or ping for tab entries that aren't players in SkyBlock.",
        category = "General", subcategory = "Tab"
    )
    public static boolean cleanerSkyblockTabInfo;

    @Switch(
        name = "Hide Ping in Tab",
        description = "Prevent ping from showing up in tab while playing games, since the value is misleading. Ping will remain visible in lobbies.",
        category = "General", subcategory = "Tab"
    )
    public static boolean hidePingInTab;

    @Switch(
        name = "Broadcast Achievements",
        description = "Announce in Guild chat when you get an achievement.",
        category = "General", subcategory = "Guilds"
    )
    public static boolean broadcastAchievements;

    @Switch(
        name = "Broadcast Levelup",
        description = "Announce in Guild chat when you level up.",
        category = "General", subcategory = "Guilds"
    )
    public static boolean broadcastLevelup;

    // Game

    @Switch(
        name = "Notify When Kicked From Game",
        description = "Notify in party chat when you are kicked from the game due to a connection issue.",
        category = "Game", subcategory = "Chat"
    )
    public static boolean notifyWhenKick;

    @Switch(
        name = "Put Notify Message In Capital Letters",
        category = "Game", subcategory = "Chat"
    )
    public static boolean putInCaps;

    @Switch(
        name = "Highlight Opened Chests",
        description = "Highlight chests that have been opened.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean highlightChests;

    @cc.polyfrost.oneconfig.config.annotations.Color(
        name = "Highlight Color",
        category = "Game", subcategory = "Visual"
    )
    public static OneColor highlightChestsColor = new OneColor(Color.RED);

    @Switch(
        name = "UHC Overlay",
        description = "Increase the size of dropped apples, golden apples, golden ingots, and player heads in UHC Champions and Speed UHC.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean uhcOverlay;

    @Slider(
        name = "UHC Overlay Multiplier",
        category = "Game", subcategory = "Visual",
        min = 1f, max = 5f
    )
    public static float uhcOverlayMultiplier = 1f;

    @Switch(
        name = "UHC Middle Waypoint",
        description = "Adds a waypoint to signify (0,0).",
        category = "Game", subcategory = "Visual"
    )
    public static boolean uhcMiddleWaypoint;

    @Text(
        name = "UHC Middle Waypoint Text",
        category = "Game", subcategory = "Visual"
    )
    public static String uhcMiddleWaypointText = "0,0";

    @Switch(
        name = "Lower Render Distance in Sumo",
        description = "Lowers render distance to your desired value in Sumo Duels.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean sumoRenderDistance;

    @Slider(
        name = "Sumo Render Distance",
        category = "Game", subcategory = "Visual",
        min = 1, max = 5
    )
    public static int sumoRenderDistanceAmount = 2;

    @Switch(
        name = "Hide Armor",
        description = "Hide armor in games where armor is always the same.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideArmor;

    @Switch(
        name = "Hide Useless Game Nametags",
        description = "Hides unnecessary nametags such as those that say \"RIGHT CLICK\" or \"CLICK\" in SkyBlock, BedWars, SkyWars, and Duels, as well as other useless ones.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideUselessArmorStandsGame;

    @Switch(
        name = "Hardcore Hearts",
        description = "When your bed is broken/wither is killed in Bedwars/The Walls, set the heart style to Hardcore.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hardcoreHearts;

    @Switch(
        name = "Pit Lag Reducer",
        description = "Hide entities at spawn while you are in the PVP area.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean pitLagReducer;

    @Switch(
        name = "Hide Game Starting Titles",
        description = "Hide titles such as the countdown when a game is about to begin and gamemode names.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideGameStartingTitles;

    @Switch(
        name = "Hide Game Ending Titles",
        description = "Hide titles that signify when the game has ended.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideGameEndingTitles;

    @Switch(
        name = "Hide Game Ending Countdown Titles",
        description = "Hide titles that signify the time left in a game.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideGameEndingCountdownTitles;

    @Switch(
        name = "Height Overlay",
        description = "Make blocks that are in the Hypixel height limit a different color.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean heightOverlay;

    @Slider(
        name = "Height Overlay Tint Multiplier",
        category = "Game", subcategory = "Visual",
        min = 1, max = 1000
    )
    public static int overlayAmount = 300;

    @Switch(
        name = "Edit Height Overlay Manually",
        category = "Game", subcategory = "Visual"
    )
    public static boolean manuallyEditHeightOverlay;

    @Page(
        name = "Manual Height Overlay Editor",
        location = PageLocation.BOTTOM,
        category = "Game", subcategory = "Visual"
    )
    public static BlockHighlightConfig blockHighlightConfig = new BlockHighlightConfig();

    @Switch(
        name = "Hide Duels Cosmetics",
        description = "Hide Duels Cosmetics in Hypixel.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideDuelsCosmetics;

    @Switch(
        name = "Hide Actionbar in Housing",
        description = "Hide the Actionbar in Housing.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideHousingActionBar;

    @Switch(
        name = "Hide Actionbar in Dropper",
        description = "Hide the Actionbar in Dropper.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideDropperActionBar;

    @Switch(
        name = "Remove Non-NPCs in SkyBlock",
        description = "Remove entities that are not NPCs in SkyBlock.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideNonNPCs;

    @Switch(
        name = "Middle Waypoint Beacon in MiniWalls",
        description = "Adds a beacon at (0,0) when your MiniWither is dead in MiniWalls.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean miniWallsMiddleBeacon;

    @cc.polyfrost.oneconfig.config.annotations.Color(
        name = "MiniWalls Beacon Color",
        category = "Game", subcategory = "Visual"
    )
    public static OneColor miniWallsMiddleBeaconColor = new OneColor(Color.BLUE);

    @Switch(
        name = "Mute Housing Music",
        description = "Prevent the Housing songs from being heard.",
        category = "Game", subcategory = "Sound"
    )
    public static boolean muteHousingMusic;

    @Switch(
        name = "Notify When Blocks Run Out",
        description = "Pings you via a sound when your blocks are running out.",
        category = "Game", subcategory = "Sound"
    )
    public static boolean blockNotify;

    @Slider(
        name = "Block Number",
        category = "Game", subcategory = "Sound",
        min = 4, max = 20
    )
    public static int blockNumber = 10;

    @Dropdown(
        name = "Block Notify Sound",
        category = "Game", subcategory = "Sound",
        options = {"Hypixel Ding", "Golem Hit", "Blaze Hit", "Anvil Land", "Horse Death", "Ghast Scream", "Guardian Floop", "Cat Meow", "Dog Bark"}
    )
    public static int blockNotifySound = 0;

    // Lobby

    @Switch(
        name = "Hide Lobby NPCs",
        description = "Hide NPCs in the lobby.",
        category = "Lobby", subcategory = "Entities"
    )
    public static boolean npcHider;

    @Switch(
        name = "Hide Useless Lobby Nametags",
        description = "Hides unnecessary nametags such as those that say \"RIGHT CLICK\" or \"CLICK TO PLAY\" in a lobby, as well as other useless ones.",
        category = "Lobby", subcategory = "Entities"
    )
    public static boolean hideUselessArmorStands;

    @Switch(
        name = "Remove Limbo AFK Title",
        description = "Remove the AFK title when you get sent to limbo for being AFK.",
        category = "Lobby", subcategory = "General"
    )
    public static boolean hideLimboTitle;

    @Switch(
        name = "Limbo Limiter",
        description = "While in Limbo, limit your framerate to reduce the load of the game on your computer.",
        category = "Lobby", subcategory = "General"
    )
    public static boolean limboLimiter;

    @Slider(
        name = "Limbo Limiter FPS",
        category = "Lobby", subcategory = "General",
        min = 1, max = 60
    )
    public static int limboFPS = 15;

    @Switch(
        name = "Limbo PM Ding",
        description = "While in Limbo, play the ding sound if you get a PM. Currently, Hypixel's option does not work in Limbo.",
        category = "Lobby", subcategory = "General"
    )
    public static boolean limboDing;

    @Switch(
        name = "Hide Lobby Bossbars",
        description = "Hide the bossbar in the lobby.",
        category = "Lobby", subcategory = "GUI"
    )
    public static boolean lobbyBossbar;

    @Switch(
        name = "Mystery Box Star",
        description = "Shows what star a mystery box is in the Mystery Box Vault, Orange stars are special boxes.",
        category = "Lobby", subcategory = "GUI"
    )
    public static boolean mysteryBoxStar;

    public static int configNumber = 0;

    @Exclude
    public static final ArrayList<String> wbMessages = new ArrayList<>();


    public HytilsConfig() {
        super(new Mod("Hytils Reborn", ModType.HYPIXEL, new VigilanceMigrator(new File(HytilsReborn.INSTANCE.modDir, "hytilsreborn.toml").getAbsolutePath())), "hytilsreborn.json");
        initialize();
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

        if (configNumber != 2) { // Config version has not been set or is outdated
            if (configNumber == 1) {
                overlayAmount = 300;
            }
            configNumber = 2; // set this to the current config version
            save();
        }

        addDependency("autoQueueDelay", "autoQueue");

        addDependency("gexpMode", "autoGetGEXP");

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

        addListener("heightOverlay", () -> Minecraft.getMinecraft().renderGlobal.loadRenderers());
        addListener("overlayAmount", () -> {
            DarkColorUtils.invalidateCache();
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        });

        //addDependency("editHeightOverlay", "heightOverlay");
        addDependency("manuallyEditHeightOverlay", "heightOverlay");
        //addDependency("editHeightOverlay", "manuallyEditHeightOverlay");
    }

    public void hideTabulous() {
        hideNpcsInTab = false;
        keepImportantNpcsInTab = false;
        hideGuildTagsInTab = false;
        hidePlayerRanksInTab = false;
        hidePingInTab = false;
        cleanerSkyblockTabInfo = false;
        save();
        addDependency("hideNpcsInTab", () -> false);
        addDependency("keepImportantNpcsInTab", () -> false);
        addDependency("hideGuildTagsInTab", () -> false);
        addDependency("hidePlayerRanksInTab", () -> false);
        addDependency("hidePingInTab", () -> false);
        addDependency("cleanerSkyblockTabInfo", () -> false);
    }

    private void setWBMessages() {
        wbMessages.clear();
        wbMessages.addAll(Arrays.asList(autoWBMessage1, autoWBMessage2, autoWBMessage3, autoWBMessage4, autoWBMessage5, autoWBMessage6, autoWBMessage7, autoWBMessage8, autoWBMessage9, autoWBMessage10));
    }
}
