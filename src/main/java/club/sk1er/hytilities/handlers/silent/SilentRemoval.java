package club.sk1er.hytilities.handlers.silent;

import club.sk1er.hytilities.Hytilities;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class SilentRemoval {

    private final List<String> silentUsers = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onChat(ClientChatReceivedEvent event) {
        Matcher matcher = Hytilities.INSTANCE.getLanguageHandler().getCurrent().silentRemovalLeaveMessageRegex.matcher(event.message.getUnformattedText());

        if (matcher.matches()) {
            // not a friend anymore :(
            for (String friend : silentUsers) {
                if (matcher.group("player").equalsIgnoreCase(friend)) {
                    Hytilities.INSTANCE.getCommandQueue().queue("/f remove " + friend);
                    silentUsers.remove(friend);
                }
            }
        }
    }

    public List<String> getSilentUsers() {
        return silentUsers;
    }
}
