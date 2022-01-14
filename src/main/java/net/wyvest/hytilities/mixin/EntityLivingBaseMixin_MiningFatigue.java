/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wyvest.hytilities.mixin;

import gg.essential.api.EssentialAPI;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.config.HytilitiesConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin_MiningFatigue {
    private final EntityLivingBase $this = (EntityLivingBase) (Object) this;


    @Inject(method = "addPotionEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;onNewPotionEffect(Lnet/minecraft/potion/PotionEffect;)V"))
    private void onPotionEffect(PotionEffect potioneffectIn, CallbackInfo ci) {
        if (HytilitiesConfig.notifyMiningFatigue && potioneffectIn.getPotionID() == Potion.digSlowdown.getId() && ($this instanceof EntityPlayerSP) && (!HytilitiesConfig.disableNotifyMiningFatigueSkyblock || !Hytilities.INSTANCE.getSkyblockChecker().isSkyblockScoreboard())) {
            EssentialAPI.getNotifications().push("Hytilities Reborn", "You have miner fatigue!");
        }
    }
}
