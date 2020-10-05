/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.sk1er.hytilities.handlers.general;

import net.minecraft.client.Minecraft;
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

    public void queue(String chat, Runnable task) {
        commands.add(new QueueObject(chat, task));
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        if (tick < delay) {
            tick++;
        }

        if (tick % delay == 0) {
            final QueueObject poll = commands.poll();
            if (poll == null || poll.message.isEmpty()) {
                return;
            }

            tick = 0;

            if (Minecraft.getMinecraft().thePlayer != null) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage(poll.message);
            }

            poll.runnable.run();
        }
    }

    public void queue(String message) {
        queue(message, EMPTY);
    }

    /**
     * Sets the delay between messages. Used when the user is a YT Rank / Staff in order to remove unnecessary delays
     *
     * @param delay delay in ticks
     */
    public void setDelay(long delay) { //TODO call this when necessary
        this.delay = delay;
    }

    static class QueueObject {
        final String message;
        final Runnable runnable;

        public QueueObject(String message, Runnable runnable) {
            this.message = message;
            this.runnable = runnable;
        }
    }
}
