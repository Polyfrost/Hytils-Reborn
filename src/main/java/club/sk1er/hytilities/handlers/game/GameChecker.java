package club.sk1er.hytilities.handlers.game;

import club.sk1er.mods.core.universal.ChatColor;
import club.sk1er.mods.core.util.Multithreading;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;

/**
 * Handles checking for the current location of the player using many different techniques.
 */

public class GameChecker {

    private static GameType gameType = GameType.UNKNOWN;

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
                switch (scoreboardTitle) {
                    case "BED WARS":
                        gameType = GameType.BED_WARS;
                        break;
                    case "SKYWARS":
                        gameType = GameType.SKY_WARS;
                        break;
                    case "PROTOTYPE":
                        gameType = GameType.PROTOTYPE;
                        break;
                    case "MURDER MYSTERY":
                        gameType = GameType.MURDER_MYSTERY;
                        break;
                    case "HOUSING":
                        gameType = GameType.HOUSING;
                        break;
                    case "ARCADE GAMES":
                        gameType = GameType.ARCADE_GAMES;
                        break;
                    case "BUILD BATTLE":
                        gameType = GameType.BUILD_BATTLE;
                        break;
                    case "DUELS":
                        gameType = GameType.DUELS;
                        break;
                    case "UHC CHAMPIONS":
                        gameType = GameType.UHC_CHAMPIONS;
                        break;
                    case "THE TNT GAMES":
                        gameType = GameType.TNT_GAMES;
                        break;
                    case "CLASSIC GAMES":
                        gameType = GameType.CLASSIC_GAMES;
                        break;
                    case "COPS AND CRIMS":
                        gameType = GameType.COPS_AND_CRIMS;
                        break;
                    case "BLITZ SG":
                        gameType = GameType.BLITZ_SG;
                        break;
                    case "MEGA WALLS":
                        gameType = GameType.MEGA_WALLS;
                        break;
                    case "SMASH HEROES":
                        gameType = GameType.SMASH_HEROES;
                        break;
                    case "WARLORDS":
                        gameType = GameType.WARLORDS;
                        break;
                    default:
                        gameType = GameType.UNKNOWN;
                        break;
                }
            }
        }, 3, TimeUnit.SECONDS);
    }

    public static GameType getGameType() {
        return gameType;
    }
}
