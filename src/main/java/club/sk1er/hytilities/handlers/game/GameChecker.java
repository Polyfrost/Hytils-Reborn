package club.sk1er.hytilities.handlers.game;

import club.sk1er.mods.core.universal.ChatColor;
import club.sk1er.mods.core.util.Multithreading;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;

public class GameChecker {

    private static boolean bedwars;

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        // this will fire 3 times just bc of how the game works
        // may have a workaround, but for now this doesn't cause
        // any true issues and can be ignored.
        Multithreading.schedule(() -> {
            String scoreboardTitle = "";
            try {
                scoreboardTitle = ChatColor.stripColor(event.world.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());
            } catch (Exception ignored) {
            }

            if (!scoreboardTitle.isEmpty()) {
                // eventually make this into a fully-fledged game checker, converted to
                // switch (scoreboardTitle) {
                //  case "BED WARS":
                //      bedwars = true;
                //      break;
                //
                //  case "SKYWARS":
                //      skywars = true;
                //      break;
                //
                // etc..
                bedwars = scoreboardTitle.equals("BED WARS");
            }
        }, 3, TimeUnit.SECONDS);
    }

    public static boolean isBedwars() {
        return bedwars;
    }
}
