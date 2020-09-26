package club.sk1er.hytilities.handlers.chat.adblock;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Pattern;

public class AdBlocker implements ChatModule {

    // match "[/]<visit|ah|party|p join|guild|g join> playername"
    // the "/" is optional as some people simply just say "ah playername"
    // todo: this seems to remove any mention of the word "party", needs to be fixed
    private final Pattern commonAdvertisements = Pattern.compile("/?(?:visit|ah|party|p join|guild|g join) \\w{1,16}", Pattern.CASE_INSENSITIVE);

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        /*if (!HytilitiesConfig.hytilitiesAdblock) {
            return;
        }
*/
        if (commonAdvertisements.matcher(event.message.getUnformattedText()).find()) {
            event.setCanceled(true);
        }
    }
}
