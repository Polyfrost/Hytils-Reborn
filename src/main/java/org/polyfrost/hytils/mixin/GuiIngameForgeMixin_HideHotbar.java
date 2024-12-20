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

//#if FORGE
import net.hypixel.data.type.GameType;
import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.GuiIngameForge;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngameForge.class, remap = false)
public class GuiIngameForgeMixin_HideHotbar {
    @Inject(method = "renderHealth", at = @At("HEAD"), cancellable = true)
    public void hytils$cancelHealthbar(int width, int height, CallbackInfo ci) {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (HytilsConfig.hideHudElements && HypixelUtils.isHypixel()) {
            if (!location.inGame() || !location.getGameType().isPresent() || "limbo".equals(location.getServerName().orElse(null))) {
                // rudimentary check if player has engaged in pvp or something
                if (Minecraft.getMinecraft().thePlayer.getHealth() < 20) return;
                ci.cancel();
                return;
            }
            GameType gameType = location.getGameType().get();
            String gameMode = location.getMode().orElse("");
            switch (gameType) {
                case TNTGAMES:
                    // break out of the switch if it's a duels game that has varying health
                    if (gameMode.contains("CAPTURE") || gameMode.contains("PVPRUN")) break;
                case HOUSING:
                case MURDER_MYSTERY:
                case BUILD_BATTLE:
                case QUAKECRAFT:
                case REPLAY:
                    // rudimentary check if player has engaged in pvp or something
                    if (Minecraft.getMinecraft().thePlayer.getHealth() < 20) break;
                    ci.cancel();
                    return;
            }

            switch (gameMode) {
                case "DUELS_PARKOUR":           // parkour (duels)
                case "DUELS_BOWSPLEEF_DUEL":    // tnt games (duels)
                case "DUELS_BOXING_DUEL":       // boxing (duels)
                case "PIXEL_PARTY":             // pixel party (arcade)
                case "PIXEL_PARTY_HYPER":       // pixel party (arcade)
                case "HOLE_IN_THE_WALL":        // hole in the wall (arcade)
                case "SOCCER":                  // football (arcade)
                case "DRAW_THEIR_THING":        // pixel painters (arcade)
                case "DROPPER":
                    // rudimentary check if player has engaged in pvp or something
                    if (Minecraft.getMinecraft().thePlayer.getHealth() < 20) break;
                // these games start the player off with lowered health for decoration - they do not alter gameplay
                case "ENDER":                   // ender spleef (arcade)
                    ci.cancel();
            }
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    public void hytils$cancelFood(int width, int height, CallbackInfo ci) {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (HytilsConfig.hideHudElements && HypixelUtils.isHypixel()) {
            if (!location.inGame() || !location.getGameType().isPresent() || "limbo".equals(location.getServerName().orElse(null))) {
                ci.cancel();
                return;
            }
            GameType gameType = location.getGameType().get();
            String gameMode = location.getMode().orElse("");
            switch (gameType) {
                case TNTGAMES:
                    // break out of the switch if it's a tnt game that has varying hunger
                    if (gameMode.contains("CAPTURE")) {
                        break;
                    }
                case BEDWARS:
                case MURDER_MYSTERY:
                case HOUSING:
                case PAINTBALL:
                case PIT:
                case DUELS:
                case BUILD_BATTLE:
                case QUAKECRAFT:
                case REPLAY:
                case WOOL_GAMES:
                case SKYBLOCK:
                    ci.cancel();
                    return;
            }

            switch (gameMode) {
                case "PIXEL_PARTY":                 // pixel party (arcade)
                case "PIXEL_PARTY_HYPER":           // pixel party (arcade)
                case "PVP_CTW":                     // capture the wool (arcade)
                case "ZOMBIES_DEAD_END":            // zombies (arcade)
                case "ZOMBIES_BAD_BLOOD":           // zombies (arcade)
                case "ZOMBIES_ALIEN_ARCADIUM":      // zombies (arcade)
                case "HIDE_AND_SEEK_PROP_HUNT":     // hide and seek (arcade)
                case "HIDE_AND_SEEK_PARTY_POOPER":  // hide and seek (arcade)
                case "MINI_WALLS":                  // miniwalls (arcade)
                case "STARWARS":                    // galaxy wars (arcade)
                case "HOLE_IN_THE_WALL":            // hole in the wall (arcade)
                case "SOCCER":                      // football (arcade)
                case "ONEINTHEQUIVER":              // bounty hunters (arcade)
                case "DRAW_THEIR_THING":            // pixel painters (arcade)
                case "ENDER":                       // ender spleef (arcade)
                case "DROPPER":
                    ci.cancel();
            }
        }
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    public void hytils$cancelArmor(int width, int height, CallbackInfo ci) {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (HytilsConfig.hideHudElements && HypixelUtils.isHypixel()) {
            if (!location.inGame() || !location.getGameType().isPresent() || "limbo".equals(location.getServerName().orElse(null))) {
                ci.cancel();
                return;
            }
            GameType gameType = location.getGameType().get();
            String gameMode = location.getMode().orElse("");
            switch (gameType) {
                case DUELS:
                    // break out of the switch if it's a duels game that has varying armor
                    if (gameMode.contains("DUELS_SW_DUEL") || gameMode.contains("DUELS_UHC_MEETUP_DUEL")) {
                        break;
                    }
                case REPLAY:
                case MURDER_MYSTERY:
                case BUILD_BATTLE:
                case QUAKECRAFT:
                case TNTGAMES:
                case SKYBLOCK:
                    ci.cancel();
                    return;
            }

            switch (gameMode) {
                case "SOCCER":          // football (arcade)
                case "ONEINTHEQUIVER":  // bounty hunters (arcade)
                case "ENDER":           // ender spleef (arcade)
                case "DROPPER":         // dropper (prototype)
                    ci.cancel();
            }
        }
    }

    @Inject(method = "renderAir", at = @At("HEAD"), cancellable = true)
    public void hytils$cancelAir(int width, int height, CallbackInfo ci) {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (HytilsConfig.hideHudElements && HypixelUtils.isHypixel()) {
            if (!location.inGame() || !location.getGameType().isPresent()) {
                ci.cancel();
                return;
            }
            GameType gameType = location.getGameType().get();
            String gameMode = location.getMode().orElse("");
            switch (gameType) {
                case BUILD_BATTLE:
                case REPLAY:
                    ci.cancel();
                    return;
            }

            switch (gameMode) {
                case "DROPPER": // dropper (prototype)
                    ci.cancel();
            }
        }
    }
}
//#endif
