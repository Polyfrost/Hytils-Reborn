package club.sk1er.hytilities.handlers.chat.whitechat;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhiteChat {

    private final Pattern nonMessage = Pattern.compile("(?<prefix>.+)§7: (?<message>.*)");

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!MinecraftUtils.isHypixel() || !HytilitiesConfig.hytilitiesWhiteChat) {
            return;
        }

        Matcher matcher = nonMessage.matcher(event.message.getFormattedText());

        if (matcher.find()) {
            event.message = new ChatComponentText(matcher.group("prefix") + ": " + matcher.group("message"));
        }
    }
}
