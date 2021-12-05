package club.sk1er.hytilities.mixin;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.cache.CosmeticsHandler;
import club.sk1er.hytilities.handlers.game.GameType;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V", at = @At("HEAD"), cancellable = true)
    private void yeah(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        if (stack == null) return;
        if (HytilitiesConfig.hideDuelsCosmetics && Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation() != null && Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation().getGameType() == GameType.DUELS && !Hytilities.INSTANCE.getLobbyChecker().playerIsInLobby() && (stack.getItem() instanceof ItemDoublePlant || stack.getItem() instanceof ItemDye || stack.getItem() instanceof ItemRecord || shouldRemove(stack.getItem().getUnlocalizedName()) ||(stack.getItem() instanceof ItemBlock && (shouldRemove(((ItemBlock) stack.getItem()).block.getUnlocalizedName()) || ((ItemBlock) stack.getItem()).block instanceof BlockPumpkin)))) ci.cancel();
    }

    private boolean shouldRemove(String name) {
        AtomicBoolean yes = new AtomicBoolean();
        CosmeticsHandler.INSTANCE.itemCosmetics.forEach((itemName) -> {
            if (name.equals(itemName)) yes.set(true);
        });
        return yes.get();
    }
}
