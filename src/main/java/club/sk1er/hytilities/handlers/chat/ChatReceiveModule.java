package club.sk1er.hytilities.handlers.chat;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

/**
 * Notifies the implementer when a chat message is received by the client.
 * Must be registered in {@link ChatHandler}'s constructor with registerReceiveModule
 */
public interface ChatReceiveModule extends AbstractChatModule {
    void onChatEvent(ClientChatReceivedEvent event);

    boolean isReceiveModuleEnabled();
}
