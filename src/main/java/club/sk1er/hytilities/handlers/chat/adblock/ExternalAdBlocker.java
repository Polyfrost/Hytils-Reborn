package club.sk1er.hytilities.handlers.chat.adblock;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExternalAdBlocker {


    @SubscribeEvent
    public void onBookOpen(GuiOpenEvent event) {
        if (!HytilitiesConfig.hytilitiesAdblock || !MinecraftUtils.isHypixel()) {
            return;
        }

        if (event.gui instanceof GuiScreenBook) {
            if (((GuiScreenBook) event.gui).pageGetCurrent().contains("SALE")) {
                event.setCanceled(true);

                // Hypixel will only display this every 24h.
                // safe to assume nobody is keeping their game opened for 24h+
                // so unregister the class as we no longer need to keep checking for what
                // the gui is that was just opened.
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }
}
