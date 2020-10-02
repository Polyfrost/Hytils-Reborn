package club.sk1er.hytilities.handlers.chat.whitechat;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhiteChat implements ChatModule {

    private final Pattern nonMessage = Pattern.compile("(?<prefix>.+)§7: (?<message>.*)");

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        Matcher matcher = nonMessage.matcher(event.message.getFormattedText());

        if (matcher.find(0)) {
            event.message = new ChatComponentText(matcher.group("prefix") + ": " + matcher.group("message"));
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.whiteChat;
    }
}
