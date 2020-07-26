package club.sk1er.hytilities.handlers.chat;

import club.sk1er.hytilities.handlers.chat.adblock.AdBlocker;
import club.sk1er.hytilities.handlers.chat.cleaner.ChatCleaner;
import club.sk1er.hytilities.handlers.chat.restyler.ChatRestyler;
import club.sk1er.hytilities.handlers.chat.whitechat.WhiteChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler {

    private final List<ChatModule> moduleList = new ArrayList<>();

    public ChatHandler() {
        this.registerModule(new AdBlocker());
        this.registerModule(new ChatCleaner());
        this.registerModule(new ChatRestyler());
        this.registerModule(new WhiteChat());
    }

    private void registerModule(ChatModule chatModule) {
        this.moduleList.add(chatModule);
    }

    @SubscribeEvent
    public void handleChat(ClientChatReceivedEvent event) {
        for (ChatModule module : this.moduleList) {
            module.onChatEvent(event);
        }
    }
}
