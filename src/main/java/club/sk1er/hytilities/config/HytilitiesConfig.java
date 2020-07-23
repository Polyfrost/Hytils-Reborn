package club.sk1er.hytilities.config;

import club.sk1er.vigilance.Vigilant;
import club.sk1er.vigilance.data.Property;
import club.sk1er.vigilance.data.PropertyType;

import java.io.File;

public class HytilitiesConfig extends Vigilant {

    @Property(
        type = PropertyType.SWITCH, name = "Ad Blocker",
        description = "Remove spam messages, typically advertising something.",
        category = "General", subcategory = "Chat"
    )
    public static boolean hytilitiesAdblock;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Line Separators",
        description = "Remove separators from chat.\n§eExample: §7§m---------",
        category = "General", subcategory = "Chat"
    )
    public static boolean hytilitiesLineBreaker;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Lobby Statuses",
        description = "Remove lobby join messages from chat.\n§eExample: §b[MVP§c+§b] asbyth §6joined the lobby!",
        category = "General", subcategory = "Chat"
    )
    public static boolean hytilitiesLobbyStatuses;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Mystery Box Announcements",
        description = "Remove mystery box messages from chat.\n§eExample: §b[Mystery Box] asbyth §ffound a §6Legendary Hype Train Gadget§f!",
        category = "General", subcategory = "Chat"
    )
    public static boolean hytilitiesMysteryBoxAnnouncer;

    @Property(
        type = PropertyType.SWITCH, name = "Remove Game Announcements",
        description = "Remove game announcements from chat.\n§eExample: §b➤ A §e§lMega Skywars §bgame is available to join! §6§lCLICK HERE §bto join!",
        category = "General", subcategory = "Chat"
    )
    public static boolean hytilitiesGameAnnouncements;

    @Property(
        type = PropertyType.SWITCH, name = "Hide Lobby NPC's",
        description = "Hide NPC's in the lobby.",
        category = "General", subcategory = "Entities"
    )
    public static boolean hytilitiesNpcHider;

    public HytilitiesConfig() {
        super(new File("./config/hytilities.toml"));
        initialize();
    }
}
