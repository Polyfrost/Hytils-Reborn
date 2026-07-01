package org.polyfrost.hytils.mixin.client.hud;

import net.hypixel.data.type.GameType;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//~ if <26.2 'gui.Hud' -> 'gui.Gui'
@Mixin(net.minecraft.client.gui.Hud.class)
abstract class HudMixin_HideActionbar {
    //~ if <26.1 'extractOverlayMessage' -> 'renderOverlayMessage'
    @Inject(method = "extractOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void hideActionbar(CallbackInfo ci) {
        if (HytilsRebornConfig.isEnabled() && HypixelUtils.isHypixel() && HypixelUtils.getLocation().inGame()
            && ((HytilsRebornConfig.INSTANCE.getHideHousingActionBar() && HypixelUtils.getLocation().getGameType().orElse(null) == GameType.HOUSING)
            || (HytilsRebornConfig.INSTANCE.getHideDropperActionBar() && "DROPPER".equals(HypixelUtils.getLocation().getMode().orElse(null))))
        ) {
            ci.cancel();
        }
    }
}
