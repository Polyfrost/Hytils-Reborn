package org.polyfrost.hytils.mixin.client.chat;

//? if >=1.21.11 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.FormattedCharSequence;
import org.polyfrost.hytils.client.features.chat.enhancements.ChatEnhancements;
import org.polyfrost.hytils.client.features.chat.enhancements.core.CustomChatLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.gui.components.ChatComponent$1")
abstract class LineConsumerRendererMixin_RenderCustomLines {
    @WrapOperation(
        method = "accept",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;handleMessage(IFLnet/minecraft/util/FormattedCharSequence;)Z"
        )
    )
    private boolean renderCustomLine(
        ChatComponent.ChatGraphicsAccess graphics,
        int textTop,
        float textAlpha,
        FormattedCharSequence sequence,
        Operation<Boolean> original,
        @Local(ordinal = 1) int lineBottom,
        @Local(ordinal = 2) int lineTop
    ) {
        if (sequence instanceof CustomChatLine customLine) {
            return ChatEnhancements.renderCustomLine(customLine, graphics, lineBottom, lineTop, textTop, textAlpha);
        }

        return original.call(graphics, textTop, textAlpha, sequence);
    }
}
//?}
