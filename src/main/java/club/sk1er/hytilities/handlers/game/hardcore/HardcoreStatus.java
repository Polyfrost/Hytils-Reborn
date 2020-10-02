package club.sk1er.hytilities.handlers.game.hardcore;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.tweaker.asm.GuiIngameForgeTransformer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.objectweb.asm.tree.ClassNode;

public class HardcoreStatus {

    private boolean danger;

    @SubscribeEvent
    public void worldEvent(WorldEvent.Load event) {
        if (this.danger) {
            this.danger = false;
        }
    }

    /**
     * Used in {@link GuiIngameForgeTransformer#transform(ClassNode, String)}
     */
    @SuppressWarnings("unused")
    public boolean shouldChangeStyle() {
        return this.danger && HytilitiesConfig.hardcoreHearts;
    }

    /**
     * Used in {@link GuiIngameForgeTransformer#transform(ClassNode, String)}
     */
    @SuppressWarnings("unused")
    public void setDanger(boolean danger) {
        this.danger = danger;
    }
}
