package club.sk1er.hytilities.handlers.chat.adblock;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Pattern;

public class AdBlocker {

    // match "[/]<visit|ah|party|p join|guild|g join> playername"
    // the "/" is optional as some people simply just say "ah playername"
    private final Pattern commonAdvertisements = Pattern.compile("/?(?:visit|ah|party|p join|guild|g join) \\w{1,16}", Pattern.CASE_INSENSITIVE);

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!HytilitiesConfig.hytilitiesAdblock || !MinecraftUtils.isHypixel()) {
            return;
        }

        if (commonAdvertisements.matcher(event.message.getUnformattedText()).find()) {
            event.setCanceled(true);
        }
    }
}
