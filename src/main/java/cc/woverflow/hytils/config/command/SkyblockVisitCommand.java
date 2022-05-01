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

package cc.woverflow.hytils.config.command;

import cc.woverflow.hytils.HytilsReborn;
import com.google.common.collect.Sets;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.DisplayName;
import gg.essential.api.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SkyblockVisitCommand extends Command {

    /**
     * Used for performing a rudimentary check to prevent visiting invalid houses.
     */
    protected final Pattern usernameRegex = Pattern.compile("\\w{1,16}");

    protected String playerName = "";

    // thank you DJ; https://github.com/BiscuitDevelopment/SkyblockAddons/blob/development/src/main/java/codes/biscuit/skyblockaddons/utils/Utils.java#L100
    private static final Set<String> SKYBLOCK_IN_ALL_LANGUAGES = Sets.newHashSet(
        "SKYBLOCK", "\u7A7A\u5C9B\u751F\u5B58", "\u7A7A\u5CF6\u751F\u5B58");

    private final Set<Alias> hashSet = new HashSet<>();

    public SkyblockVisitCommand() {
        super("skyblockvisit");
        hashSet.add(new Alias("sbvisit"));
    }

    @Override
    public Set<Alias> getCommandAliases() {
        return hashSet;
    }

    @DefaultHandler
    public void handle(@DisplayName("Player Name") String playerName) {
        if (usernameRegex.matcher(playerName).matches()) {
            this.playerName = playerName;
            if (SKYBLOCK_IN_ALL_LANGUAGES.contains(EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().theWorld
                .getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().split(" ")[0]))) {
                visit(0);
                return;
            }
            HytilsReborn.INSTANCE.getCommandQueue().queue("/play skyblock");
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            HytilsReborn.INSTANCE.sendMessage("&cInvalid playername!");
        }
    }

    @SubscribeEvent
    public void onSkyblockLobbyJoin(final WorldEvent.Load event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        visit(300);
    }

    void visit(final long time) {
        if (playerName != null) {
            Multithreading.schedule(
                () -> HytilsReborn.INSTANCE.getCommandQueue().queue("/visit " + playerName),
                time, TimeUnit.MILLISECONDS); // at 300ms you can be nearly certain that nothing important will be null
        }
    }
}
