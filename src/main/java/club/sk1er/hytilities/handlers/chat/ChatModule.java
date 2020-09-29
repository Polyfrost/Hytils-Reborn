package club.sk1er.hytilities.handlers.chat;

import club.sk1er.mods.core.universal.ChatColor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public interface ChatModule {
    void onChatEvent(ClientChatReceivedEvent event);

    boolean condition();

    default IChatComponent colorMessage(String message) {
        return new ChatComponentText(ChatColor.translateAlternateColorCodes('&', message));
    }
}
