package org.polyfrost.hytils.mixin.client.hud;

//? if <1.21.8 {
/*import net.minecraft.client.gui.Gui;
import org.polyfrost.hytils.client.features.game.HideActionBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
abstract class GuiMixin_HideActionBar {
    @Inject(method = "renderOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void hideActionbar(CallbackInfo ci) {
        if (HideActionBar.shouldHide()) {
            ci.cancel();
        }
    }
}
*///?}
