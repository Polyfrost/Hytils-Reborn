package club.sk1er.hytilities.handlers.chat.connected;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Pattern;

public class ConnectedMessage implements ChatModule {
    private static final Pattern serverConnectMessage = Pattern.compile("You are currently connected to server \\S+|Sending to server \\S+\\.{3}");

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        if (!HytilitiesConfig.serverConnectedMessages) {
            return;
        }

        if (serverConnectMessage.matcher(event.message.getUnformattedText()).matches()) {
            event.setCanceled(true);
        }
    }
}
