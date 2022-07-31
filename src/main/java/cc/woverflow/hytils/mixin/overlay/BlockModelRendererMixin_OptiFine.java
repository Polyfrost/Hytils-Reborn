/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.optifine.render.RenderEnv;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin_OptiFine {

    @Dynamic("OptiFine implements its own version of renderModelAmbientOcclusionQuads")
    @ModifyArgs(method = "renderQuadsSmooth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;putColorMultiplier(FFFI)V", remap = true), remap = false)
    private void modifyArgs(Args args, IBlockAccess worldIn, IBlockState stateIn, BlockPos blockPosIn, WorldRenderer buffer, List<BakedQuad> list, RenderEnv renderEnv) {
        try {
            BlockModelRendererHook.handleHeightOverlay(args, stateIn, blockPosIn);
        } catch (Exception ignored) {

        }
    }

    @Dynamic("OptiFine implements its own version of renderModelAmbientOcclusionQuads")
    @ModifyArgs(method = "renderQuadsSmooth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;putColorMultiplierRgba(FFFFI)V", remap = false), remap = false)
    private void modifyArg4s(Args args, IBlockAccess worldIn, IBlockState stateIn, BlockPos blockPosIn, WorldRenderer buffer, List<BakedQuad> list, RenderEnv renderEnv) {
        try {
            BlockModelRendererHook.handleHeightOverlay(args, stateIn, blockPosIn);
        } catch (Exception ignored) {

        }
    }

    @Dynamic("OptiFine implements its own version of renderModelStandardOcclusionQuads")
    @ModifyArgs(method = "renderQuadsFlat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;putColorMultiplier(FFFI)V", remap = true), remap = false)
    private void modifyArg2s(Args args, IBlockAccess worldIn, IBlockState stateIn, BlockPos blockPosIn, EnumFacing face, int brightnessIn, boolean ownBrightness, WorldRenderer buffer, List<BakedQuad> list, RenderEnv renderEnv) {
        try {
            BlockModelRendererHook.handleHeightOverlay(args, stateIn, blockPosIn);
        } catch (Exception ignored) {

        }
    }
}
