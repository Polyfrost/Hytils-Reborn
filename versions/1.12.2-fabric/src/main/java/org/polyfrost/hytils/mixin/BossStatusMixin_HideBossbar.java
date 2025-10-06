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

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.class_2957;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.text.Text;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BossBarHud.class)
public abstract class BossStatusMixin_HideBossbar {
    @WrapWithCondition(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/BossBarHud;method_12169(IILnet/minecraft/class_2957;)V"
        )
    )
    private boolean hytils$cancelBossStatus(BossBarHud instance, int x, int y, class_2957 bossInfo) {
        if (HytilsConfig.lobbyBossbar
            && HypixelUtils.isHypixel()
            && !HypixelUtils.getLocation().inGame()) {
            return false;
        }

        return !HytilsConfig.gameAdBossbar || !hytils$isAd(bossInfo);
    }

    @Unique
    private boolean hytils$isAd(class_2957 info) {
        final String adPattern = HytilsReborn.INSTANCE
            .getLanguageHandler()
            .getCurrent()
            .gameBossbarAdvertisementRegex
            .pattern();

        Text title = info.getTitle();
        if (title == null) {
            return false;
        }

        String text = title.asFormattedString();
        return text != null && text.matches(adPattern);
    }
}
