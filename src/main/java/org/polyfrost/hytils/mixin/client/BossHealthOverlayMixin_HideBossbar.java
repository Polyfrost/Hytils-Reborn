package org.polyfrost.hytils.mixin.client;

import com.google.common.collect.Collections2;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.data.providers.LanguageData;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(BossHealthOverlay.class)
abstract class BossHealthOverlayMixin_HideBossbar {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void hideLobbyBossbar(CallbackInfo ci) {
        if (HytilsRebornConfig.isEnabled() && HytilsRebornConfig.INSTANCE.getLobbyBossbar() && HypixelUtils.isHypixel() && !HypixelUtils.getLocation().inGame()) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"))
    private Collection<LerpingBossEvent> hideGameAdBossbar(Collection<LerpingBossEvent> original) {
        if (HytilsRebornConfig.isEnabled() && HytilsRebornConfig.INSTANCE.getHideGameAdsBossbar() && HypixelUtils.isHypixel() && HypixelUtils.getLocation().inGame()) {
            return Collections2.filter(original, event -> {
                assert event != null;
                return !LanguageData.INSTANCE.getGAME_BOSSBAR_ADVERTISEMENT().matches(event.getName().getString());
            });
        }

        return original;
    }
}
