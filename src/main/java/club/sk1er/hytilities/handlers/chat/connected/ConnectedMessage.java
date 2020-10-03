package club.sk1er.hytilities.handlers.chat.connected;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ConnectedMessage implements ChatModule {
    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        if (getLanguage().connectedServerConnectMessageRegex.matcher(event.message.getUnformattedText()).matches()) {
            event.setCanceled(true);
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.serverConnectedMessages;
    }
}
