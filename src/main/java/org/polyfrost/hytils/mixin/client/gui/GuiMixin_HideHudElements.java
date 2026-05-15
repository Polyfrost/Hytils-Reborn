package org.polyfrost.hytils.mixin.client.gui;

import net.hypixel.data.type.GameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
abstract class GuiMixin_HideHudElements {
    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    public void hideHearts(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, float f, int m, int n, int o, boolean bl, CallbackInfo ci) {
        if (!HytilsRebornConfig.isEnabled() || !HytilsRebornConfig.INSTANCE.getHideHudElements() || !HypixelUtils.isHypixel())
            return;

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (!location.inGame() || location.getGameType().isEmpty() || "limbo".equals(location.getServerName().orElse(null))) {
            // rudimentary check if player has engaged in pvp or something
            assert Minecraft.getInstance().player != null;
            if (Minecraft.getInstance().player.getHealth() < 20) return;
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
                assert Minecraft.getInstance().player != null;
                if (Minecraft.getInstance().player.getHealth() < 20) return;
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
                assert Minecraft.getInstance().player != null;
                if (Minecraft.getInstance().player.getHealth() < 20) return;
                // these games start the player off with lowered health for decoration - they do not alter gameplay
            case "ENDER":                   // ender spleef (arcade)
                ci.cancel();
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    public void hideHunger(GuiGraphics guiGraphics, Player player, int i, int j, CallbackInfo ci) {
        if (!HytilsRebornConfig.isEnabled() || !HytilsRebornConfig.INSTANCE.getHideHudElements() || !HypixelUtils.isHypixel()) return;

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (!location.inGame() || location.getGameType().isEmpty() || "limbo".equals(location.getServerName().orElse(null))) {
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
            case PROTOTYPE:
                if (!gameMode.contains("DISASTERS")) {
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

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void hideArmorBar(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, CallbackInfo ci) {
        if (!HytilsRebornConfig.isEnabled() || !HytilsRebornConfig.INSTANCE.getHideHudElements() || !HypixelUtils.isHypixel()) return;

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (!location.inGame() || location.getGameType().isEmpty() || "limbo".equals(location.getServerName().orElse(null))) {
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

    @Inject(method = "renderAirBubbles", at = @At("HEAD"), cancellable = true)
    public void hideAirBubbles(GuiGraphics guiGraphics, Player player, int i, int j, int k, CallbackInfo ci) {
        if (!HytilsRebornConfig.isEnabled() || !HytilsRebornConfig.INSTANCE.getHideHudElements() || !HypixelUtils.isHypixel()) return;

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (!location.inGame() || location.getGameType().isEmpty()) {
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
