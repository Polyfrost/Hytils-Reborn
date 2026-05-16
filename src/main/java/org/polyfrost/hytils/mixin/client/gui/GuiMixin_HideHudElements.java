package org.polyfrost.hytils.mixin.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.polyfrost.hytils.client.handlers.game.HideHudElements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
abstract class GuiMixin_HideHudElements {
    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    public void hideHearts(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, float f, int m, int n, int o, boolean bl, CallbackInfo ci) {
        if (HideHudElements.shouldHideHearts(player)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    public void hideHunger(GuiGraphics guiGraphics, Player player, int i, int j, CallbackInfo ci) {
        if (HideHudElements.shouldHideHunger()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void hideArmorBar(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, CallbackInfo ci) {
        if (HideHudElements.shouldHideArmorBar()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderAirBubbles", at = @At("HEAD"), cancellable = true)
    public void hideAirBubbles(GuiGraphics guiGraphics, Player player, int i, int j, int k, CallbackInfo ci) {
        if (HideHudElements.shouldHideAirBubbles()) {
            ci.cancel();
        }
    }
}
