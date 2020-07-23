package club.sk1er.hytilities.handlers.adblock;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;

public class AdBlocker {

    private final List<String> commonAdvertisements = Arrays.asList(
        "/ah", // skyblock auctions
        "/party", "p join", // party advertisements
        "/guild", "g join", // guild advertisements
        "/visit" // housing advertisements
    );

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!HytilitiesConfig.hytilitiesAdblock || !MinecraftUtils.isHypixel()) {
            return;
        }

        String message = event.message.getUnformattedText();
        for (String advertisement : commonAdvertisements) {
            if (message.contains(advertisement)) {
                event.setCanceled(true);
                break;
            }
        }
    }
}
