package org.polyfrost.hytils.mixin.client.hud;

//? if <1.21.8 {
/*import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import org.polyfrost.hytils.client.features.game.HideHudElements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
abstract class GuiMixin_HideHudElements {
    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    public void hideHearts(
        GuiGraphicsExtractor graphics,
        Player player,
        int xLeft,
        int yLineBase,
        int healthRowHeight,
        int heartOffsetIndex,
        float maxHealth,
        int currentHealth,
        int oldHealth,
        int absorption,
        boolean blink,
        CallbackInfo ci
    ) {
        if (HideHudElements.shouldHideHearts()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    public void hideHunger(GuiGraphicsExtractor graphics, Player player, int yLineBase, int xRight, CallbackInfo ci) {
        if (HideHudElements.shouldHideHunger()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void hideArmorBar(
        GuiGraphicsExtractor graphics,
        Player player,
        int yLineBase,
        int numHealthRows,
        int healthRowHeight,
        int xLeft,
        CallbackInfo ci
    ) {
        if (HideHudElements.shouldHideArmorBar()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderAirBubbles", at = @At("HEAD"), cancellable = true)
    public void hideAirBubbles(GuiGraphicsExtractor graphics, Player player, int vehicleHearts, int yLineAir, int xRight, CallbackInfo ci) {
        if (HideHudElements.shouldHideAirBubbles()) {
            ci.cancel();
        }
    }
}
*///?}
