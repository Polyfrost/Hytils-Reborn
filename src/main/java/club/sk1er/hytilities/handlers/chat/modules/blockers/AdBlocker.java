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

package club.sk1er.hytilities.handlers.chat.modules.blockers;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatReceiveModule;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class AdBlocker implements ChatReceiveModule {

    @Override
    public int getPriority() {
        return -3;
    }

    // private final Pattern commonAdvertisements = Pattern.compile("/?(?:visit|ah|party|p join|guild|g join) \\w{1,16}", Pattern.CASE_INSENSITIVE);
    // https://regexr.com/5ct51 old regex would capture any sentence with party and "ah"

    /**
     * [/](party join or join party) or (p join) or (guild join or join guild) or (g join)
     * Blocks twitch.tv youtube.com/youtu.be
     * ah + visit are common words "ah yes" would flag best to keep /ah and /visit for now?
     * /visit|ah playername is blocked and visit /playername
     * https://regexr.com/5ct10 current tests
     * // TODO add more phrases to regex
     */
    private final Pattern commonAdvertisements = Pattern.compile("(?:/?)(((party join|join party)|p join|(guild join)|(join guild)|g join) \\w{1,16})|(twitch.tv)|(youtube.com|youtu.be)|(/(visit|ah) \\w{1,16}|(visit /\\w{1,16}))",
            Pattern.CASE_INSENSITIVE);

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (commonAdvertisements.matcher(event.message.getUnformattedText()).find(0)) {
            event.setCanceled(true);
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.playerAdBlock;
    }

}
