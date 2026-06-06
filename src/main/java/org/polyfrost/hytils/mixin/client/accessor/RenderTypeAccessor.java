package org.polyfrost.hytils.mixin.client.accessor;

//? if >=1.21.11 {
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderTypeAccessor {
    @Invoker
    static RenderType invokeCreate(String string, RenderSetup renderSetup) {
        throw new AssertionError();
    }
}
//?}
