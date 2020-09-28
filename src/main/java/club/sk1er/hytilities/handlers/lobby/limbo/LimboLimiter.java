package club.sk1er.hytilities.handlers.lobby.limbo;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.restyler.ChatRestyler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

public class LimboLimiter {

    private static boolean limboStatus;
    private static long time;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();

        if (message.equals("You were spawned in Limbo.") || message.equals("You are AFK. Move around to return from AFK.")) {
            limboStatus = true;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (limboStatus) {
            ++time;
        } else {
            time = 0;
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Unload event) {
        limboStatus = false;
        ChatRestyler.reset(); // putting this here so we don't have to make a new event class just to do this
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public static boolean shouldLimitFramerate() {
        return (!Display.isActive() || limboStatus) && HytilitiesConfig.limboLimiter && time * 20 >= 5;
    }
}
