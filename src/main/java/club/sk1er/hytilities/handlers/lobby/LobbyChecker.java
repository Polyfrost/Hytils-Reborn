package club.sk1er.hytilities.handlers.lobby;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
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
        if (!MinecraftUtils.isHypixel()) {
            this.lobbyStatus = false;
            return;
        }

        // todo: information can be null when invoked for the first time
        // find a way around this, maybe schedule it for a few seconds?
        // everything i've tried hasn't worked, so this will do for now, just wont
        // work the first time around.
        final LocrawInformation info = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
        this.lobbyStatus = this.lobbyPattern.matcher(info.getServerId()).matches();
    }

    public boolean playerIsInLobby() {
        return this.lobbyStatus;
    }
}
