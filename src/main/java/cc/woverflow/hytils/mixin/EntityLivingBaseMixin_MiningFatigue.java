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
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.hytils.mixin;

import cc.polyfrost.oneconfig.utils.Notifications;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin_MiningFatigue {
    private final EntityLivingBase $this = (EntityLivingBase) (Object) this;


    @Inject(method = "addPotionEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;onNewPotionEffect(Lnet/minecraft/potion/PotionEffect;)V"))
    private void onPotionEffect(PotionEffect potioneffectIn, CallbackInfo ci) {
        if (HytilsConfig.notifyMiningFatigue && potioneffectIn.getPotionID() == Potion.digSlowdown.getId() && ($this instanceof EntityPlayerSP) && (!HytilsConfig.disableNotifyMiningFatigueSkyblock || !HytilsReborn.INSTANCE.getSkyblockChecker().isSkyblockScoreboard())) {
            Notifications.INSTANCE.send("Hytils Reborn", "You have mining fatigue!");
        }
    }
}
