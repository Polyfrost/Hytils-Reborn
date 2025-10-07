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

package org.polyfrost.hytils.handlers.lobby.limbo;

import dev.deftu.omnicore.api.client.sound.OmniClientSound;
import dev.deftu.omnicore.api.sound.OmniSounds;
import dev.deftu.textile.minecraft.MCText;
import net.minecraft.util.IChatComponent;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

public class LimboPmDing {
    @Subscribe
    public void onChat(ChatEvent.Receive event) {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        IChatComponent message = MCText.convert(event.getMessage());
        if (HypixelUtils.isHypixel() && "limbo".equals(location.getServerName().orElse(null)) && message.getFormattedText().startsWith("§dFrom §r") && HytilsConfig.limboDing) {
            OmniClientSound.play(OmniSounds.ENTITY.getExperienceOrb(), 1f, 1f);
        }
    }
}
