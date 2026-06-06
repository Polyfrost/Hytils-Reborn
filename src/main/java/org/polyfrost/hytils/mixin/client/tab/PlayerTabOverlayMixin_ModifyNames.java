package org.polyfrost.hytils.mixin.client.tab;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.Team;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.features.general.TabChanger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerTabOverlay.class)
abstract class PlayerTabOverlayMixin_ModifyNames {
    @WrapOperation(
        method = "getNameForDisplay",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/scores/PlayerTeam;formatNameForTeam(Lnet/minecraft/world/scores/Team;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"
        )
    )
    private static MutableComponent modifyPlayerNameForDisplay(Team team, Component component, Operation<MutableComponent> original, PlayerInfo info) {
        MutableComponent formattedName = original.call(team, component);

        if (HytilsRebornConfig.isEnabled()) {
            return TabChanger.modifyName(formattedName, info);
        }

        return formattedName;
    }
}
