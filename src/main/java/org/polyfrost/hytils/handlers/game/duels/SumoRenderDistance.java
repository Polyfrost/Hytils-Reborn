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

package org.polyfrost.hytils.handlers.game.duels;

import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

public class SumoRenderDistance {
    final GameSettings gs = Minecraft.getMinecraft().gameSettings;
    int resetRd = gs.renderDistanceChunks;
    boolean wasSumo = false;
    boolean isFirstRender = true;

    @Subscribe
    public void onWorldLoad(RenderWorldLastEvent event) { // TODO
        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (HytilsConfig.sumoRenderDistance && HypixelUtils.isHypixel() && location.getMode().orElse("null").contains("SUMO")) {
            if (isFirstRender) {
                final int oldRd = gs.renderDistanceChunks;
                wasSumo = true;
                gs.renderDistanceChunks = HytilsConfig.sumoRenderDistanceAmount;
                resetRd = oldRd;
            }
            isFirstRender = false;
        }
        else {
            if (wasSumo) {
                wasSumo = false;
                isFirstRender = true;
                gs.renderDistanceChunks = resetRd;
            }
        }
    }
}
