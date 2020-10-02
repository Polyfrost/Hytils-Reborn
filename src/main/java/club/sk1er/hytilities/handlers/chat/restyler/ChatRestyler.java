package club.sk1er.hytilities.handlers.chat.restyler;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import club.sk1er.hytilities.handlers.lobby.limbo.LimboLimiter;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChatRestyler implements ChatModule {

    private static int playerCount = -1;
    private static int maxPlayerCount = -1;
    private final Pattern gameJoinStyle = Pattern.compile("^§r§(?<color>[\\da-f])(?<player>\\w{1,16})§r§e has joined (?<amount>.+)!§r$");
    private final Pattern gameLeaveStyle = Pattern.compile("^§r§(?<color>[\\da-f])(?<player>\\w{1,16})§r§e has quit!§r$");
    //    private final Pattern unformattedPaddingPattern = Pattern.compile("\\((\\d{1,2})/(\\d{1,3})\\)");
    private final Pattern gameStartCounterStyle = Pattern.compile("^The game starts in (?<time>\\d{1,3}) seconds?!§r$");
    private final Pattern formattedPaddingPattern = Pattern.compile("\\(§r§b(\\d{1,2})§r§e/§r§b(\\d{1,3})§r§e\\)");
    // (?:\u00a7r)? is optional capture group as not all the messages contain §r at the start (e.g leave join messages)
    private final Pattern partyPattern = Pattern.compile("^((?:\\u00a7r)?\\u00a7\\w)(Party )(\\u00a7\\w>)");
    private final Pattern guildPattern =  Pattern.compile("^((?:\\u00a7r)?\\u00a7\\w)(Guild >)");
    private final Pattern friendPattern =  Pattern.compile("^((?:\\u00a7r)?\\u00a7\\w)(Friend >)");


    /**
     * Normally this wouldn't be static but it has to be called from a static method so it has to be static.
     * As long as we don't make multiple ChatRestyler objects it should be fine.
     * (Called by {@link LimboLimiter#onWorldChange(WorldEvent.Unload)})
     */
    public static void reset() {
        playerCount = maxPlayerCount = -1;
    }

    private static String pad(String n) {
        if (HytilitiesConfig.padPlayerCount) {
            return StringUtils.repeat('0', String.valueOf(maxPlayerCount).length() - n.length()) + n;
        } else {
            return n;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        String message = event.message.getFormattedText().trim();
        String unformattedMessage = event.message.getUnformattedText().trim();

        Matcher joinMatcher = gameJoinStyle.matcher(message);

        if (joinMatcher.matches()) {
            String amount = joinMatcher.group("amount").replaceAll("(?i)\u00a7[\\da-fk-or]", "");
            String[] amounts = amount.substring(1, amount.length() - 1).split("/");
            playerCount = Integer.parseInt(amounts[0]);
            maxPlayerCount = Integer.parseInt(amounts[1]);
        }

        if (HytilitiesConfig.shortChannelNames) {
            Matcher partyMatcher = partyPattern.matcher(message);
            Matcher guildMatcher = guildPattern.matcher(message);
            Matcher friendMatcher = friendPattern.matcher(message);
            if (partyMatcher.find()) {
                event.message = colorMessage(message.replaceAll(partyPattern.pattern(),
                        partyMatcher.group(1) + "P " + partyMatcher.group(3)));
            }else if(guildMatcher.find()){
                event.message = colorMessage(message.replaceAll(guildPattern.pattern(),
                        guildMatcher.group(1) + "G >"));
            }else if(friendMatcher.find()){
                event.message = colorMessage(message.replaceAll(friendPattern.pattern(),
                        friendMatcher.group(1) + "F >"));
            }
        }

        // Currently unformattedMessage doesn't need to be changed but I'm leaving these in, commented, in case it's
        // changed in the future and they need to be padded.
        if (HytilitiesConfig.padPlayerCount) {
            Matcher mf = formattedPaddingPattern.matcher(message);
//            Matcher mu = unformattedPaddingPattern.matcher(unformattedMessage);
            if (mf.find(0)) { // this only matches a small part so we need find()
                mf.replaceAll("(§r§b" + pad(mf.group(1)) + "§r§e/§r§b" + mf.group(2) + "§r§e)");
//                uf.replaceAll("(" + pad(mu.group(1)) + "/" + mu.group(2) + ")");

                joinMatcher = gameJoinStyle.matcher(message); // recalculate since we padded
                event.message = new ChatComponentText(message);
            }
        }

        if (HytilitiesConfig.gameStatusRestyle) { // todo: all the code following this might have room for optimization, should be looked into
            if (joinMatcher.matches()) {
                if (HytilitiesConfig.playerCountBeforePlayerName) {
                    event.message = colorMessage("&a&l+ &e" + joinMatcher.group("amount")
                            + " &" + joinMatcher.group("color") + joinMatcher.group("player"));
                } else {
                    event.message = colorMessage("&a&l+ &" + joinMatcher.group("color") + joinMatcher.group("player") + " &e" +
                            joinMatcher.group("amount"));
                }
            } else {
                Matcher leaveMatcher = gameLeaveStyle.matcher(message);
                if (leaveMatcher.matches()) {
                    if (HytilitiesConfig.playerCountOnPlayerLeave) {
                        if (HytilitiesConfig.playerCountBeforePlayerName) {
                            event.message = colorMessage("&c&l- &e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount +
                                    "&e) &" + leaveMatcher.group("color") + leaveMatcher.group("player"));
                        } else {
                            event.message = colorMessage("&c&l- &" + leaveMatcher.group("color") +
                                    leaveMatcher.group("player") + " &e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e)");
                        }
                    } else {
                        event.message = colorMessage("&c&l- &" + leaveMatcher.group("color") + leaveMatcher.group("player"));
                    }
                } else {
                    Matcher startCounterMatcher = gameStartCounterStyle.matcher(unformattedMessage);
                    if (startCounterMatcher.matches()) {
                        String time = startCounterMatcher.group("time");
                        boolean secondMessage = unformattedMessage.contains("seconds");

                        event.message = colorMessage("&e&l* &aGame starts in &b&l" + time
                                // for some bizarre reason, seconds is captured in the time group (though we explicitly tell
                                // it to only capture numbers (\d)), so get around that by just replacing seconds with nothing
                                .replaceAll(" seconds", "") + (secondMessage ? " &aseconds." : " &asecond."));
                    } else {
                        if ("We don't have enough players! Start cancelled.".equals(unformattedMessage)) {
                            event.message = colorMessage("&e&l* &cStart cancelled.");
                        }
                    }
                }
            }
        } else {
            if (HytilitiesConfig.playerCountOnPlayerLeave) {
                Matcher leaveMater = gameLeaveStyle.matcher(message);
                if (leaveMater.matches()) {
                    if (HytilitiesConfig.playerCountBeforePlayerName) {
                        event.message = colorMessage("&e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e) " + message);
                    } else {
                        event.message = colorMessage(message.substring(0, message.length() - 3) + " &e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e)!");
                    }
                    return;
                }
            }
            if (HytilitiesConfig.playerCountBeforePlayerName) {
                if (joinMatcher.matches()) {
                    event.message = colorMessage("&e(&b" + pad(String.valueOf(--playerCount)) + "&e/&b" + maxPlayerCount + "&e) " + message.split(" \\(")[0] + "!");
                }
            }
        }
    }

    @Override
    public boolean condition() {
        return true;
    }

}
