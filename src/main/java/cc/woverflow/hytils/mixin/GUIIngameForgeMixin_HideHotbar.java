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

@Mixin(value = GuiIngameForge.class, remap = false)
public class GUIIngameForgeMixin_HideHotbar {
    LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();

    // currently doesn't account for admin abuse lobbies, duels lobby pvp, or housing pvp
    @Inject(method = "renderHealth", at = @At("HEAD"), cancellable = true)
    public void cancelHealthbar(int width, int height, CallbackInfo ci) {
        if (HytilsConfig.hideHudElements && locraw != null && HypixelUtils.INSTANCE.isHypixel() && (
            HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby() ||
            locraw.getGameType() == LocrawInfo.GameType.DROPPER ||
            locraw.getGameType() == LocrawInfo.GameType.HOUSING ||
            locraw.getGameType() == LocrawInfo.GameType.BEDWARS ||
            locraw.getGameMode().contains("DUELS_PARKOUR") ||
            locraw.getGameMode().contains("DUELS_BOWSPLEEF_DUEL") ||
            locraw.getGameMode().contains("DUELS_PARKOUR") ||
            locraw.getGameMode().contains("DUELS_BOXING_DUEL")))
            ci.cancel();
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    public void cancelFood(int width, int height, CallbackInfo ci) {
        if (HytilsConfig.hideHudElements && locraw != null && HypixelUtils.INSTANCE.isHypixel() && (
            HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby() ||
            locraw.getGameType() == LocrawInfo.GameType.BEDWARS ||
            locraw.getGameType() == LocrawInfo.GameType.MURDER_MYSTERY ||
            locraw.getGameType() == LocrawInfo.GameType.HOUSING ||
            locraw.getGameType() == LocrawInfo.GameType.TNT_GAMES ||
            locraw.getGameType() == LocrawInfo.GameType.CLASSIC_GAMES ||
            locraw.getGameType() == LocrawInfo.GameType.COPS_AND_CRIMS ||
            locraw.getGameType() == LocrawInfo.GameType.SMASH_HEROES ||
            locraw.getGameType() == LocrawInfo.GameType.PIT ||
            locraw.getGameType() == LocrawInfo.GameType.WARLORDS ||
            locraw.getGameType() == LocrawInfo.GameType.DROPPER ||
            locraw.getGameType() == LocrawInfo.GameType.DUELS ||
            locraw.getGameType().toString().contains("WOOL_GAMES")))
            ci.cancel();

    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    public void cancelArmor(int width, int height, CallbackInfo ci) {
        if (HytilsConfig.hideHudElements && locraw != null && HypixelUtils.INSTANCE.isHypixel() && (
            HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby() ||
            locraw.getGameType() == LocrawInfo.GameType.DROPPER ||
            locraw.getGameType() == LocrawInfo.GameType.TNT_GAMES ||
            (locraw.getGameType() == LocrawInfo.GameType.DUELS &&
            !(locraw.getGameMode().contains("DUELS_SW_DUEL") || locraw.getGameMode().contains("DUELS_UHC_MEETUP_DUEL")))))
            ci.cancel();
    }
}
