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

package org.polyfrost.hytils.mixin.beds;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.deftu.omnicore.api.client.OmniClient;
import net.minecraft.block.BedBlock;

import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.BedLocationHandler;
import org.polyfrost.hytils.util.BedColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockModelRenderer.class)
public abstract class BlockModelRendererMixin_RenderNewModels {

    @WrapOperation(method = "renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockPos;Lnet/minecraft/client/renderer/WorldRenderer;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelRenderer;renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockPos;Lnet/minecraft/client/renderer/WorldRenderer;Z)Z"))
    private boolean hytils$redirectBedModel(BlockModelRenderer instance, IBlockAccess blockView, IBakedModel model, IBlockState state, BlockPos pos, WorldRenderer buffer, boolean cull, Operation<Boolean> original) {
        if (HytilsConfig.coloredBeds && hytils$isBed(pos)) {
            int[] locations = BedLocationHandler.INSTANCE.getBedLocations();
            if (locations != null) {
                int side = (int)((Math.atan2(pos.getZ(), pos.getX()) + Math.PI * 4) / Math.toRadians(45)) % 8;
                side = locations[side];
                BedColor color = BedColor.getBedColor(BedLocationHandler.COLORS_REVERSE.get(side));
                if (color != null) {
                    model = hytils$getModelById(color.getBlockStateIdentifier(state));
                }
            }
        }

        return original.call(instance, blockView, model, state, pos, buffer, cull);
    }

    @Unique
    private static IBakedModel hytils$getModelById(ModelResourceLocation identifier) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher()
            .getBlockModelShapes()
            .getModelManager()
            .getModel(identifier);
    }

    @Unique
    private static boolean hytils$isBed(BlockPos pos) {
        World world = OmniClient.getWorld();
        if (world != null) {
            return world.getBlockState(pos).getBlock() instanceof BedBlock;
        } else {
            return false;
        }
    }

}
