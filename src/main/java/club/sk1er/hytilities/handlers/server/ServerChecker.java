package club.sk1er.hytilities.handlers.server;

import club.sk1er.hytilities.util.LobbyChecker;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ServerChecker {

    @SubscribeEvent
    public void joinServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        LobbyChecker.runLobbyCheckerTimer();
    }
}
