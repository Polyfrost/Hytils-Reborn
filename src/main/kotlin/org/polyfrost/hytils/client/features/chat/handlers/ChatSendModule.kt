package org.polyfrost.hytils.client.features.chat.handlers

import org.polyfrost.hytils.client.events.ChatSendEvent

/**
 * must be registered in [ChatHandler] to run
 */
interface ChatSendModule : ChatModule {
    /**
     * cancelling stops later modules so [ChatSendEvent.cancelled] checks are unnecessary here
     */
    fun onChatSend(event: ChatSendEvent)
}
