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

package org.polyfrost.hytils.hooks;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.BedLocationHandler;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BedModelHook {
    private static final HashMap<IBlockState, IBakedModel[]> states = new HashMap<IBlockState, IBakedModel[]>() {{
        for (IBlockState state : Blocks.bed.getBlockState().getValidStates()) {
            put(state, new IBakedModel[8]);
        }
    }};
    private static final String OAK = "minecraft:blocks/planks_oak";
    public static final String COLORED_BED = "hytils:hytils_beds/";
    private static final FaceBakery faceBakery = new FaceBakery();

    public static IBakedModel getBedModel(IBlockState state, BlockPos pos, IBakedModel model) {
        if (!HytilsConfig.coloredBeds) {
            return null;
        }
        if (pos == null || state == null) {
            return null;
        }
        if (state.getBlock() != Blocks.bed) {
            return null;
        }
        IBakedModel[] models = states.get(state);
        if (models == null) {
            return null;
        }
        int[] locations = BedLocationHandler.INSTANCE.getBedLocations();
        if (locations == null) {
            return null;
        }
        int side = (int)((Math.atan2(pos.getZ(), pos.getX()) + Math.PI * 4) / Math.toRadians(45)) % 8; // calculate which side the bed is on
        side = locations[side];
        IBakedModel cachedModel = models[side];
        if (cachedModel != null) {
            return cachedModel;
        }
        if (model == null) {
            return null;
        }
        // too lazy to make a custom SimpleBakedModel builder
        List<BakedQuad> quads = new ArrayList<>();
        List<List<BakedQuad>> faceQuads = new ArrayList<>(6);
        for (EnumFacing ignored : EnumFacing.values()) {
            faceQuads.add(new ArrayList<>());
        }
        EnumFacing facing = state.getValue(BlockDirectional.FACING);
        BlockBed.EnumPartType bedPart = state.getValue(BlockBed.PART);
        String bedTexture = COLORED_BED + BedLocationHandler.COLORS_REVERSE.get(side);
        TextureAtlasSprite particles = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(bedTexture);
        for (BakedQuad quad : model.getFaceQuads(facing)) {
            faceQuads.get(quad.getFace().ordinal()).add(new BreakingFour(quad, particles));
        }
        ModelRotation rotation = ModelRotation.getModelRotation(0, facing.getHorizontalIndex() * 90 - 180);
        int footPadding = (bedPart == BlockBed.EnumPartType.FOOT) ? 16 : 0;
        float funnyMultipler = 0.25f;
        quads.add(faceBakery.makeBakedQuad(new Vector3f(0.0f, 3.0f, 0.0f), new Vector3f(16.0f, 3.0f, 16.0f), new BlockPartFace(EnumFacing.DOWN, -1, OAK, new BlockFaceUV(new float[]{0.0f, 0.0f, 16.0f, 16.0f}, 0)), Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(OAK), EnumFacing.DOWN, rotation, new BlockPartRotation(new Vector3f(8.0f, 0.0f, 8.0f), EnumFacing.Axis.Y, 0.0f, false), false, true));
        if (bedPart != BlockBed.EnumPartType.FOOT) {
            quads.add(faceBakery.makeBakedQuad(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(16.0f, 9.0f, 0.0f), new BlockPartFace(EnumFacing.NORTH, -1, bedTexture, new BlockFaceUV(new float[]{25.0f * funnyMultipler, 9.0f * funnyMultipler, 9.0f * funnyMultipler, 0.0f}, 0)), particles, EnumFacing.NORTH, rotation, new BlockPartRotation(new Vector3f(8.0f, 0.0f, 8.0f), EnumFacing.Axis.Y, 0.0f, false), false, true));
        } else {
            quads.add(faceBakery.makeBakedQuad(new Vector3f(0.0f, 0.0f, 16.0f), new Vector3f(16.0f, 9.0f, 16.0f), new BlockPartFace(EnumFacing.SOUTH, -1, bedTexture, new BlockFaceUV(new float[]{25.0f * funnyMultipler, 50.0f * funnyMultipler, 9.0f * funnyMultipler, 41.0f * funnyMultipler}, 180)), particles, EnumFacing.SOUTH, rotation, new BlockPartRotation(new Vector3f(8.0f, 0.0f, 8.0f), EnumFacing.Axis.Y, 0.0f, false), false, true));
        }
        quads.add(faceBakery.makeBakedQuad(new Vector3f(0.0f, 9.0f, 0.0f), new Vector3f(16.0f, 9.0f, 16.0f), new BlockPartFace(EnumFacing.UP, -1, bedTexture, new BlockFaceUV(new float[]{9.0f * funnyMultipler, 9.0f * funnyMultipler + (float)footPadding * funnyMultipler, 25.0f * funnyMultipler, 25.0f * funnyMultipler + (float)footPadding * funnyMultipler}, 0)), particles, EnumFacing.UP, rotation, new BlockPartRotation(new Vector3f(8.0f, 0.0f, 8.0f), EnumFacing.Axis.Y, 0.0f, false), false, true));
        quads.add(faceBakery.makeBakedQuad(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 9.0f, 16.0f), new BlockPartFace(EnumFacing.WEST, -1, bedTexture, new BlockFaceUV(new float[]{0.0f, 9.0f * funnyMultipler + (float)footPadding * funnyMultipler, 9.0f * funnyMultipler, 25.0f * funnyMultipler + (float)footPadding * funnyMultipler}, 270)), particles, EnumFacing.WEST, rotation, new BlockPartRotation(new Vector3f(8.0f, 0.0f, 8.0f), EnumFacing.Axis.Y, 0.0f, false), false, true));
        quads.add(faceBakery.makeBakedQuad(new Vector3f(16.0f, 0.0f, 0.0f), new Vector3f(16.0f, 9.0f, 16.0f), new BlockPartFace(EnumFacing.EAST, -1, bedTexture, new BlockFaceUV(new float[]{25.0f * funnyMultipler, 9.0f * funnyMultipler + (float)footPadding * funnyMultipler, 34.0f * funnyMultipler, 25.0f * funnyMultipler + (float)footPadding * funnyMultipler}, 90)), particles, EnumFacing.EAST, rotation, new BlockPartRotation(new Vector3f(8.0f, 0.0f, 8.0f), EnumFacing.Axis.Y, 0.0f, false), false, true));
        return (models[side] = new SimpleBakedModel(quads, faceQuads, true, false, particles, model.getItemCameraTransforms()));
    }

    public static void reloadModels() {
        for (IBakedModel[] models : states.values()) {
            Arrays.fill(models, null);
        }
    }
}
