package org.polyfrost.hytils.mixin.client.hud;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.storage.LevelData;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.features.game.HardcoreStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//~ if <26.2 'gui.Hud' -> 'gui.Gui'
@Mixin(net.minecraft.client.gui.Hud.class)
abstract class HudMixin_HardcoreHearts {
    //~ if <26.1 'extractHearts' -> 'renderHearts'
    @WrapOperation(method = "extractHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelData;isHardcore()Z"))
    private boolean modifyIsHardcore(LevelData levelData, Operation<Boolean> original) {
        return original.call(levelData) || (HytilsRebornConfig.isEnabled() && HardcoreStatus.shouldChangeStyle());
    }
}
