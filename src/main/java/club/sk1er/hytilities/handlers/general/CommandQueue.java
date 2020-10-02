package club.sk1er.hytilities.handlers.general;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Sk1er
 */
public class CommandQueue {
    private final Runnable EMPTY = () -> {
    }; //An empty runnable to be substituted when none is provided

    private final Queue<QueueObject> commands = new ConcurrentLinkedQueue<>(); //Queue of messages to ber sent and their corresponding callbacks when sent
    private long delay = 20; //Delay in ticks between messages
    private int tick; //Counter used to keep track of how many ticks since the last message

    public CommandQueue() {
    }

    public void queue(String chat, Runnable task) {
        commands.add(new QueueObject(chat, task));
    }

    /**
     * Sets the delay between messages. Used when the user is a YT Rank / Staff in order to remove unnecessary delays
     * @param delay delay in ticks
     */
    public void setDelay(long delay) { //TODO call this when necessary
        this.delay = delay;
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null) {
            return;
        }

        if (tick < delay) {
            tick++;
        }

        if (tick % delay == 0) {
            final QueueObject poll = commands.poll();
            if (poll == null) {
                return;
            }

            tick = 0;
            Minecraft.getMinecraft().thePlayer.sendChatMessage(poll.getMessage());
            poll.getRunnable().run();
        }
    }

    public void queue(String message) {
        queue(message, EMPTY);
    }

    static class QueueObject {
        final String message;
        final Runnable runnable;

        public QueueObject(String message, Runnable runnable) {
            this.message = message;
            this.runnable = runnable;
        }

        public String getMessage() {
            return message;
        }

        public Runnable getRunnable() {
            return runnable;
        }
    }
}