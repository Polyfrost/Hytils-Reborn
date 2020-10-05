package club.sk1er.hytilities.handlers.chat;

/**
 * Notifies the implementer when a chat message is sent by the client.
 * Must be registered in {@link ChatHandler}'s constructor with registerSendModule
 */
public interface ChatSendModule extends AbstractChatModule {
    /**
     * Called when user sends a chat message.
     * @param message message that the user sent
     * @return if the message should be sent
     */
    boolean shouldSendMessage(String message);

    boolean isSendModuleEnabled();
}
