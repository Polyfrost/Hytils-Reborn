package club.sk1er.hytilities.mixin;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.cache.HeightHandler;
import club.sk1er.hytilities.util.ColorUtils;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.client.model.pipeline.BlockInfo;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = VertexLighterFlat.class, remap = false)
public class VertexLighterFlatMixin {
    @Shadow
    @Final
    protected BlockInfo blockInfo;

    @ModifyArgs(method = "processQuad", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/model/pipeline/VertexLighterFlat;updateColor([F[FFFFFI)V"))
    private void modifyArgs(Args args) {
        int height = HeightHandler.INSTANCE.getHeight();
        if (height == -1) return;
        if (HytilitiesConfig.heightOverlay && blockInfo.getBlockPos().getY() == (height - 1) && blockInfo.getBlock() instanceof BlockColored) {
            MapColor mapColor = blockInfo.getBlock().getMapColor(blockInfo.getWorld().getBlockState(blockInfo.getBlockPos()));
            boolean isClay = blockInfo.getBlock().getMaterial() == Material.rock;
            if (!isClay || check(mapColor.colorIndex)) {
                args.set(5, 1.0F);
                args.set(6, ColorUtils.getCachedDarkColor(mapColor));
            }
        }
    }

    private boolean check(int color) {
        switch (color) {
            case 18:
            case 25:
            case 27:
            case 28:
                return true;
            default:
                return false;
        }
    }
}
