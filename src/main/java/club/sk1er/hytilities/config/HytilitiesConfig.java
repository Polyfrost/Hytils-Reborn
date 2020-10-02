package club.sk1er.hytilities.config;

import club.sk1er.vigilance.Vigilant;
import club.sk1er.vigilance.data.Property;
import club.sk1er.vigilance.data.PropertyType;

import java.io.File;

public class HytilitiesConfig extends Vigilant {

    @Property(
        type = PropertyType.SWITCH, name = "Player AdBlocker",
        description = "Remove spam messages from players, usually advertising something.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean playerAdBlock;

    /*@Property(
        type = PropertyType.SWITCH, name = "Remove Line Separators",
        description = "Remove separators from chat.\n§eExample: §9§m--------------",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hytilitiesLineBreaker;*/

    @Property(
        type = PropertyType.SWITCH, name = "Remove Lobby Statuses",
        description = "Remove lobby join messages from chat.\n§eExample: §b[MVP§c+§b] Steve §6joined the lobby!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean lobbyStatus;

    @Property(
        type = PropertyType.SWITCH, name = "Show Own Mystery Box Rewards",
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
        type = PropertyType.SWITCH, name = "Remove Chat Emojis",
        description = "Remove MVP++ chat emojis.\n§eExample: §c§lOOF ",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean mvpEmotes;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Server Connected Messages",
        description = "Remove server Connected messages.\n§eExample: §bYou are currently connected to server §6mini104H",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean serverConnectedMessages;

    @Property(
        type = PropertyType.SWITCH, name = "White Chat",
        description = "Make nons appear as normal chat messages.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean whiteChat;

    @Property(
        type = PropertyType.SWITCH, name = "Game Status Restyle",
        description = "Replace common game status messages with a new style.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean gameStatusRestyle;

    @Property(
        type = PropertyType.SWITCH, name = "Player Count Before Player Name",
        description = "Put player count before player name in game join/leave messages.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountBeforePlayerName;

    @Property(
        type = PropertyType.SWITCH, name = "Show Player Count on Player Leave",
        description = "Include the player count when players leave.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean playerCountOnPlayerLeave;

    @Property(
        type = PropertyType.SWITCH, name = "Zero-Pad Player Counts",
        description = "Places zeros at the beginning of player counts to align with the max player count.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean padPlayerCount;

    @Property(
            type = PropertyType.SWITCH, name = "Short Channel Names",
            description = "Shortens channel names. \n§eExample:  Guild -> G, Party -> P, Friend -> F",
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
        type = PropertyType.SWITCH, name = "Hide NPCs in tab",
        description = "Remove NPCs from ever showing up in tab.",
        category = "General", subcategory = "Entities"
    )
    public static boolean hideNpcsInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Limbo Limiter",
        description = "Limit your framerate to 15, reducing the load of the game on the computer while in Limbo.",
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
        description = "Announce when you've gotten an achievement in Guild chat.",
        category = "General", subcategory = "Guilds"
    )
    public static boolean broadcastAchievements;

    @Property(
        type = PropertyType.SWITCH, name = "Broadcast Levelup",
        description = "Announce when you've leveled up in Guild chat.",
        category = "General", subcategory = "Guilds"
    )
    public static boolean broadcastLevelup;

    @Property(
        type = PropertyType.SWITCH, name = "AutoStart",
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
        max = 100
    )
    public static int autoQueueDelay;

    @Property(
        type = PropertyType.SWITCH, name = "Armour Hider",
        description = "Hide armour in games where armour is always the same.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hideArmour;

    @Property(
        type = PropertyType.SWITCH, name = "Hardcore Hearts",
        description = "When your bed is broken/wither is killed in BedWars/The Walls, set the heart style to Hardcore.",
        category = "Game", subcategory = "Visual"
    )
    public static boolean hardcoreHearts;

    @Property(
        type = PropertyType.SWITCH, name = "Remove All Chat Message",
        description = "Hide the \"§aYou are now in the §6ALL§a channel§r\" message when auto-switching.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean hideAllChatMessage;

    public HytilitiesConfig() {
        super(new File("./config/hytilities.toml"));
        initialize();
    }
}
