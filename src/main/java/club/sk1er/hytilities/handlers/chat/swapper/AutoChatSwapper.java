package club.sk1er.hytilities.handlers.chat.swapper;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoChatSwapper implements ChatModule {

    private final Pattern partyStatusRegex = Pattern.compile("(You have been kicked from the party by (?:\\[.+] )?\\w{1,16})|((?:\\[.+] )?\\w{1,16} has disbanded the party!)");

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        if (!HytilitiesConfig.chatSwapper) {
            return;
        }

        final Matcher statusMatcher = this.partyStatusRegex.matcher(event.message.getUnformattedText());
        if (statusMatcher.find()) {
            // todo: maybe remove the line of "you're already in this channel!" when this is ran
            // but the user is already in all chat, so chat doesn't become a garbled mess
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/chat a");
        }
    }
}
