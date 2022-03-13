/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.hytils.handlers.chat;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.language.LanguageData;
import cc.woverflow.hytils.util.locraw.LocrawInformation;
import cc.woverflow.hytils.util.locraw.LocrawUtil;
import gg.essential.universal.ChatColor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import cc.woverflow.hytils.handlers.chat.modules.triggers.AutoQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Note that {@link LocrawUtil} and
     * {@link AutoQueue} are always executed before
     * any other {@link ChatModule}, regardless of priority.
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
        return new ChatComponentText(ChatColor.Companion.translateAlternateColorCodes('&', message));
    }

    /**
     * Get the user's Hypixel language setting.
     */
    @NotNull
    default LanguageData getLanguage() {
        return HytilsReborn.INSTANCE.getLanguageHandler().getCurrent();
    }

    /**
     * Get the player's server location.
     */
    @Nullable
    default LocrawInformation getLocraw() {
        return HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation();
    }

    /**
     * Calling {@link IChatComponent#getUnformattedText()} will only remove § symbols, but not the ones that are
     * translated through Bukkit's ChatColor.translateAlternateColorCodes(String) method,
     * so {@link EnumChatFormatting#getTextWithoutFormattingCodes(String)} must be used to fully strip any color code
     * after they're translated.
     *
     * @return a completely stripped string
     */
    default String getStrippedMessage(IChatComponent component) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(component.getUnformattedText());
    }

}
