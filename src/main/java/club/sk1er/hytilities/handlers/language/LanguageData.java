package club.sk1er.hytilities.handlers.language;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Data class for storing the Regex's for each Hypixel language.
 *
 * @author Koding
 */
@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class LanguageData {

    /**
     * GSON deserialization fields which are loaded in when the file is parsed.
     */
    public String autoQueuePrefix = "You died! Want to play again?";
    public String autoQueueClick = "Click here!";

    private String chatCleanerJoinNormal = "joined the lobby";
    private String chatCleanerJoinHalloween = "spooked in the lobby";
    private String chatCleanerMysteryBoxFind = "^(?<player>\\w{1,16}) found a \u2730{5} Mystery Box!$";
    private String chatCleanerSoulWellFind = "^.+ has found .+ in the Soul Well!$";
    private String chatCleanerGameAnnouncement = "^\u27A4 A .+ game is (?:available to join|starting in .+ seconds)! CLICK HERE to join!$";
    private String chatCleanerBedwarsPartyAdvertisement = "(?<number>[1-3]/[2-4])";
    private String chatCleanerConnectionStatus = "^(?:Friend|Guild) > (?<player>\\w{1,16}) (?:joined|left)\\.$";
    private String chatCleanerMvpEmotes = "\u00A7r\u00A7(?:c\u2764|6\u272e|a\u2714|c\u2716|b\u2615|e\u279c|e\u00af\\\\_\\(\u30c4\\)_/\u00af|c\\(\u256F\u00B0\u25A1\u00B0\uFF09\u256F\u00A7r\u00A7f\uFE35\u00A7r\u00A77 \u253B\u2501\u253B|d\\( \uFF9F\u25E1\uFF9F\\)/|a1\u00A7r\u00A7e2\u00A7r\u00A7c3|b\u2609\u00A7r\u00A7e_\u00A7r\u00A7b\u2609|e\u270E\u00A7r\u00A76\\.\\.\\.|a\u221A\u00A7r\u00A7e\u00A7l\\(\u00A7r\u00A7a\u03C0\u00A7r\u00A7a\u00A7l\\+x\u00A7r\u00A7e\u00A7l\\)\u00A7r\u00A7a\u00A7l=\u00A7r\u00A7c\u00A7lL|e@\u00A7r\u00A7a'\u00A7r\u00A7e-\u00A7r\u00A7a'|6\\(\u00A7r\u00A7a0\u00A7r\u00A76\\.\u00A7r\u00A7ao\u00A7r\u00A7c\\?\u00A7r\u00A76\\)|b\u0F3C\u3064\u25D5_\u25D5\u0F3D\u3064|e\\(\u00A7r\u00A7b'\u00A7r\u00A7e-\u00A7r\u00A7b'\u00A7r\u00A7e\\)\u2283\u00A7r\u00A7c\u2501\u00A7r\u00A7d\u2606\uFF9F\\.\\*\uFF65\uFF61\uFF9F|e\u2694|a\u270C|c\u00A7lOOF|e\u00A7l<\\('O'\\)>)\u00A7r";
    public String chatCleanerHypeLimit = "  \u27A4 You have reached your Hype limit!";

    private String connectedServerConnectMessage = "You are currently connected to server \\S+|Sending to server \\S+\\.{3}";

    private String achievementPattern = "a>> {3}Achievement Unlocked: (?<achievement>.+) {3}<<a";
    private String levelUpPattern = "You are now Hypixel Level (?<level>\\d+)!";

    private String chatRestylerGameJoinStyle = "^§r§(?<color>[\\da-f])(?<player>\\w{1,16})§r§e has joined (?<amount>.+)!§r$";
    private String chatRestylerGameLeaveStyle = "^§r§(?<color>[\\da-f])(?<player>\\w{1,16})§r§e has quit!§r$";
    private String chatRestylerGameStartCounterStyle = "^The game starts in (?<time>\\d{1,3}) seconds?!§r$";
    private String chatRestylerFormattedPaddingPattern = "\\(§r§b(\\d{1,2})§r§e/§r§b(\\d{1,3})§r§e\\)";
    private String chatRestylerPartyPattern = "^((?:\\u00a7r)?\\u00a7\\w)(Party )(\\u00a7\\w>)";
    private String chatRestylerGuildPattern = "^((?:\\u00a7r)?\\u00a7\\w)(Guild >)";
    private String chatRestylerFriendPattern = "^((?:\\u00a7r)?\\u00a7\\w)(Friend >)";

    private String autoChatSwapperPartyStatus = "^(?:You have been kicked from the party by (?:\\[.+] )?\\w{1,16}|(?:\\[.+] )?\\w{1,16} has disbanded the party!|You left the party.)$";
    private String autoChatSwapperChannelSwap = "^(?:You are now in the (?<channel>ALL|GUILD|OFFICER) channel)";
    public String autoChatSwapperAlreadyInChannel = "You're already in this channel!";

    private String whiteChatNonMessage = "(?<prefix>.+)§7: (?<message>.*)";
    private String privateMessageWhiteChat = "^(?<type>.+|To|From) (?<prefix>.+)§7: (?<message>.*)$";

    public String limboLimiterSpawned = "You were spawned in Limbo.";
    public String limboLimiterAfk = "You are AFK. Move around to return from AFK.";

    private String silentRemovalLeaveMessage = "(?:Friend|Guild) > (?<player>\\w{1,16}) left\\.";

    /**
     * Cached values which use the messages read from the config file.
     * Particularly Regexes.
     */
    public List<String> chatCleanerJoinMessageTypes;

    public Pattern chatCleanerMysteryBoxFindRegex;
    public Pattern chatCleanerSoulWellFindRegex;
    public Pattern chatCleanerGameAnnouncementRegex;
    public Pattern chatCleanerBedwarsPartyAdvertisementRegex;
    public Pattern chatCleanerConnectionStatusRegex;
    public Pattern chatCleanerMvpEmotesRegex;

    public Pattern connectedServerConnectMessageRegex;

    public Pattern achievementRegex;
    public Pattern levelUpRegex;

    public Pattern chatRestylerGameJoinStyleRegex;
    public Pattern chatRestylerGameLeaveStyleRegex;
    public Pattern chatRestylerGameStartCounterStyleRegex;
    public Pattern chatRestylerFormattedPaddingPatternRegex;
    public Pattern chatRestylerPartyPatternRegex;
    public Pattern chatRestylerGuildPatternRegex;
    public Pattern chatRestylerFriendPatternRegex;

    public Pattern autoChatSwapperPartyStatusRegex;
    public Pattern autoChatSwapperChannelSwapRegex;

    public Pattern whiteChatNonMessageRegex;
    public Pattern privateMessageWhiteChatRegex;
    public Pattern silentRemovalLeaveMessageRegex;

    /**
     * Compiles all the required patterns and caches them for later use.
     */
    public void initialize() {
        chatCleanerJoinMessageTypes = Arrays.asList(chatCleanerJoinNormal, chatCleanerJoinHalloween);

        chatCleanerMysteryBoxFindRegex = Pattern.compile(chatCleanerMysteryBoxFind);
        chatCleanerSoulWellFindRegex = Pattern.compile(chatCleanerSoulWellFind);
        chatCleanerGameAnnouncementRegex = Pattern.compile(chatCleanerGameAnnouncement);
        chatCleanerBedwarsPartyAdvertisementRegex = Pattern.compile(chatCleanerBedwarsPartyAdvertisement);
        chatCleanerConnectionStatusRegex = Pattern.compile(chatCleanerConnectionStatus);
        chatCleanerMvpEmotesRegex = Pattern.compile(chatCleanerMvpEmotes);

        connectedServerConnectMessageRegex = Pattern.compile(connectedServerConnectMessage);

        achievementRegex = Pattern.compile(achievementPattern);
        levelUpRegex = Pattern.compile(levelUpPattern);

        chatRestylerGameJoinStyleRegex = Pattern.compile(chatRestylerGameJoinStyle);
        chatRestylerGameLeaveStyleRegex = Pattern.compile(chatRestylerGameLeaveStyle);
        chatRestylerGameStartCounterStyleRegex = Pattern.compile(chatRestylerGameStartCounterStyle);
        chatRestylerFormattedPaddingPatternRegex = Pattern.compile(chatRestylerFormattedPaddingPattern);
        chatRestylerPartyPatternRegex = Pattern.compile(chatRestylerPartyPattern);
        chatRestylerGuildPatternRegex = Pattern.compile(chatRestylerGuildPattern);
        chatRestylerFriendPatternRegex = Pattern.compile(chatRestylerFriendPattern);

        autoChatSwapperPartyStatusRegex = Pattern.compile(autoChatSwapperPartyStatus);
        autoChatSwapperChannelSwapRegex = Pattern.compile(autoChatSwapperChannelSwap);

        whiteChatNonMessageRegex = Pattern.compile(whiteChatNonMessage);
        privateMessageWhiteChatRegex = Pattern.compile(privateMessageWhiteChat);
        silentRemovalLeaveMessageRegex = Pattern.compile(silentRemovalLeaveMessage);
    }

}
