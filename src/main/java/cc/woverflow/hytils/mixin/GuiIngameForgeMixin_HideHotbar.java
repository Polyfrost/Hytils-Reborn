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
            String gameMode = locraw.getGameMode();
            switch (gameType) {
                case DROPPER:
                case HOUSING:
                case BUILD_BATTLE:
                case LIMBO:
                case QUAKE:
                case REPLAY:
                case TNT_GAMES:
                    // break out of the switch if it's a duels game that has varying health
                    if (gameMode.contains("CAPTURE") || gameMode.contains("PVPRUN")) {
                        break;
                    }
                case BEDWARS:
                    ci.cancel();
                    return;
            }

            Arrays.asList(
                "DUELS_PARKOUR",
                "DUELS_BOWSPLEEF_DUEL",
                "DUELS_PARKOUR",
                "DUELS_BOXING_DUEL",
                "PIXEL_PARTY",
                "HOLE_IN_THE_WALL",
                "SOCCER",
                "DRAW_THEIR_THING",
                "ENDER"
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
            String gameMode = locraw.getGameMode();
            switch (gameType) {
                case BEDWARS:
                case MURDER_MYSTERY:
                case HOUSING:
                case LIMBO:
                case TNT_GAMES:
                    // break out of the switch if it's a duels game that has varying hunger
                    if (gameMode.contains("CAPTURE")) {
                        break;
                    }
                case CLASSIC_GAMES:
                case PIT:
                case DROPPER:
                case DUELS:
                case BUILD_BATTLE:
                case QUAKE:
                case REPLAY:
                case WOOL_WARS:
                case VAMPIREZ:
                    ci.cancel();
            }

            Arrays.asList(
                "PIXEL_PARTY",
                "PVP_CTW",
                "ZOMBIES_",
                "HIDE_AND_SEEK_",
                "MINI_WALLS",
                "STARWARS",
                "HOLE_IN_THE_WALL",
                "SOCCER",
                "ONEINTHEQUIVER",
                "DRAW_THEIR_THING",
                "DEFENDER",
                "ENDER"
            ).forEach((mode) -> {
                if (gameMode.contains(mode)) {
                    ci.cancel();
                }
            });
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
                    // break out of the switch if it's a duels game that has varying armor
                    if (gameMode.contains("DUELS_SW_DUEL") || gameMode.contains("DUELS_UHC_MEETUP_DUEL")) {
                        break;
                    }
                case DROPPER:
                case REPLAY:
                case BUILD_BATTLE:
                case LIMBO:
                case QUAKE:
                case TNT_GAMES:
                    ci.cancel();
            }

            Arrays.asList(
                "HOLE_IN_THE_WALL",
                "SOCCER",
                "ONEINTHEQUIVER",
                "DRAW_THEIR_THING",
                "ENDER"
            ).forEach((mode) -> {
                if (gameMode.contains(mode)) {
                    ci.cancel();
                }
            });
        }
    }

    @Inject(method = "renderAir", at = @At("HEAD"), cancellable = true)
    public void cancelAir(int width, int height, CallbackInfo ci) {
        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (HytilsConfig.hideHudElements && locraw != null && HypixelUtils.INSTANCE.isHypixel()) {
            if (HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby()) {
                ci.cancel();
                return;
            }

            LocrawInfo.GameType gameType = locraw.getGameType();
            switch (gameType) {
                case BUILD_BATTLE:
                case REPLAY:
                    ci.cancel();
            }
        }
    }
}
