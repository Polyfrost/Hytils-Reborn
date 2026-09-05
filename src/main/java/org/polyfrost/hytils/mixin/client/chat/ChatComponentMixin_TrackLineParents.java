package org.polyfrost.hytils.mixin.client.chat;

//? if <26.1 {
/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.gui.components.ChatComponent;
import org.polyfrost.hytils.ducks.GuiMessageLineDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatComponent.class)
abstract class ChatComponentMixin_TrackLineParents {
    @ModifyExpressionValue(
        method = "addMessageToDisplayQueue",
        at = @At(value = "NEW", target = "net/minecraft/client/GuiMessage$Line")
    )
    private GuiMessage.Line trackParent(GuiMessage.Line line, @Local(argsOnly = true) GuiMessage message) {
        ((GuiMessageLineDuck) (Object) line).hytils$setParent(message);
        return line;
    }
}
*///?}
