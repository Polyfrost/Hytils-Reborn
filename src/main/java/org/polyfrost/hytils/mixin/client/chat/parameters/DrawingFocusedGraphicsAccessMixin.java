package org.polyfrost.hytils.mixin.client.chat.parameters;

//? if >=1.21.11 {
import net.minecraft.client.gui.ActiveTextCollector;
import org.polyfrost.hytils.ducks.GuiGraphicsDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.UnaryOperator;

@Mixin(targets = "net.minecraft.client.gui.components.ChatComponent$DrawingFocusedGraphicsAccess")
abstract class DrawingFocusedGraphicsAccessMixin implements GuiGraphicsDuck {
    @Shadow private ActiveTextCollector.Parameters parameters;

    @Override
    public void hytils$applyParameters(UnaryOperator<ActiveTextCollector.Parameters> modifier) {
        this.parameters = modifier.apply(this.parameters);
    }
}
//?}
