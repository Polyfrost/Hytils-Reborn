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

package org.polyfrost.hytils.mixin;

import net.hypixel.data.type.GameType;
import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.oneconfig.api.ui.v1.Notifications;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.12.2
//$$ import net.minecraft.entity.effect.StatusEffects;
//#endif

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin_MiningFatigue {
    @Unique private final EntityLivingBase hytils$this = (EntityLivingBase) (Object) this;

    @Inject(method = "addPotionEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;onNewPotionEffect(Lnet/minecraft/potion/PotionEffect;)V"))
    private void hytils$onPotionEffect(PotionEffect potioneffectIn, CallbackInfo ci) {
        boolean isMiningFatigue =
            //#if MC >= 1.12.2
            //$$ potioneffectIn.getStatusEffect() == StatusEffects.MINING_FATIGUE;
            //#else
            potioneffectIn.getPotionID() == Potion.digSlowdown.getId();
            //#endif
        if (HytilsConfig.notifyMiningFatigue && isMiningFatigue && (hytils$this instanceof EntityPlayerSP) && (!HytilsConfig.disableNotifyMiningFatigueSkyblock || !(HypixelUtils.getLocation().getGameType().orElse(null) == GameType.SKYBLOCK))) {
            Notifications.INSTANCE.enqueue(Notifications.Type.Warning, "Hytils Reborn", "You have mining fatigue!");
        }
    }
}
