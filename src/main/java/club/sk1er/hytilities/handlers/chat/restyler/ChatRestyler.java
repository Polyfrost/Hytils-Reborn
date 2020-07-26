package club.sk1er.hytilities.handlers.chat.restyler;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChatRestyler implements ChatModule {

    private final Pattern gameJoinStyle = Pattern.compile("§r§(?<color>[\\da-f])(?<player>\\w{1,16})§r§e has joined (?<amount>.+)!");
    private final Pattern gameLeaveStyle = Pattern.compile("§r§(?<color>[\\da-f])(?<player>\\w{1,16})§r§e has quit!");
    private final Pattern gameStartCounterStyle = Pattern.compile("The game starts in (?<time>\\d{1,3}) seconds?!");

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        if (!MinecraftUtils.isHypixel()) {
            return;
        }

        String message = event.message.getFormattedText().trim();
        String unformattedMessage = event.message.getUnformattedText().trim();

        if (HytilitiesConfig.hytilitiesGameStatusRestyle) {
            Matcher joinMatcher = gameJoinStyle.matcher(message);

            if (joinMatcher.find()) {
                event.message = colorMessage("&b&l+ &" + joinMatcher.group("color") + joinMatcher.group("player") + " &a" +
                    joinMatcher.group("amount").replaceAll("§[\\da-fk-or]", ""));
            } else {
                Matcher leaveMatcher = gameLeaveStyle.matcher(message);
                if (leaveMatcher.find()) {
                    event.message = colorMessage("&c&l- &" + leaveMatcher.group("color") + leaveMatcher.group("player"));
                } else {
                    Matcher startCounterMatcher = gameStartCounterStyle.matcher(unformattedMessage);
                    if (startCounterMatcher.find()) {
                        String time = startCounterMatcher.group("time");
                        boolean secondMessage = unformattedMessage.contains("seconds");

                        event.message = colorMessage("&e&l* &aGame starts in &b&l" + time
                            // for some bizarre reason, seconds is captured in the time group (though we explicitly tell
                            // it to only capture numbers (\d)), so get around that by just replacing seconds with nothing
                            .replaceAll(" seconds", "") + (secondMessage ? " &aseconds." : " &asecond."));
                    }
                }
            }
        }
    }
}
