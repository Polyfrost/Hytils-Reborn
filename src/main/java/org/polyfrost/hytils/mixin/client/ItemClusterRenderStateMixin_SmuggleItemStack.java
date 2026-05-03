package org.polyfrost.hytils.mixin.client;

import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.polyfrost.hytils.ducks.ItemClusterRenderStateDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemClusterRenderState.class)
public class ItemClusterRenderStateMixin_SmuggleItemStack implements ItemClusterRenderStateDuck {
    @Unique
    private ItemStack itemStack;

    @Inject(method = "extractItemGroupRenderState", at = @At("HEAD"))
    public void extractItemGroupRenderState(Entity entity, ItemStack itemStack, ItemModelResolver itemModelResolver, CallbackInfo ci) {
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack hytils$getItemStack() {
        return itemStack;
    }
}
