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

import dev.deftu.omnicore.api.client.chat.OmniClientChat;
import dev.deftu.textile.minecraft.MCSimpleMutableTextHolder;
import dev.deftu.textile.minecraft.MCSimpleTextHolder;
import dev.deftu.textile.minecraft.MCTextFormat;
import net.hypixel.data.type.GameType;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.hytils.HytilsReborn;
import com.mojang.authlib.GameProfile;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Combination command & listener, since they are both small.
 */
@Command({"housingvisit", "hvisit"})
public class HousingVisitCommand {

    /**
     * Used for performing a rudimentary check to prevent visiting invalid houses.
     */
    protected static final Pattern usernameRegex = Pattern.compile("\\w{1,16}");

    protected static String playerName = "";

    @Command
    private void main() {
        OmniClientChat.displayChatMessage(new MCSimpleTextHolder("Usage: /housingvisit <username>").withFormatting(MCTextFormat.RED));
    }

    @Command(description = "Visits a player's house in Housing.")
    private void main(GameProfile player) {
        if (!HypixelUtils.isHypixel()) {
            OmniClientChat.displayChatMessage(new MCSimpleMutableTextHolder("You must be on Hypixel to use this command!").withFormatting(MCTextFormat.RED));
            return;
        }
        if (usernameRegex.matcher(player.getName()).matches()) {
            playerName = player.getName();

            // if we are in the housing lobby, just immediately run the /visit command
            HypixelUtils.Location location = HypixelUtils.getLocation();
            Optional<GameType> gameType = location.getGameType();
            if (gameType.isPresent() && gameType.get() == GameType.HOUSING && !location.inGame()) {
                visit(0);
            } else {
                HytilsReborn.INSTANCE.getCommandQueue().queue("/l housing");
                EventManager.INSTANCE.register(new HousingVisitHook());
            }
        } else {
            OmniClientChat.displayChatMessage(new MCSimpleTextHolder("Invalid username!").withFormatting(MCTextFormat.RED));
        }
    }

    private void visit(final long time) {
        if (playerName != null) {
            Multithreading.schedule(() -> HytilsReborn.INSTANCE.getCommandQueue().queue("/visit " + playerName), time, TimeUnit.MILLISECONDS); // at 300ms you can be nearly certain that nothing important will be null
        }
    }

    private class HousingVisitHook {

        @Subscribe
        public void onHousingLobbyJoin(final WorldEvent.Load event) {
            EventManager.INSTANCE.unregister(this);
            visit(300);
        }

    }
}
