package club.sk1er.hytilities.handlers.chat.adblock;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Pattern;

public class AdBlocker implements ChatModule {

    // private final Pattern commonAdvertisements = Pattern.compile("/?(?:visit|ah|party|p join|guild|g join) \\w{1,16}", Pattern.CASE_INSENSITIVE);
    // https://regexr.com/5ct51 old regex would capture any sentence with party and "ah"

    /**
     * [/](party join or join party) or (p join) or (guild join or join guild) or (g join)
     * Blocks twitch.tv youtube.com/youtu.be
     * ah + visit are common words "ah yes" would flag best to keep /ah and /visit for now?
     * /visit|ah playername is blocked and visit /playername
     * https://regexr.com/5ct10 current tests
     * // TODO add more phrases to regex
     */
    private final Pattern commonAdvertisements = Pattern.compile("(?:/?)(((party join|join party)|p join|(guild join)|(join guild)|g join) \\w{1,16})|(twitch.tv)|(youtube.com|youtu.be)|(/(visit|ah) \\w{1,16}|(visit /\\w{1,16}))",
            Pattern.CASE_INSENSITIVE);

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        if (!HytilitiesConfig.hytilitiesAdblock) {
            return;
        }

        if (commonAdvertisements.matcher(event.message.getUnformattedText()).find()) {
            event.setCanceled(true);
        }
    }
}
