package org.polyfrost.hytils.client.features.chat.handlers

import org.polyfrost.hytils.client.events.ChatReceiveEvent

/**
 * must be registered in [ChatHandler] to run
 */
interface ChatReceiveModule : ChatModule {
    /**
     * cancelling stops later modules so [ChatReceiveEvent.cancelled] checks are unnecessary here
     */
    fun onChatReceived(event: ChatReceiveEvent)
}
