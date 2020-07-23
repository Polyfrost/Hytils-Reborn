package club.sk1er.hytilities.handlers.cleaner;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;

public class ChatCleaner {

    private final List<String> joinMessageTypes = Arrays.asList(
        "joined the lobby", // normal
        "spooked in the lobby" // halloween
    );

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!MinecraftUtils.isHypixel()) {
            return;
        }

        String message = event.message.getUnformattedText();

        if (HytilitiesConfig.hytilitiesLobbyStatuses) {
            for (String messages : joinMessageTypes) {
                if (message.contains(messages)) {
                    event.setCanceled(true);
                    break;
                }
            }
        }

        if (HytilitiesConfig.hytilitiesLineBreaker) {
            if (message.contains("-------") || message.contains("=======")) {
                event.setCanceled(true);
            }
        }
    }
}
