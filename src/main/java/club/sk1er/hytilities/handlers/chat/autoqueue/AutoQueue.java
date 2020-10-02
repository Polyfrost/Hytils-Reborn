package club.sk1er.hytilities.handlers.chat.autoqueue;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import club.sk1er.mods.core.universal.ChatColor;
import club.sk1er.mods.core.util.Multithreading;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.concurrent.TimeUnit;

public class AutoQueue implements ChatModule {
    private String command = null;
    private boolean sentCommand;

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        if (!HytilitiesConfig.autoQueue) {
            return;
        }

        String message = ChatColor.stripColor(event.message.getUnformattedText());
        if (message.startsWith("You died! Want to play again?")) {
            for (IChatComponent component : event.message.getSiblings()) {
                String compMsg = ChatColor.stripColor(component.getUnformattedText().trim());
                if (compMsg.equals("Click here!")) {
                    this.command = component.getChatStyle().getChatClickEvent().getValue();
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.autoQueue;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (this.command != null) {
            this.switchGame();
        }
    }

    @SubscribeEvent
    public void onMouseEvent(InputEvent.MouseInputEvent event) {
        if (this.command != null) {
            this.switchGame();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        // stop the command from being spammed, to prevent chat from filling with "please do not spam commands"
        if (event.world.provider.getDimensionId() == 0 && this.sentCommand) {
            this.sentCommand = false;
        }
    }

    private void switchGame() {
        Multithreading.schedule(() -> {
            if (!this.sentCommand) {
                Hytilities.INSTANCE.getCommandQueue().queue(this.command);
                this.sentCommand = true;
                this.command = null;
            }

        }, HytilitiesConfig.autoQueueDelay, TimeUnit.SECONDS);
    }
}
