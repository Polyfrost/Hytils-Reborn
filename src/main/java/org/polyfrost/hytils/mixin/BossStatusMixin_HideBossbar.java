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

package org.polyfrost.hytils.mixin;

import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossStatus.class)
public abstract class BossStatusMixin_HideBossbar {
    @Inject(method = "setBossStatus", at = @At("HEAD"), cancellable = true)
    private static void hytils$cancelBossStatus(IBossDisplayData displayData, boolean hasColorModifierIn, CallbackInfo ci) {
        if (displayData == null) return;
        if (HytilsConfig.lobbyBossbar && HypixelUtils.INSTANCE.isHypixel() && !LocrawUtil.INSTANCE.isInGame() || HytilsConfig.gameAdBossbar && displayData.getDisplayName().getFormattedText().matches(HytilsReborn.INSTANCE.getLanguageHandler().getCurrent().gameBossbarAdvertisementRegex.pattern()))
            ci.cancel();
    }
}
