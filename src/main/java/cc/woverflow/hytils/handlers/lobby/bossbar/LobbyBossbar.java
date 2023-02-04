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

package cc.woverflow.hytils.handlers.lobby.bossbar;

import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.entity.boss.BossStatus;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LobbyBossbar {

    @SubscribeEvent
    public void onBossbarRender(RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH && BossStatus.bossName != null &&
            ((HytilsConfig.gameAdBossbar && BossStatus.bossName.matches(HytilsReborn.INSTANCE.getLanguageHandler().getCurrent().gameBossbarAdvertisementRegex.pattern())) || (HytilsConfig.lobbyBossbar && LocrawUtil.INSTANCE.getLocrawInfo() != null && !LocrawUtil.INSTANCE.isInGame()))) {
            event.setCanceled(true);
        }
    }
}
