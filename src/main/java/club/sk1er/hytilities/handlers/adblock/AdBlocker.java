package club.sk1er.hytilities.handlers.adblock;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AdBlocker {

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!HytilitiesConfig.hytilitiesAdblock || !MinecraftUtils.isHypixel()) {
            return;
        }

        String message = event.message.getUnformattedText();
        if (message.contains("/ah") // skyblock auctions
            || message.contains("/party") || message.contains("p join") // party advertisements
            || message.contains("/guild") || message.contains("g join") // guild advertisements
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBookOpen(GuiOpenEvent event) {
        if (!HytilitiesConfig.hytilitiesAdblock || !MinecraftUtils.isHypixel()) {
            return;
        }

        if (event.gui instanceof GuiScreenBook) {
            NBTTagList bookPages = ((GuiScreenBook) event.gui).bookPages;
            int currPage = ((GuiScreenBook) event.gui).currPage;

            if (currPage < bookPages.tagCount()) {
                try {
                    String unformattedText = EnumChatFormatting.getTextWithoutFormattingCodes(
                        IChatComponent.Serializer.jsonToComponent(bookPages.getStringTagAt(currPage)).getUnformattedText().replace('\n', ' ')
                    );

                    if (unformattedText.contains("SALE")) {
                        event.setCanceled(true);
                    }

                } catch (Exception e) {
                    Hytilities.INSTANCE.sendMessage("&cFailed to close advertisement book.");
                    e.printStackTrace();
                }
            }
        }
    }
}
