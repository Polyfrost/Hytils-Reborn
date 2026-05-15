package org.polyfrost.hytils.mixin.client.chat.parameters;

//? if >=1.21.11 {
import net.minecraft.client.gui.ActiveTextCollector;
import org.polyfrost.hytils.ducks.GuiGraphicsDuck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.UnaryOperator;

@Mixin(targets = "net.minecraft.client.gui.components.ChatComponent$ClickableTextOnlyGraphicsAccess")
abstract class ClickableTextOnlyGraphicsAccessMixin implements GuiGraphicsDuck {
    @Shadow @Final private ActiveTextCollector output;

    @Override
    public void hytils$applyParameters(UnaryOperator<ActiveTextCollector.Parameters> modifier) {
        output.defaultParameters(modifier.apply(output.defaultParameters()));
    }
}
//?}
