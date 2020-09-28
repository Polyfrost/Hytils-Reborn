package club.sk1er.hytilities.handlers.general;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoStart {

    private int ticks;
    private GuiScreen gui;

    @SubscribeEvent
    public void openGui(GuiScreenEvent.InitGuiEvent event) {
        this.gui = event.gui;
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (this.gui instanceof GuiMainMenu && Hytilities.INSTANCE.isLoadedCall() && HytilitiesConfig.autoStart) {
            // we need to delay for a second as gl context needs to be created first,
            // otherwise everything is blocky & untextured for a few seconds
            // when joining a world.
            if (ticks != 20) {
                ++ticks;
            } else {
                GuiMultiplayer guiMultiplayer = new GuiMultiplayer(this.gui);
                guiMultiplayer.setWorldAndResolution(Minecraft.getMinecraft(), this.gui.width, this.gui.height);
                guiMultiplayer.directConnect = true;
                guiMultiplayer.selectedServer = new ServerData("hypixel", "hypixel.net", false);
                guiMultiplayer.confirmClicked(true, 0);
                Hytilities.INSTANCE.setLoadedCall(false);

                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }
}
