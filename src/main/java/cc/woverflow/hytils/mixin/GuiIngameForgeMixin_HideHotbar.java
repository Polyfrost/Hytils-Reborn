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

package cc.woverflow.hytils.mixin;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = GuiIngameForge.class, remap = false)
public class GuiIngameForgeMixin_HideHotbar {
    // currently doesn't account for admin abuse lobbies, duels lobby pvp, or housing pvp
    @Inject(method = "renderHealth", at = @At("HEAD"), cancellable = true)
    public void cancelHealthbar(int width, int height, CallbackInfo ci) {
        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (HytilsConfig.hideHudElements && locraw != null && HypixelUtils.INSTANCE.isHypixel()) {
            if (HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby()) {
                ci.cancel();
                return;
            }

            LocrawInfo.GameType gameType = locraw.getGameType();
            switch (gameType) {
                case DROPPER:
                case HOUSING:
                case BEDWARS:
                    ci.cancel();
                    return;
            }

            String gameMode = locraw.getGameMode();
            Arrays.asList(
                "DUELS_PARKOUR",
                "DUELS_BOWSPLEEF_DUEL",
                "DUELS_PARKOUR",
                "DUELS_BOXING_DUEL"
            ).forEach((mode) -> {
                if (gameMode.contains(mode)) {
                    ci.cancel();
                }
            });
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    public void cancelFood(int width, int height, CallbackInfo ci) {
        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (HytilsConfig.hideHudElements && locraw != null && HypixelUtils.INSTANCE.isHypixel()) {
            if (HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby()) {
                ci.cancel();
                return;
            }

            LocrawInfo.GameType gameType = locraw.getGameType();
            switch (gameType) {
                case BEDWARS:
                case MURDER_MYSTERY:
                case HOUSING:
                case TNT_GAMES:
                case CLASSIC_GAMES:
                case COPS_AND_CRIMS:
                case SMASH_HEROES:
                case PIT:
                case WARLORDS:
                case DROPPER:
                case DUELS:
                case WOOL_GAMES:
                    ci.cancel();
            }
        }
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    public void cancelArmor(int width, int height, CallbackInfo ci) {
        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (HytilsConfig.hideHudElements && locraw != null && HypixelUtils.INSTANCE.isHypixel()) {
            if (HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby()) {
                ci.cancel();
                return;
            }

            LocrawInfo.GameType gameType = locraw.getGameType();
            String gameMode = locraw.getGameMode();
            switch (gameType) {
                case DUELS:
                    // break out of the switch if it's a duels game that has armor
                    if (gameMode.contains("DUELS_SW_DUEL") || gameMode.contains("DUELS_UHC_MEEUP_DUEL")) {
                        break;
                    }
                case DROPPER:
                case TNT_GAMES:
                    ci.cancel();
            }
        }
    }
}
