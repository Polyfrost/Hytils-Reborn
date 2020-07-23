package club.sk1er.hytilities.handlers.cleaner;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCleaner {

    private final List<String> joinMessageTypes = Arrays.asList(
        "joined the lobby", // normal
        "spooked in the lobby" // halloween
    );

    private final Pattern mysteryBoxFind = Pattern.compile(".+ found a Mystery Box!");
    private final Pattern gameAnnouncement = Pattern.compile("➤ A .+ game is available to join! CLICK HERE to join!");

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
                    return;
                }
            }
        }

        if (HytilitiesConfig.hytilitiesLineBreaker) {
            if (message.contains("-------") || message.contains("=======")) {
                event.setCanceled(true);
                return;
            }
        }

        if (HytilitiesConfig.hytilitiesMysteryBoxAnnouncer) {
            if (message.startsWith("[Mystery Box]")) {
                event.setCanceled(true);
            } else {
                Matcher matcher = mysteryBoxFind.matcher(message);

                if (matcher.find()) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        if (HytilitiesConfig.hytilitiesGameAnnouncements) {
            Matcher matcher = gameAnnouncement.matcher(message);

            if (matcher.find()) {
                event.setCanceled(true);
            }
        }
    }
}
