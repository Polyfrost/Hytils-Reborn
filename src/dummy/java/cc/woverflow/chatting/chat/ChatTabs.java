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

package cc.woverflow.chatting.chat;

import java.util.ArrayList;

public final class ChatTabs {
    public static final ChatTabs INSTANCE = new ChatTabs();

    public final ArrayList getTabs() {
        throw new RuntimeException();
    }

    public final void setCurrentTab(ChatTab currentTab) {

    }

    public final ChatTab getCurrentTab() {
        throw new RuntimeException();
    }
}
