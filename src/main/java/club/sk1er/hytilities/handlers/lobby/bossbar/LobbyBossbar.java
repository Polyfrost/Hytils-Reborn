package club.sk1er.hytilities.handlers.lobby.bossbar;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LobbyBossbar {

    @SubscribeEvent
    public void onBossbarRender(RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH
            && Hytilities.INSTANCE.getLobbyChecker().playerIsInLobby()
            && HytilitiesConfig.lobbyBossbar) {
            event.setCanceled(true);
        }
    }
}
