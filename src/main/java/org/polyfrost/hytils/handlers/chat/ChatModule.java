/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022, 2023  Polyfrost, Sk1er LLC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.polyfrost.hytils.handlers.chat;

import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.language.LanguageData;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

/**
 * This interface handles shared methods between {@link ChatReceiveModule} and {@link ChatSendModule}.
 * It has things like priority and enabled checks, as well as default utility methods for classes
 * to use.
 * <p>
 * It is not intended to be directly implemented, (hence the package-private) but rather for classes
 * to implement one of it's subinterfaces, for example {@link ChatReceiveModule} and {@link ChatSendModule}.
 *
 * @see ChatHandler
 */
interface ChatModule {
    char COLOR_CHAR = '§';

    // TODO: A lot of the priority numbers were chosen mostly at random, with only some thought put into them. Someone should go through them and really make sure that each one has a good priority.

    /**
     * This determines the order in which the {@link ChatModule}s are executed. The lower, the earlier.
     * It is highly recommended you override this method.
     * <p>
     * If your {@link ChatModule} removes messages then it is recommended to have a negative number.
     * The more expensive your code is, the higher your number should be (in general) so that if the
     * event is cancelled then the expensive code isn't run for nothing. However, lower numbers may
     * have increased responsiveness in the case of a large amount of activated modules. You must find
     * a good balance.
     * <p>
     *
     * @return the class's priority (lower goes first)
     */
    default int getPriority() {
        return 0;
    }

    /**
     * This function allows you to determine if your ChatModule will be executed.
     * Overriding it is <em>heavily</em> encouraged.
     * <p>
     * For example, one might return a {@link HytilsConfig} value.
     *
     * @return a {@code boolean} that determines whether or not the code should be executed
     */
    default boolean isEnabled() {
        return true;
    }

    /**
     * Default pedantically static utility method to allow {@link ChatModule}s to color messages
     * without a long line of code.
     */
    @NotNull
    default IChatComponent colorMessage(@NotNull String message) {
        char[] b = message.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        String newString = new String(b);
        return new ChatComponentText(newString);
    }

    /**
     * Get the user's Hypixel language setting.
     */
    @NotNull
    default LanguageData getLanguage() {
        return HytilsReborn.INSTANCE.getLanguageHandler().getCurrent();
    }

}
