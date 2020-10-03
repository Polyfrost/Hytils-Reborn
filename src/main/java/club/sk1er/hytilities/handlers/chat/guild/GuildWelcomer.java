package club.sk1er.hytilities.handlers.chat.guild;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Matcher;

public class GuildWelcomer implements ChatModule {

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        String text = event.message.getUnformattedText();

        Matcher matcher = getLanguage().guildPlayerJoinRegex.matcher(text);
        if (matcher.matches()) {
            String player = matcher.group("player");
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/gc Welcome to the guild " + player + "!");
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.guildWelcomer;
    }
}
