/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
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

package cc.woverflow.hytils.mixin.overlay;

import cc.woverflow.hytils.hooks.BlockModelRendererHook;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.BitSet;
import java.util.List;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin_NoOptiFine {
    @ModifyArgs(method = "renderModelAmbientOcclusionQuads", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;putColorMultiplier(FFFI)V"))
    private void modifyArgs(Args args, IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, List<BakedQuad> listQuadsIn, float[] quadBounds, BitSet boundsFlags, BlockModelRenderer.AmbientOcclusionFace aoFaceIn) {
        try {
            BlockModelRendererHook.handleHeightOverlay(args, blockAccessIn.getBlockState(blockPosIn), blockPosIn);
        } catch (Exception ignored) {

        }
    }

    @ModifyArgs(method = "renderModelStandardQuads", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;putColorMultiplier(FFFI)V"))
    private void modifyArg2s(Args args, IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, EnumFacing faceIn, int brightnessIn, boolean ownBrightness, WorldRenderer worldRendererIn, List<BakedQuad> listQuadsIn, BitSet boundsFlags) {
        try {
            BlockModelRendererHook.handleHeightOverlay(args, blockAccessIn.getBlockState(blockPosIn), blockPosIn);
        } catch (Exception ignored) {

        }
    }
}
