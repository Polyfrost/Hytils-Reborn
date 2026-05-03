package org.polyfrost.hytils.client.handlers.chat

import org.polyfrost.hytils.client.HytilsRebornConfig

/**
 * This interface handles shared methods between {@link ChatReceiveModule} and {@link ChatSendModule}.
 * It has things like priority and enabled checks, as well as default utility methods for classes
 * to use.
 *
 * It is not intended to be directly implemented, (hence the package-private) but rather for classes
 * to implement one of it's subinterfaces, for example {@link ChatReceiveModule} and {@link ChatSendModule}.
 *
 * @see ChatHandler
 */
interface ChatModule {
    /**
     * This determines the order in which the [ChatModule]s are executed. The lower, the earlier.
     * It is highly recommended you override this method.
     *
     * If your [ChatModule] removes messages then it is recommended to have a negative number.
     * The more expensive your code is, the higher your number should be (in general) so that if the
     * event is canceled then the expensive code isn't run for nothing. However, lower numbers may
     * have increased responsiveness in the case of a large amount of activated modules. You must find
     * a good balance.
     *
     * @return the class's priority (lower goes first)
     */
    fun getPriority(): Int {
        return 0
    }

    /**
     * This function allows you to determine if your ChatModule will be executed.
     * Overriding it is *heavily* encouraged.
     *
     * For example, one might return a [HytilsRebornConfig] value.
     *
     * @return a `boolean` that determines whether the code should be executed
     */
    fun isEnabled(): Boolean {
        return true
    }
}
