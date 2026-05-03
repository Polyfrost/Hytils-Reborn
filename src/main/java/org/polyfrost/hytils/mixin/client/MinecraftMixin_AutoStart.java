package org.polyfrost.hytils.mixin.client;

import dev.deftu.omnicore.api.client.network.OmniClientServers;
import net.minecraft.client.Minecraft;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin_AutoStart {
    @Inject(method = "onGameLoadFinished", at = @At("TAIL"))
    private void onGameLoadFinished(CallbackInfo ci) {
        if (HytilsRebornConfig.isEnabled() && HytilsRebornConfig.INSTANCE.getAutoStart()) {
            OmniClientServers.connectTo("mc.hypixel.net");
        }
    }
}
