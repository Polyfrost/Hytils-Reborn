package club.sk1er.hytilities.handlers.lobby;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Pattern;

public class LobbyChecker {

    private final Pattern lobbyPattern = Pattern.compile("(.+)?lobby(.+)");
    private boolean lobbyStatus;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void retrieveLocraw(ClientChatReceivedEvent event) {
        if (!MinecraftUtils.isHypixel() || Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation() == null) {
            this.lobbyStatus = false;
            return;
        }

        this.lobbyStatus = this.lobbyPattern.matcher(Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation().getServerId()).matches();
    }

    public boolean playerIsInLobby() {
        return this.lobbyStatus;
    }
}
