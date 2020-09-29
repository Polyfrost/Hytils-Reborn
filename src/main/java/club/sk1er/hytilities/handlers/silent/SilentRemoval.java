package club.sk1er.hytilities.handlers.silent;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SilentRemoval {

    private final List<String> silentUsers = new ArrayList<>();
    private final Pattern leaveMessage = Pattern.compile("(?:Friend|Guild) > (?<player>\\w{1,16}) left\\.");

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onChat(ClientChatReceivedEvent event) {
        Matcher matcher = leaveMessage.matcher(event.message.getUnformattedText());

        if (matcher.matches()) {
            // not a friend anymore :(
            for (String friend : silentUsers) {
                if (matcher.group("player").equalsIgnoreCase(friend)) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/f remove " + friend);
                    silentUsers.remove(friend);
                }
            }
        }
    }

    public List<String> getSilentUsers() {
        return silentUsers;
    }
}
