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

package club.sk1er.hytilities.util.skyblock;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

import java.util.Set;

public class SkyblockChecker {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Set<String> skyblockInAllLanguages = Sets.newHashSet("SKYBLOCK", "\u7A7A\u5C9B\u751F\u5B58", "\u7A7A\u5CF6\u751F\u5B58");

    // stolen from sba then stolen from neu
    public boolean isSkyblockScoreboard() {
        final EntityPlayerSP player = mc.thePlayer;
        if (mc.theWorld != null && player != null) {
            if (!mc.isSingleplayer() && player.getClientBrand() != null) {
                if (!player.getClientBrand().contains("Hypixel")) {
                    return false;
                }
            } else {
                return false;
            }

            final Scoreboard scoreboard = mc.theWorld.getScoreboard();
            final ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);
            if (sidebarObjective != null) {
                final String objectiveName = sidebarObjective.getDisplayName().replaceAll("(?i)\\u00A7.", "");
                for (String skyblock : skyblockInAllLanguages) {
                    return objectiveName.startsWith(skyblock);
                }
            }
        }

        return false;
    }
}
