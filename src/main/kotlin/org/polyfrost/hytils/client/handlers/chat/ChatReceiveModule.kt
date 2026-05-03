package org.polyfrost.hytils.client.handlers.chat

import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.oneconfig.api.event.v1.EventManager

/**
 * This interface is used to handle many different [ChatReceiveEvent]-consuming methods
 * without having to add them all to the [EventManager].
 *
 * Must be registered in [ChatHandler] to be executed.
 *
 * @see ChatModule
 * @see ChatHandler
 */
interface ChatReceiveModule : ChatModule {
    /**
     * Place your code here. Called when a Hytils [ChatReceiveEvent] is received.
     *
     * If the event is canceled, [ChatModule]s after that event will not execute. Therefore,
     * [ChatReceiveEvent.cancelled]} checks are unnecessary.
     *
     * @param event a [ChatReceiveEvent]
     */
    fun onChatReceived(event: ChatReceiveEvent)
}
