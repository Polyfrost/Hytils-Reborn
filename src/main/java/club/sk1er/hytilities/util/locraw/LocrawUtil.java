package club.sk1er.hytilities.util.locraw;

import club.sk1er.hytilities.handlers.chat.ChatModule;
import club.sk1er.hytilities.handlers.game.GameType;
import club.sk1er.mods.core.util.MinecraftUtils;
import club.sk1er.mods.core.util.Multithreading;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LocrawUtil implements ChatModule {

    private LocrawInformation locrawInformation;

    private final Gson gson = new Gson();
    private boolean listening;
    private long lastWorldLoad = System.currentTimeMillis();

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!MinecraftUtils.isHypixel()) return; // Make sure that we're on Hypixel.

        // Forge likes to fire this event multiple times, so let's work around that.
        if (System.currentTimeMillis() - lastWorldLoad < 3000) return;
        lastWorldLoad = System.currentTimeMillis();

        Multithreading.runAsync(() -> {
            try {
                int tries = 0;
                // If the player is null and we haven't tried 10 times, keep trying.
                while (Minecraft.getMinecraft().thePlayer == null && tries < 10) {
                    tries++;
                    Thread.sleep(100L);
                }
            } catch (Exception ignored) {}

            // Can't do anything if after all that the player is null, return.
            if (Minecraft.getMinecraft().thePlayer == null) return;

            try {
                // Hypixel sometimes does this thing where it'll report its location as limbo, instead
                // of the full location for ~1 second, just to be safe we do this to prevent that.
                Thread.sleep(1000);

                listening = true;
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/locraw");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        if (listening) {
            try {
                // Had some false positives while testing, so this is here just to be safe.
                String msg = event.message.getUnformattedTextForChat();
                if (msg.startsWith("{")) {
                    // Parse the json, and make sure that it's not null.
                    locrawInformation = gson.fromJson(msg, LocrawInformation.class);
                    if (locrawInformation != null) {
                        // Gson does not want to parse the GameType, as some stuff is different so this
                        // is just a way around that to make it properly work :)
                        locrawInformation.setGameType(GameType.getFromLocraw(locrawInformation.getRawGameType()));

                        // Stop listening for locraw and cancel the message.
                        event.setCanceled(true);
                        listening = false;
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    public LocrawInformation getLocrawInformation() {
        return locrawInformation;
    }
}