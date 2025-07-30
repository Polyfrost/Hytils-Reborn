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

import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.polyfrost.hytils.util.BedColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin_RegisterModels {
    @Shadow
    protected abstract ModelBlockDefinition getModelBlockDefinition(ResourceLocation identifier);

    @Shadow
    protected abstract void registerVariant(ModelBlockDefinition modelVariantMap, ModelResourceLocation modelIdentifier);

    @Inject(method = "loadVariantItemModels", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadVariantModels()V"))
    private void hytils$registerStates(CallbackInfo ci) {
        for (BedColor bedColor : BedColor.values()) {
            ResourceLocation identifier = bedColor.getIdentifier();
            ModelBlockDefinition modelVariantMap = this.getModelBlockDefinition(identifier);
            for (BlockBed.EnumPartType bedPart : BlockBed.EnumPartType.values()) {
                for (EnumFacing direction : EnumFacing.Plane.HORIZONTAL.facings()) {
                    this.registerVariant(modelVariantMap, new ModelResourceLocation(identifier, "facing=" + direction.name() + ",part=" + bedPart.name()));
                }
            }
        }
    }
}
