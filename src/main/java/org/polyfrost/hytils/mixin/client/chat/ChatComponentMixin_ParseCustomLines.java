package org.polyfrost.hytils.mixin.client.chat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.chat.ChatEnhancements;
import org.polyfrost.hytils.client.chat.core.ChatTextBuilder;
import org.polyfrost.hytils.client.chat.core.ChatLineParser;
import org.polyfrost.hytils.client.chat.core.ParsedChatSequence;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatComponent.class)
abstract class ChatComponentMixin_ParseCustomLines {
    //~ if <1.21.11 'protected' -> 'public' {
    @Shadow protected abstract int getWidth();
    @Shadow protected abstract double getScale();
    //~}

    //? if <1.21.11
    //@Shadow @org.spongepowered.asm.mixin.Final private net.minecraft.client.Minecraft minecraft;

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
    private List<FormattedCharSequence> parseCustomLines(
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
        if (!HytilsRebornConfig.isEnabled() || !HypixelUtils.isHypixel()) {
            //~ if <1.21.11 'message, font, maxWidth' -> 'formattedText, maxWidth, font'
            return original.call(message, font, maxWidth);
        }

        ChatTextBuilder builder = new ChatTextBuilder();
        message.content().getVisualOrderText().accept(builder);
        List<MutableComponent> texts = builder.getTexts();

        List<FormattedCharSequence> result = new ArrayList<>();
        int chatWidth = getScaledWidth();

        for (MutableComponent text : texts) {
            String rawString = text.getString();
            String trimmedString = rawString.trim();

            List<ChatLineParser.ParsedLine> parsedLines = ChatEnhancements.parseLine(
                text, rawString, trimmedString, chatWidth, font
            );

            if (parsedLines != null) {
                for (ChatLineParser.ParsedLine parsedLine : parsedLines) {
                    result.add(new ParsedChatSequence(parsedLine.getSequence(), parsedLine.getRenderer()));
                }
            } else {
                result.addAll(ComponentRenderUtils.wrapComponents(text, chatWidth, font));
            }
        }

        return result;
    }

    //? if <1.21.11 {
    /*@WrapOperation(
        method = "method_71991",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;III)V"
        )
    )
    private void renderParsedSequence(
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
        if (sequence instanceof ParsedChatSequence parsed) {
            float alpha = net.minecraft.util.ARGB.alphaFloat(color);
            ChatEnhancements.renderSequence(parsed, graphics, textTop, alpha, lineBottom, lineTop);
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
        if (sequence instanceof ParsedChatSequence parsed) {
            return ChatEnhancements.getStyleAt(parsed, mouseX, this.minecraft.font);
        }

        return original.call(instance, sequence, mouseX);
    }
    *///?}

    @Unique
    private int getScaledWidth() {
        return Mth.floor(this.getWidth() / this.getScale());
    }
}
