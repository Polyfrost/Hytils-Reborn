package club.sk1er.hytilities.handlers.game.hardcore;

import club.sk1er.hytilities.config.HytilitiesConfig;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HardcoreStatus {

    private boolean danger;

    @SubscribeEvent
    public void worldEvent(WorldEvent.Load event) {
        if (this.danger) {
            this.danger = false;
        }
    }

    @SuppressWarnings("unused")
    public boolean shouldChangeStyle() {
        return this.danger && HytilitiesConfig.hardcoreHearts;
    }

    public void setDanger(boolean danger) {
        this.danger = danger;
    }
}
