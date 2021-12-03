package club.sk1er.hytilities.mixin;

import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiIngame.class)
public interface GuiIngameAccessor {
    @Accessor
    String getDisplayedTitle();
}
