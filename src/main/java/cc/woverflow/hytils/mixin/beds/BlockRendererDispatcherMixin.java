/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022, 2023  Polyfrost, Sk1er LLC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.hytils.mixin.beds;

import cc.woverflow.hytils.hooks.BedModelHook;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRendererDispatcher.class)
public class BlockRendererDispatcherMixin {
    @Inject(method = "getModelFromBlockState", at = @At("RETURN"), cancellable = true)
    private void addBedModels(IBlockState state, IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<IBakedModel> cir) {
        IBakedModel model = BedModelHook.getBedModel(state, pos, cir.getReturnValue());
        if (model != null) {
            cir.setReturnValue(model);
        }
    }
}
