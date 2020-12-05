/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
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

package club.sk1er.hytilities.handlers.chat.modules.modifiers;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatSendModule;
import club.sk1er.hytilities.handlers.lobby.limbo.LimboLimiter;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class LimboPlayCommandHelper implements ChatSendModule {

	/**
	 * /play command names for games that cannot be joined from limbo.
	 */
	private static final Set<String> CANNOT_JOIN_FROM_LIMBO = Sets.newHashSet("sb", "skyblock", "pit");

	@Override
	public boolean isEnabled() {
		return HytilitiesConfig.limboPlayCommandHelper && LimboLimiter.inLimbo();
	}

	@Nullable
	@Override
	public String onMessageSend(@NotNull final String message) {
		if (message.startsWith("/play ") && CANNOT_JOIN_FROM_LIMBO.contains(message.split(" ")[1])) {
			Hytilities.INSTANCE.getCommandQueue().queue("/l");
			Hytilities.INSTANCE.getCommandQueue().queue(message);
			return null;
		} else {
			return message;
		}
	}
}
