package club.sk1er.hytilities.config;

import club.sk1er.vigilance.Vigilant;
import club.sk1er.vigilance.data.Property;
import club.sk1er.vigilance.data.PropertyType;

import java.io.File;

public class HytilitiesConfig extends Vigilant {

    /*@Property(
        type = PropertyType.SWITCH, name = "Ad Blocker",
        description = "Remove spam messages, typically advertising something.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hytilitiesAdblock;*/

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
    public static boolean hytilitiesLobbyStatuses;

    @Property(
        type = PropertyType.SWITCH, name = "Show Own Mystery Box Rewards",
        description = "Remove others mystery box messages from chat.\n§eExample: §b[Mystery Box] Steve §ffound a §6Legendary Hype Train Gadget§f!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hytilitiesMysteryBoxAnnouncer;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Soul Box Announcements",
        description = "Remove soul box announcements from chat.\n§eExample: §bSteve §7has found a §6Bulldozer Perk I (Insane) §7in the §bSoul Well§7!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hytilitiesSoulBoxAnnouncer;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Game Announcements",
        description = "Remove game announcements from chat.\n§eExample: §b➤ A §e§lMega Skywars §bgame is available to join! §6§lCLICK HERE §bto join!",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hytilitiesGameAnnouncements;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Hype Limit Reminder",
        description = "Remove Hype limit reminders from chat.\n§eExample: §f➤ §6You have reached your Hype limit...",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hytilitiesHypeLimitReminder;

    @Property(
        type = PropertyType.SWITCH, name = "Remove BedWars Advertisements",
        description = "Remove player messages asking to join BedWars parties.\n§eExample: §7[1✫] §b[MVP§c+§b] Steve§f: guys pls join bedwars party 2/4 :)",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hytilitiesBedwarsAdvertisements;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Friend/Guild Statuses",
        description = "Remove join/quit messages from friend/guild members.\n§eExample: §aFriend > §bSteve §ejoined.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hytilitiesConnectionStatus;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Chat Emojis",
        description = "Remove MVP++ chat emojis.\n§eExample: §c§lOOF ",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hytilitiesMvpEmotes;

    @Property(
        type = PropertyType.SWITCH, name = "White Chat",
        description = "Make nons appear as normal chat messages.",
        category = "Chat", subcategory = "Toggles"
    )
    public static boolean hytilitiesWhiteChat;

    @Property(
        type = PropertyType.SWITCH, name = "Game Status Restyle",
        description = "Replace common game status messages with a new style.",
        category = "Chat", subcategory = "Restyler"
    )
    public static boolean hytilitiesGameStatusRestyle;

    @Property(
            type = PropertyType.SWITCH, name = "Player Count Before Player Name",
            description = "Put player count before player name in game join/leave messages.",
            category = "Chat", subcategory = "Restyler"
    )
    public static boolean hytilitiesPlayerCountBeforePlayerName;

    @Property(
            type = PropertyType.SWITCH, name = "Show Player Count on Player Leave",
            description = "Include the player count when players leave.",
            category = "Chat", subcategory = "Restyler"
    )
    public static boolean hytilitiesPlayerCountOnPlayerLeave;

    @Property(
            type = PropertyType.SWITCH, name = "Zero-Pad Player Counts",
            description = "Places zeros at the beginning of player counts to align with the max player count.",
            category = "Chat", subcategory = "Restyler"
    )
    public static boolean hytilitiesPadPlayerCount;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Lobby NPCs",
        description = "Hide NPCs in the lobby.",
        category = "Lobby", subcategory = "Entities"
    )
    public static boolean hytilitiesNpcHider;

    @Property(
        type = PropertyType.SWITCH, name = "Hide NPCs in tab",
        description = "Remove NPCs from ever showing up in tab.",
        category = "General", subcategory = "Entities"
    )
    public static boolean hytilitiesHideNpcsInTab;

    @Property(
        type = PropertyType.SWITCH, name = "Limbo Limiter",
        description = "Limit your framerate to 15, reducing the load of the game on the computer while in Limbo.",
        category = "Lobby", subcategory = "General"
    )
    public static boolean hytilitiesLimboLimiter;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Lobby Bossbars",
        description = "Hide the bossbar in the lobby.",
        category = "Lobby", subcategory = "GUI"
    )
    public static boolean hytilitiesLobbyBossbar;

    @Property(
        type = PropertyType.SWITCH, name = "Broadcast Achievements",
        description = "Announce when you've gotten an achievement in Guild chat.",
        category = "General", subcategory = "Guilds"
    )
    public static boolean hytilitiesBroadcastAchievements;

    @Property(
        type = PropertyType.SWITCH, name = "Broadcast Levelup",
        description = "Announce when you've leveled up in Guild chat.",
        category = "General", subcategory = "Guilds"
    )
    public static boolean hytilitiesBroadcastLevelup;

    @Property(
        type = PropertyType.SWITCH, name = "AutoStart",
        description = "Join Hypixel immediately once the game is done starting.",
        category = "General", subcategory = "General"
    )
    public static boolean hytilitiesAutoStart;

    @Property(
        type = PropertyType.SWITCH, name = "Chat Swapper",
        description = "Automatically change back to ALL chat when kicked from a party.",
        category = "Chat", subcategory = "Parties"
    )
    public static boolean hytilitiesChatSwapper;

    public HytilitiesConfig() {
        super(new File("./config/hytilities.toml"));
        initialize();
    }
}
