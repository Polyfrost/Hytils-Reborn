package club.sk1er.hytilities.mixin;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.handlers.cache.CosmeticsHandler;
import club.sk1er.hytilities.handlers.game.GameType;
import club.sk1er.hytilities.util.HypixelAPIUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class WorldMixin {

    @Inject(method = "spawnParticle(IZDDDDDD[I)V", at = @At("HEAD"), cancellable = true)
    private void removeParticles(int particleID, boolean p_175720_2_, double xCood, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int[] p_175720_15_, CallbackInfo ci) {
        if (Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation() != null && Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation().getGameType() == GameType.DUELS && !HypixelAPIUtils.isLobby()) {
            String particleName = EnumParticleTypes.getParticleFromId(particleID).getParticleName();
            CosmeticsHandler.INSTANCE.particleCosmetics.forEach((particle) -> {
                if (particleName.equalsIgnoreCase(particle)) {
                    ci.cancel();
                }
            });
        }
    }
}
