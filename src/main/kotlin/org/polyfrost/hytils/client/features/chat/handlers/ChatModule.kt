package org.polyfrost.hytils.client.features.chat.handlers

import org.polyfrost.hytils.client.HytilsRebornConfig

/**
 * This interface handles shared methods between [ChatReceiveModule] and [ChatSendModule].
 * It has things like priority and enabled checks, as well as
 * default utility methods for classes to use.
 *
 * It is not intended to be directly implemented, but rather for classes to
 * implement one of it's subinterfaces, for example [ChatReceiveModule] and [ChatSendModule].
 *
 * @see ChatHandler
 */
interface ChatModule {
    /**
     * Determines if your ChatModule will be executed.
     * Overriding it is *heavily* encouraged.
     *
     * For example, one might return a [HytilsRebornConfig] value.
     */
    val isEnabled: Boolean
        get() = true

    /**
     * Determines the order in which the [ChatModule]s are executed. The lower, the earlier.
     * It is highly recommended you override this property.
     *
     * If your [ChatModule] removes messages then it is recommended to have a negative number.
     * The more expensive your code is, the higher your number should be (in general) so that if the
     * event is canceled then the expensive code isn't run for nothing. However, lower numbers may
     * have increased responsiveness in the case of a large amount of activated modules. You must find
     * a good balance.
     */
    val priority: Int
        get() = 0
}
