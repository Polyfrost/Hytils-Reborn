package org.polyfrost.hytils.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.hypixel.data.type.GameType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
//~ if <1.21.11 'villager.Villager' -> 'Villager'
import net.minecraft.world.entity.npc.villager.Villager;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
abstract class LevelRendererMixin_HideNPCs {
    @WrapOperation(
        //~ if <1.21.11 'extractVisibleEntities' -> 'collectVisibleEntities'
        method = "extractVisibleEntities",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z")
    )
    private <E extends Entity> boolean shouldRenderEntity(EntityRenderDispatcher instance, E entity, Frustum frustum, double d, double e, double f, Operation<Boolean> original) {
        boolean shouldRender = original.call(instance, entity, frustum, d, e, f);
        if (!HytilsRebornConfig.isEnabled() || !HypixelUtils.isHypixel()) return shouldRender;

        HypixelUtils.Location location = HypixelUtils.getLocation();
        boolean isNpc = !(entity instanceof ArmorStand)
            && (entity.getUUID().version() == 2 // hypixel marks npc uuids as version 2
            || entity instanceof Villager);

        if (HytilsRebornConfig.INSTANCE.getHideLobbyNPCs() && !location.inGame() && isNpc) {
            return false;
        }

        if (HytilsRebornConfig.INSTANCE.getHideNonNPCs()
            && location.getGameType().orElse(null) == GameType.SKYBLOCK
            && !isNpc
            && !(entity instanceof LocalPlayer)
        ) {
            return false;
        }

        return shouldRender;
    }
}
