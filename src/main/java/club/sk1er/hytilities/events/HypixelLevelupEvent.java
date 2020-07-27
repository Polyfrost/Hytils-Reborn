package club.sk1er.hytilities.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class HypixelLevelupEvent extends Event {

    private final int level;

    public HypixelLevelupEvent(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
