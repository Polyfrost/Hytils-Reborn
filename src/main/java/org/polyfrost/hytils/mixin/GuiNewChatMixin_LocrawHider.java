/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022, 2023  Polyfrost, Sk1er LLC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.polyfrost.hytils.mixin;

import org.polyfrost.oneconfig.api.hypixel.v0.HypixelUtils;
import org.polyfrost.hytils.HytilsReborn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.polyfrost.hytils.config.HytilsConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiNewChat.class)
public abstract class GuiNewChatMixin_LocrawHider {
    @Shadow @Final private Minecraft mc;

    @Shadow @Final private List<ChatLine> chatLines;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private float percentComplete;

    @Shadow public abstract void deleteChatLine(int id);

    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"), cancellable = true)
    private void handlePrintChatMessage(IChatComponent chatComponent, int chatLineId, CallbackInfo ci) {
        handleHytilsMessage(chatComponent, chatLineId, mc.ingameGUI.getUpdateCounter(), false, ci);
    }

    @Inject(method = "setChatLine", at = @At("HEAD"), cancellable = true)
    private void handleSetChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly, CallbackInfo ci) {
        handleHytilsMessage(chatComponent, chatLineId, updateCounter, displayOnly, ci);
    }

    private void handleHytilsMessage(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly, CallbackInfo ci) {
        if (HytilsConfig.hideLocraw && HypixelUtils.isHypixel() && chatComponent.getUnformattedTextForChat().startsWith("{") && chatComponent.getUnformattedTextForChat().endsWith("}")) {
            percentComplete = 1.0F;
            if (chatLineId != 0) {
                deleteChatLine(chatLineId);
            }
            if (!displayOnly) {
                chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));
                while (this.chatLines.size() > (100 + (HytilsReborn.INSTANCE.isPatcher ? 32667 : 0)))
                {
                    this.chatLines.remove(this.chatLines.size() - 1);
                }
            }
            ci.cancel();
        }
    }


}
