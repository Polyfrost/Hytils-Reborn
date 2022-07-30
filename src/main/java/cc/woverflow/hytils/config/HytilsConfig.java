/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
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
        category = "API",
        secure = true
    )
    public static String apiKey = "";

    // Automatic

    @Switch(
        name = "Automatically Get API Key",
        category = "Automatic", subcategory = "API"
    )
    public static boolean autoGetAPI = true;

    @Switch(
        name = "Auto Start",
        category = "Automatic", subcategory = "General"
    )
    public static boolean autoStart;

    @Switch(
        name = "Auto Queue",
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
        category = "Automatic", subcategory = "Game"
    )
    public static boolean autocompletePlayCommands;

    @Switch(
        name = "Limbo Play Helper",
        category = "Automatic", subcategory = "Game"
    )
    public static boolean limboPlayCommandHelper;

    @Switch(
        name = "Auto GL",
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
        category = "Automatic", subcategory = "AutoGL"
    )
    public static boolean antiGL;

    @Switch(
        name = "Auto Friend",
        category = "Automatic", subcategory = "Social"
    )
    public static boolean autoFriend;

    @Switch(
        name = "Automatically Check GEXP",
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
        category = "Automatic", subcategory = "Stats"
    )
    public static boolean autoGetWinstreak;

    // Chat

    @Switch(
        name = "Hide Locraw Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hideLocraw = true;

    @Switch(
        name = "Remove Lobby Statuses",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean lobbyStatus;

    @Switch(
        name = "Remove Mystery Box Rewards",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean mysteryBoxAnnouncer;

    @Switch(
        name = "Remove Soul Well Announcements",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean soulWellAnnouncer;

    @Switch(
        name = "Remove Game Announcements",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean gameAnnouncements;

    @Switch(
        name = "Remove Hype Limit Reminder",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hypeLimitReminder;

    @Switch(
        name = "Player AdBlocker",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean playerAdBlock;

    @Switch(
        name = "Remove BedWars Advertisements",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean bedwarsAdvertisements;

    @Switch(
        name = "Remove Friend/Guild Statuses",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean connectionStatus;

    @Switch(
        name = "Remove Guild MOTD",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean guildMotd;

    @Switch(
        name = "Remove Chat Emojis",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean mvpEmotes;

    @Switch(
        name = "Remove Server Connected Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean serverConnectedMessages;

    @Switch(
        name = "Remove Game Tips Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean gameTipMessages;

    @Switch(
        name = "Remove Auto Activated Quest Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean questsMessages;

    @Switch(
        name = "Remove Stats Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean statsMessages;

    @Switch(
        name = "Remove Curse of Spam Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean curseOfSpam;

    @Switch(
        name = "Remove Bridge Self Goal Death Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean bridgeOwnGoalDeath;

    @Switch(
        name = "Remove Duels No Stats Change Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean duelsNoStatsChange;

    @Switch(
        name = "Remove Block Trail Disabled Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean duelsBlockTrail;

    @Switch(
        name = "Remove SkyBlock Welcome Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean skyblockWelcome;

    @Switch(
        name = "Remove Gift Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean giftBlocker;

    @Switch(
        name = "Remove Seasonal Simulator Collection Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean grinchPresents;

    @Switch(
        name = "Remove Earned Coins and Experience Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean earnedCoinsAndExp;

    @Switch(
        name = "Remove Replay Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean replayMessage;

    @Switch(
        name = "Remove Tip Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean tipMessage;

    @Switch(
        name = "Remove Online Status Messages",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean onlineStatus;

    @Switch(
        name = "Trim Line Separators",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean lineBreakerTrim = true;

    @Switch(
        name = "Clean Line Separators",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean cleanLineSeparator = true;

    @Switch(
        name = "White Chat",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean whiteChat;

    @Switch(
        name = "White Private Messages",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean whitePrivateMessages;

    @Switch(
        name = "Colored Friend/Guild Statuses",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean coloredStatuses;

    @Switch(
        name = "Cleaner Game Start Counter",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean cleanerGameStartAnnouncements;

    @Switch(
        name = "Short Channel Names",
        category = "Chat", subcategory = "Visual"
    )
    public static boolean shortChannelNames;

    @Switch(
        name = "Game Status Restyle",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean gameStatusRestyle;

    @Switch(
        name = "Player Count Before Player Name",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountBeforePlayerName;

    @Switch(
        name = "Player Count on Player Leave",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountOnPlayerLeave;

    @Switch(
        name = "Player Count Padding",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean padPlayerCount;

    @Switch(
        name = "Party Chat Swapper",
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
        category = "Chat", subcategory = "Parties"
    )
    public static boolean chattingIntegration;

    @Switch(
        name = "Remove All Chat Message",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean hideAllChatMessage;

    @Switch(
        name = "Thank Watchdog",
        category = "Chat", subcategory = "Watchdog"
    )
    public static boolean thankWatchdog;

    @Switch(
        name = "Auto Chat Report Confirm",
        category = "Automatic", subcategory = "Chat"
    )
    public static boolean autoChatReportConfirm;

    @Switch(
        name = "Auto Party Warp Confirm",
        category = "Automatic", subcategory = "Chat"
    )
    public static boolean autoPartyWarpConfirm;

    @Switch(
        name = "Guild Welcome Message",
        category = "Chat", subcategory = "Guild"
    )
    public static boolean guildWelcomeMessage;

    // AutoWB

    @Switch(
        name = "AutoWB",
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


    @Switch(
        name = "Shout Cooldown",
        category = "Chat", subcategory = "Cooldown"
    )
    public static boolean preventShoutingOnCooldown;

    @Switch(
        name = "Non Speech Cooldown",
        category = "Chat", subcategory = "Cooldown"
    )
    public static boolean preventNonCooldown;

    // General

    @Switch(
        name = "Notify Mining Fatigue",
        category = "General", subcategory = "Potion Effects"
    )
    public static boolean notifyMiningFatigue;

    @Switch(
        name = "Disable Mining Fatigue Notification in SkyBlock",
        category = "General", subcategory = "Potion Effects"
    )
    public static boolean disableNotifyMiningFatigueSkyblock;

    @Switch(
        name = "Hide NPCs in Tab",
        category = "General", subcategory = "Tab"
    )
    public static boolean hideNpcsInTab;

    @Switch(
        name = "Don't Hide Important NPCs",
        category = "General", subcategory = "Tab"
    )
    public static boolean keepImportantNpcsInTab;

    @Switch(
        name = "Hide Guild Tags in Tab",
        category = "General", subcategory = "Tab"
    )
    public static boolean hideGuildTagsInTab;

    @Switch(
        name = "Hide Player Ranks in Tab",
        category = "General", subcategory = "Tab"
    )
    public static boolean hidePlayerRanksInTab;

    @Dropdown(
        name = "Highlight Friends In Tab",
        category = "General", subcategory = "Tab",
        options = {"Off", "Left of Name", "Right of Name"}
    )
    public static int highlightFriendsInTab;

    @Dropdown(
        name = "Highlight Self In Tab",
        category = "General", subcategory = "Tab",
        options = {"Off", "Left of Name", "Right of Name"}
    )
    public static int highlightSelfInTab;

    @Switch(
        name = "Cleaner Tab in SkyBlock",
        category = "General", subcategory = "Tab"
    )
    public static boolean cleanerSkyblockTabInfo;

    @Switch(
        name = "Hide Ping in Tab",
        category = "General", subcategory = "Tab"
    )
    public static boolean hidePingInTab;

    @Switch(
        name = "Broadcast Achievements",
        category = "General", subcategory = "Guilds"
    )
    public static boolean broadcastAchievements;

    @Switch(
        name = "Broadcast Levelup",
        category = "General", subcategory = "Guilds"
    )
    public static boolean broadcastLevelup;

    // Game

    @Switch(
        name = "Notify When Kicked From Game",
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
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideArmor;

    @Switch(
        name = "Hide Useless Game Nametags",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideUselessArmorStandsGame;

    @Switch(
        name = "Hardcore Hearts",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hardcoreHearts;

    @Switch(
        name = "Pit Lag Reducer",
        category = "Game", subcategory = "Visual"
    )
    public static boolean pitLagReducer;

    @Switch(
        name = "Hide Game Starting Titles",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideGameStartingTitles;

    @Switch(
        name = "Hide Game Ending Titles",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideGameEndingTitles;

    @Switch(
        name = "Hide Game Ending Countdown Titles",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideGameEndingCountdownTitles;

    @Switch(
        name = "Height Overlay",
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
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideDuelsCosmetics;

    @Switch(
        name = "Hide Actionbar in Housing",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideHousingActionBar;

    @Switch(
        name = "Remove Non-NPCs in SkyBlock",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideNonNPCs;

    @Switch(
        name = "Middle Waypoint Beacon in MiniWalls",
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
        category = "Game", subcategory = "Sound"
    )
    public static boolean muteHousingMusic;

    @Switch(
        name = "Notify When Blocks Run Out",
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
        category = "Lobby", subcategory = "Entities"
    )
    public static boolean npcHider;

    @Switch(
        name = "Hide Useless Lobby Nametags",
        category = "Lobby", subcategory = "Entities"
    )
    public static boolean hideUselessArmorStands;

    @Switch(
        name = "Remove Limbo AFK Title",
        category = "Lobby", subcategory = "General"
    )
    public static boolean hideLimboTitle;

    @Switch(
        name = "Limbo Limiter",
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
        category = "Lobby", subcategory = "General"
    )
    public static boolean limboDing;

    @Switch(
        name = "Hide Lobby Bossbars",
        category = "Lobby", subcategory = "GUI"
    )
    public static boolean lobbyBossbar;

    @Switch(
        name = "Mystery Box Star",
        category = "Lobby", subcategory = "GUI"
    )
    public static boolean mysteryBoxStar;

    public static int configNumber = 0;

    public static final ArrayList<String> wbMessages = new ArrayList<>();


    public HytilsConfig() {
        super(new Mod("Hytils Reborn", ModType.UTIL_QOL, new VigilanceMigrator(new File(HytilsReborn.INSTANCE.modDir, "hytilsreborn.toml").getAbsolutePath())), "hytilsreborn.json");
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
        addDependency("hideNpcsInTab", false);
        addDependency("keepImportantNpcsInTab", false);
        addDependency("hideGuildTagsInTab", false);
        addDependency("hidePlayerRanksInTab", false);
        addDependency("hidePingInTab", false);
        addDependency("cleanerSkyblockTabInfo", false);
    }

    private void setWBMessages() {
        wbMessages.clear();
        wbMessages.addAll(Arrays.asList(autoWBMessage1, autoWBMessage2, autoWBMessage3, autoWBMessage4, autoWBMessage5, autoWBMessage6, autoWBMessage7, autoWBMessage8, autoWBMessage9, autoWBMessage10));
    }
}
