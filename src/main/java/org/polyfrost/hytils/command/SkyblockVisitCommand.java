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

package org.polyfrost.hytils.command;

import com.mojang.authlib.GameProfile;
import dev.deftu.omnicore.api.client.chat.OmniClientChat;
import dev.deftu.textile.Text;
import dev.deftu.textile.minecraft.MCTextStyle;
import dev.deftu.textile.minecraft.TextColors;
import net.hypixel.data.type.GameType;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Handler;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Param;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.oneconfig.utils.v1.Multithreading;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Command({"skyblockvisit", "sbvisit"})
public class SkyblockVisitCommand {
    /**
     * Used for performing a rudimentary check to prevent visiting invalid houses.
     */
    protected static final Pattern usernameRegex = Pattern.compile("\\w{1,16}");

    protected static String playerName = "";

    @Handler
    private void main() {
        OmniClientChat.displayChatMessage(Text.literal("Usage: /skyblockvisit <username>").setStyle(MCTextStyle.color(TextColors.RED)));
    }

    @Handler(description = "Visit a player's SkyBlock island.")
    private void main(@Param("Player Name") GameProfile player) {
        if (!HypixelUtils.isHypixel()) {
            OmniClientChat.displayChatMessage(Text.literal("You must be on Hypixel to use this command!").setStyle(MCTextStyle.color(TextColors.RED)));
            return;
        }
        if (usernameRegex.matcher(player.getName()).matches()) {
            playerName = player.getName();
            if (GameType.SKYBLOCK.equals(HypixelUtils.getLocation().getGameType().orElse(null)) && HypixelUtils.getLocation().inGame()) {
                visit(0);
                return;
            }
            HytilsReborn.INSTANCE.getCommandQueue().queue("/play skyblock");
            EventManager.INSTANCE.register(new SkyblockVisitHook());
        } else {
            OmniClientChat.displayChatMessage(Text.literal("Invalid username!").setStyle(MCTextStyle.color(TextColors.RED)));
        }
    }

    private void visit(final long time) {
        if (playerName != null) {
            Multithreading.schedule(
                () -> HytilsReborn.INSTANCE.getCommandQueue().queue("/visit " + playerName),
                time, TimeUnit.MILLISECONDS); // at 300ms you can be nearly certain that nothing important will be null
        }
    }

    private class SkyblockVisitHook {
        @Subscribe
        public void onSkyblockLobbyJoin(final WorldEvent.Load event) {
            EventManager.INSTANCE.unregister(this);
            visit(300);
        }
    }
}
