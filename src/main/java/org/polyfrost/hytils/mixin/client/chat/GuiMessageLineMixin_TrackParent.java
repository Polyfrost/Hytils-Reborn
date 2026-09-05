package org.polyfrost.hytils.mixin.client.chat;

//? if <26.1 {
/*import net.minecraft.client.GuiMessage;
import org.polyfrost.hytils.ducks.GuiMessageLineDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiMessage.Line.class)
abstract class GuiMessageLineMixin_TrackParent implements GuiMessageLineDuck {
    @Unique
    private GuiMessage hytils$parent;

    @Override
    public GuiMessage hytils$getParent() {
        return hytils$parent;
    }

    @Override
    public void hytils$setParent(GuiMessage parent) {
        this.hytils$parent = parent;
    }
}
*///?}
