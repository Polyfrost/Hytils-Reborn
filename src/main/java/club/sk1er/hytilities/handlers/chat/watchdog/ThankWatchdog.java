package club.sk1er.hytilities.handlers.chat.watchdog;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ThankWatchdog implements ChatModule {
    private final String WATCHDOG_ANNOUNCEMENT_TRIGGER = "[WATCHDOG ANNOUNCEMENT]";
    private final String THANK_WATCHDOG_MESSAGE = "/achat Thanks Watchdog!";

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        String text = event.message.getUnformattedText();

        if (text.equals(WATCHDOG_ANNOUNCEMENT_TRIGGER)) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage(THANK_WATCHDOG_MESSAGE);
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.thankWatchdog;
    }
}
