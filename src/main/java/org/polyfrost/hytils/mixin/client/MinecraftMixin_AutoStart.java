package org.polyfrost.hytils.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
abstract class MinecraftMixin_AutoStart {
    @Inject(method = "onGameLoadFinished", at = @At("TAIL"))
    private void onGameLoadFinished(CallbackInfo ci) {
        if (HytilsRebornConfig.isEnabled() && HytilsRebornConfig.INSTANCE.getAutoStart()) {
            ConnectScreen.startConnecting(
                new JoinMultiplayerScreen(new TitleScreen()),
                Minecraft.getInstance(),
                ServerAddress.parseString("mc.hypixel.net"),
                new ServerData("Hypixel", "mc.hypixel.net", ServerData.Type.OTHER),
                false,
                null
            );
        }
    }
}
