package club.sk1er.hytilities.handlers.lobby.limbo;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.restyler.ChatRestyler;
import club.sk1er.hytilities.tweaker.asm.MinecraftTransformer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;
import org.objectweb.asm.tree.ClassNode;

public class LimboLimiter {

    private static boolean limboStatus;
    private static long time;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        final String message = event.message.getUnformattedText();

        if (message.equals(Hytilities.INSTANCE.getLanguageHandler().getCurrent().limboLimiterSpawned) || message.equals(Hytilities.INSTANCE.getLanguageHandler().getCurrent().limboLimiterAfk)) {
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

    /**
     * Used in {@link MinecraftTransformer#transform(ClassNode, String)}
     */
    @SuppressWarnings("unused")
    public static boolean shouldLimitFramerate() {
        return (!Display.isActive() || limboStatus) && HytilitiesConfig.limboLimiter && time * 20 >= 5;
    }
}
