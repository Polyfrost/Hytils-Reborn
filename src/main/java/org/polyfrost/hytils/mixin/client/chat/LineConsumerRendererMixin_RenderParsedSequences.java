package org.polyfrost.hytils.mixin.client.chat;

//? if >=1.21.11 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.FormattedCharSequence;
import org.polyfrost.hytils.client.chat.ChatEnhancements;
import org.polyfrost.hytils.client.chat.core.ParsedChatSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.gui.components.ChatComponent$1")
abstract class LineConsumerRendererMixin_RenderParsedSequences {
    @WrapOperation(
        method = "accept",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;handleMessage(IFLnet/minecraft/util/FormattedCharSequence;)Z"
        )
    )
    private boolean renderParsedSequence(
        ChatComponent.ChatGraphicsAccess instance,
        int textTop,
        float alpha,
        FormattedCharSequence sequence,
        Operation<Boolean> original,
        @Local(ordinal = 1) int lineBottom,
        @Local(ordinal = 2) int lineTop
    ) {
        if (sequence instanceof ParsedChatSequence parsed) {
            return ChatEnhancements.renderSequence(parsed, instance, textTop, alpha, lineBottom, lineTop);
        }

        return original.call(instance, textTop, alpha, sequence);
    }
}
//?}
