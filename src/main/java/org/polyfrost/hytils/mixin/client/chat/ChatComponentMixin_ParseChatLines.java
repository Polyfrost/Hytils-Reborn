package org.polyfrost.hytils.mixin.client.chat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.features.chat.enhancements.ChatEnhancements;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//? if <1.21.11
//import org.polyfrost.hytils.client.features.chat.enhancements.core.CustomChatLine;

import java.util.List;

@Mixin(ChatComponent.class)
abstract class ChatComponentMixin_ParseChatLines {
    @Shadow @Final /*? if <1.21.11 {*//* private *//*?}*/ Minecraft minecraft;

    @WrapOperation(
        method = "addMessageToDisplayQueue",
        at = @At(
            value = "INVOKE",
            //? if >=1.21.11 {
            target = "Lnet/minecraft/client/GuiMessage;splitLines(Lnet/minecraft/client/gui/Font;I)Ljava/util/List;"
            //?} else
            //target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;wrapComponents(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/client/gui/Font;)Ljava/util/List;"
        )
    )
    private List<FormattedCharSequence> parseChatLines(
        //? if >=1.21.11 {
        GuiMessage message,
        Font font,
        int maxWidth,
        Operation<List<FormattedCharSequence>> original
        //?} else {
        /*net.minecraft.network.chat.FormattedText formattedText,
        int maxWidth,
        Font font,
        Operation<List<FormattedCharSequence>> original,
        @com.llamalad7.mixinextras.sugar.Local(argsOnly = true) GuiMessage message
        *///?}
    ) {
        if (HytilsRebornConfig.isEnabled() && HypixelUtils.isHypixel()) {
            double chatScale = this.minecraft.options.chatScale().get();
            int chatWidth = Mth.floor(ChatComponent.getWidth(this.minecraft.options.chatWidth().get()) / chatScale);
            return ChatEnhancements.parseChatLines(message.content(), chatWidth, font);
        }

        //~ if <1.21.11 'message, font, maxWidth' -> 'formattedText, maxWidth, font'
        return original.call(message, font, maxWidth);
    }

    //? if <1.21.11 {
    /*@WrapOperation(
        method = "method_71991",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;III)V"
        )
    )
    private void renderCustomLine(
        net.minecraft.client.gui.GuiGraphics graphics,
        Font font,
        FormattedCharSequence sequence,
        int x,
        int textTop,
        int color,
        Operation<Void> original,
        @com.llamalad7.mixinextras.sugar.Local(argsOnly = true, ordinal = 2) int lineTop,
        @com.llamalad7.mixinextras.sugar.Local(argsOnly = true, ordinal = 3) int lineBottom
    ) {
        if (sequence instanceof CustomChatLine customLine) {
            float textAlpha = net.minecraft.util.ARGB.alphaFloat(color);
            ChatEnhancements.renderCustomLine(customLine, graphics, lineBottom, lineTop, textTop, textAlpha);
            return;
        }

        original.call(graphics, font, sequence, x, textTop, color);
    }

    @WrapOperation(
        method = "getClickedComponentStyleAt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/StringSplitter;componentStyleAtWidth(Lnet/minecraft/util/FormattedCharSequence;I)Lnet/minecraft/network/chat/Style;"
        )
    )
    private net.minecraft.network.chat.Style getParsedLineStyleAt(
        net.minecraft.client.StringSplitter instance,
        FormattedCharSequence sequence,
        int mouseX,
        Operation<net.minecraft.network.chat.Style> original
    ) {
        if (sequence instanceof CustomChatLine customLine) {
            return ChatEnhancements.getStyleAt(customLine, mouseX, this.minecraft.font);
        }

        return original.call(instance, sequence, mouseX);
    }
    *///?}
}
