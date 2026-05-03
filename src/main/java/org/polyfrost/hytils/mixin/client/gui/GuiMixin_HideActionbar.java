package org.polyfrost.hytils.mixin.client.gui;

import net.hypixel.data.type.GameType;
import net.minecraft.client.gui.Gui;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin_HideActionbar {
    @Inject(method = "renderOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void hideActionbar(CallbackInfo ci) {
        if (HytilsRebornConfig.isEnabled() && HypixelUtils.isHypixel() && HypixelUtils.getLocation().inGame()
            && ((HytilsRebornConfig.INSTANCE.getHideHousingActionBar() && HypixelUtils.getLocation().getGameType().orElse(null) == GameType.HOUSING)
            || (HytilsRebornConfig.INSTANCE.getHideDropperActionBar() && "DROPPER".equals(HypixelUtils.getLocation().getMode().orElse(null))))
        ) {
            ci.cancel();
        }
    }
}
