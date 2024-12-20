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

import org.jetbrains.annotations.NotNull;
import org.polyfrost.oneconfig.api.event.v1.events.ChatReceiveEvent;

/**
 * This interface is used to handle many different {@link ChatReceiveEvent}-consuming methods
 * without having to add them all to the {@link org.polyfrost.oneconfig.api.event.v1.EventManager}.
 * <p>
 * ChatModules essentially behave like small listener classes, except instead of going directly to Forge,
 * the {@link ChatHandler} handles them and passes them to Forge, taking account of things like priority and cancelled events.
 * <p>
 * Must be registered in {@link ChatHandler#ChatHandler()} to be executed.
 *
 * @see ChatModule
 * @see ChatHandler
 */
public interface ChatReceiveModule extends ChatModule {

    /**
     * Place your code here. Called when a OneConfig {@link ChatReceiveEvent} is received.
     * <p>
     * If the event is cancelled, {@link ChatModule}s after that event will not execute. Therefore,
     * {@link ChatReceiveEvent#cancelled}} checks are unnecessary.
     *
     * @param event a {@link ChatReceiveEvent}
     */
    void onMessageReceived(@NotNull ChatReceiveEvent event);

}
