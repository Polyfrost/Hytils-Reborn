package org.polyfrost.hytils.mixin.client.hypixel;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(HypixelUtils.Location.class)
public abstract class LocationMixin_FixLobbyStatus {
    @Shadow public abstract Optional<String> getLobbyName();
    @Shadow public abstract Optional<String> getLastLobbyName();

    @WrapMethod(method = "inLobby")
    private boolean inLobby(Operation<Boolean> original) {
        return getLobbyName().isPresent();
    }

    @WrapMethod(method = "wasInLobby")
    private boolean wasInLobby(Operation<Boolean> original) {
        return getLastLobbyName().isPresent();
    }
}
