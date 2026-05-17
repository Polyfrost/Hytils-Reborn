package org.polyfrost.hytils.mixin.client.tab;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.features.general.TabChanger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerTabOverlay.class)
abstract class PlayerTabOverlayMixin_RemoveAds {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;", ordinal = 0), index = 0)
    private FormattedText hideAdvertisementsInHeader(FormattedText formattedText) {
        if (HytilsRebornConfig.isEnabled()) {
            return TabChanger.modifyHeader((Component) formattedText);
        }

        return formattedText;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;", ordinal = 1), index = 0)
    private FormattedText hideAdvertisementsInFooter(FormattedText formattedText) {
        if (HytilsRebornConfig.isEnabled()) {
            return TabChanger.modifyFooter((Component) formattedText);
        }

        return formattedText;
    }
}
