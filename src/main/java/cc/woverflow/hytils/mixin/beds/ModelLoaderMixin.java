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

import cc.woverflow.hytils.handlers.cache.BedLocationHandler;
import cc.woverflow.hytils.hooks.BedModelHook;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(value = ModelLoader.class, remap = false)
public class ModelLoaderMixin {
    @Shadow
    @Final
    private Set<ResourceLocation> textures;

    @Inject(method = "setupModelRegistry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureMap;loadSprites(Lnet/minecraft/client/resources/IResourceManager;Lnet/minecraft/client/renderer/texture/IIconCreator;)V"), remap = true)
    private void getVariantsTextureLocations(CallbackInfoReturnable<Set<ResourceLocation>> cir) {
        for (String color : BedLocationHandler.COLORS_REVERSE.values()) {
            textures.add(new ResourceLocation(BedModelHook.COLORED_BED + color));
        }
    }
}
