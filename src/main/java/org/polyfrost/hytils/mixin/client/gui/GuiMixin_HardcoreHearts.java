package org.polyfrost.hytils.mixin.client.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.level.storage.LevelData;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.handlers.game.HardcoreStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
abstract class GuiMixin_HardcoreHearts {
    @WrapOperation(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelData;isHardcore()Z"))
    private boolean modifyIsHardcore(LevelData levelData, Operation<Boolean> original) {
        return original.call(levelData) || (HytilsRebornConfig.isEnabled() && HardcoreStatus.shouldChangeStyle());
    }
}
