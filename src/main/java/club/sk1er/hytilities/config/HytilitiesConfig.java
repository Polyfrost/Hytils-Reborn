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

package club.sk1er.hytilities.config;

import club.sk1er.vigilance.Vigilant;
import club.sk1er.vigilance.data.Property;
import club.sk1er.vigilance.data.PropertyType;

import java.io.File;

@SuppressWarnings("unused")
public class HytilitiesConfig extends Vigilant {

    @Property(
        type = PropertyType.SWITCH, name = "Player AdBlocker",
        description = "Remove spam messages from players, usually advertising something.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean playerAdBlock;

    @Property(
        type = PropertyType.SWITCH, name = "Trim Line Separators",
        description = "Prevent separators from overflowing onto the next chat line.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean lineBreakerTrim;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Lobby Statuses",
        description = "Remove lobby join messages from chat.\n§eExample: §b[MVP§c+§b] Steve §6joined the lobby!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean lobbyStatus;

    @Property(
        type = PropertyType.SWITCH, name = "Limbo /play helper",
        description = "When a /play command is run in Limbo, this runs /l first and then the command.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean limboPlayCommandHelper;

    @Property(
        type = PropertyType.SWITCH, name = "Show Personal Mystery Box Rewards",
        description = "Remove others mystery box messages from chat.\n§eExample: §b[Mystery Box] Steve §ffound a §6Legendary Hype Train Gadget§f!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean mysteryBoxAnnouncer;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Soul Well Announcements",
        description = "Remove soul well announcements from chat.\n§eExample: §bSteve §7has found a §6Bulldozer Perk I (Insane) §7in the §bSoul Well§7!",
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
        type = PropertyType.SWITCH, name = "Remove BedWars Advertisements",
        description = "Remove player messages asking to join BedWars parties.\n§eExample: §7[1✫] §b[MVP§c+§b] Steve§f: guys pls join bedwars party 2/4 :)",
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
        description = "Remove server connection messages.\n§eExample: §bYou are currently connected to server §6mini104H§b.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean serverConnectedMessages;

    @Property(
        type = PropertyType.SWITCH, name = "White Chat",
        description = "Make nons chat messages appear as the normal chat message color.\n§eExample: §7Steve: hey! §e-> §fSteve: hey!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean whiteChat;

    @Property(
        type = PropertyType.SWITCH, name = "White Private Messages",
        description = "Make private messages appear as the normal chat message color.\n§eExample: §dFrom §b[MVP§c+§b] Steve: §7hey! §e-> §dFrom §b[MVP§c+§b] Steve: §fhey!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean whitePrivateMessages;

    @Property(
        type = PropertyType.SWITCH, name = "Cleaner Game Start Counter",
        description = "Compacts game start announcements.\n§eExample: The game starts in 20 seconds!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean cleanerGameStartAnnouncements;

    @Property(
        type = PropertyType.SWITCH, name = "Game Status Restyle",
        description = "Replace common game status messages with a new style.\n§eExamples:\n§a§l+ §bSteve §e(§b1§e/§b12§e)\n§c§l- §bSteve §e(§b1§e/§b12§e)\n§e§l* §aGame starts in §b§l5 §aseconds.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean gameStatusRestyle;

    @Property(
        type = PropertyType.SWITCH, name = "Player Count Before Player Name",
        description = "Put the player count before the player name in game join/leave messages.\n§eExample: §e(§b1§e/§b12§e) §bSteve §ehas joined!",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountBeforePlayerName;

    @Property(
        type = PropertyType.SWITCH, name = "Player Count On Player Leave",
        description = "Include the player count when players leave.\n§eExample: §bSteve §ehas quit (§b1§e/§b12§e)!",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountOnPlayerLeave;

    @Property(
        type = PropertyType.SWITCH, name = "Player Count Padding",
        description = "Place zeros at the beginning of the player count to align with the max player count.\n§eExample: §bSteve §ehas joined §e(§b001§e/§b111§e)!",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean padPlayerCount;

    @Property(
        type = PropertyType.SWITCH, name = "Short Channel Names",
        description = "Abbreviate channel names.\n§eExample: §2Guild §e-> §2G§e, §9Party §e-> §9P§e, §dFriend §e-> §dF",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean shortChannelNames;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Lobby NPCs",
        description = "Hide NPCs in the lobby.",
        category = "Lobby", subcategory = "Entities"
    )
    public static boolean npcHider;

    @Property(
        type = PropertyType.SWITCH, name = "Hide NPCs In Tab",
        description = "Prevent NPCs from showing up in tab.",
        category = "General", subcategory = "Entities"
    )
    public static boolean hideNpcsInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Guild Tags In Tab",
        description = "Prevent Guild tags from showing up in tab.",
        category = "General", subcategory = "Guilds"
    )
    public static boolean hideGuildTagsInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Player Ranks In Tab",
        description = "Prevent player ranks from showing up in tab.",
        category = "General", subcategory = "General"
    )
    public static boolean hidePlayerRanksInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Cleaner Tab in Skyblock",
        description = "Doesn't render player heads or ping for tab entries that aren't players in Skyblock.",
        category = "General", subcategory = "General"
    )
    public static boolean cleanerSkyblockTabInfo;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Ping In Tab",
        description = "Prevent ping from showing up in tab while playing games, since the value is misleading. Ping will remain visible in lobbies.",
        category = "General", subcategory = "General"
    )
    public static boolean hidePingInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Limbo Limiter",
        description = "While in Limbo, limit your framerate to 15 to reduce the load of the game on your computer.",
        category = "Lobby", subcategory = "General"
    )
    public static boolean limboLimiter;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Lobby Bossbars",
        description = "Hide the bossbar in the lobby.",
        category = "Lobby", subcategory = "GUI"
    )
    public static boolean lobbyBossbar;

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

    @Property(
        type = PropertyType.SWITCH, name = "Auto Start",
        description = "Join Hypixel immediately once the client has loaded to the main menu.",
        category = "General", subcategory = "General"
    )
    public static boolean autoStart;

    @Property(
        type = PropertyType.SWITCH, name = "Chat Swapper",
        description = "Automatically change back to a selected channel when leaving a party.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean chatSwapper;

    @Property(
        type = PropertyType.SELECTOR, name = "Chat Swapper Channel",
        description = "The channel to return to when leaving a party.\n§eRequires Chat Swapper.",
        category = "Chat", subcategory = "Parties",
        options = {"ALL", "GUILD", "OFFICER"}
    )
    public static int chatSwapperReturnChannel;

    @Property(
        type = PropertyType.SWITCH, name = "Auto Queue",
        description = "Automatically queues for another game once you die.\n§eThis will require you to interact with the game in a way to prevent abuse.",
        category = "General", subcategory = "Queue"
    )
    public static boolean autoQueue;

    @Property(
        type = PropertyType.SLIDER, name = "Auto Queue Delay",
        description = "Delays the execution of Auto Queue. (The measurement is in seconds)",
        category = "General", subcategory = "Queue",
        max = 10
    )
    public static int autoQueueDelay;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Armour",
        description = "Hide armour in games where armour is always the same.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideArmour;

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
        type = PropertyType.SWITCH, name = "Game Countdown Timer",
        description = "Hide the displayed title text when a game is about to begin.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideGameCountdown;

    @Property(
        type = PropertyType.SWITCH, name = "Mute Housing Music",
        description = "Prevent the Housing songs from being heard.",
        category = "Game", subcategory = "Sound"
    )
    public static boolean muteHousingMusic;

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
        type = PropertyType.SWITCH, name = "Guild Welcome Message",
        description = "Send a friendly welcome message when a player joins your guild.\n§eExample: §fWelcome to the guild Steve!",
        category = "Chat", subcategory = "Guild"
    )
    public static boolean guildWelcomer;

    @Property(
        type = PropertyType.SWITCH, name = "Shout Cooldown",
        description = "Show the amount of time remaining until /shout can be reused.\n§eExample: §eShout command is on cooldown. Please wait 30 more seconds.",
        category = "Chat", subcategory = "Shout"
    )
    public static boolean preventShoutingOnCooldown;

    @Property(
        type = PropertyType.SWITCH, name = "Mystery Box Star",
        description = "Shows what star a mystery box is in the Mystery Box Vault, Orange stars are special boxes.",
        category = "Lobby", subcategory = "GUI"
    )
    public static boolean mysteryBoxStar;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Curse of Spam Messages",
        description = "Hides the constant spam of kali's curse of spam.\n§eExample: §eKALI HAS STRIKEN YOU WITH THE CURSE OF SPAM",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean curseOfSpam;

    @Property(
        type = PropertyType.SWITCH, name = "AutoComplete /play commands",
        description = "Allows tab completion of /play commands",
        category = "Chat", subcategory = "AutoComplete"
    )
    public static boolean autocompletePlayCommands;

    public HytilitiesConfig() {
        super(new File("./config/hytilities.toml"));
        initialize();
    }
}
