package club.sk1er.hytilities.handlers.lobby.bossbar;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.util.LobbyChecker;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LobbyBossbar {

    @SubscribeEvent
    public void onBossbarRender(RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH && LobbyChecker.playerIsInLobby() && HytilitiesConfig.hytilitiesLobbyBossbar) {
            event.setCanceled(true);
        }
    }
}
