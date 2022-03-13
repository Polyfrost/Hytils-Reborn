/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
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

package cc.woverflow.hytils.mixin;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import gg.essential.api.EssentialAPI;
import net.minecraftforge.client.GuiIngameForge;
import cc.woverflow.hytils.handlers.game.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngameForge.class, remap = false)
public class GuiIngameForgeMixin_HideActionbar {
    @Inject(method = "renderRecordOverlay", at = @At("HEAD"), cancellable = true)
    private void cancelActionBar(int width, int height, float partialTicks, CallbackInfo ci) {
        if (EssentialAPI.getMinecraftUtil().isHypixel() && (HytilsConfig.hideInvadersActionBar && HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation() != null && HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation().getGameType() == GameType.PROTOTYPE && "INVADERS".equalsIgnoreCase(HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation().getGameMode())) || (HytilsConfig.hideHousingActionBar && HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation() != null && HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation().getGameType() == GameType.HOUSING)) ci.cancel();
    }
}
