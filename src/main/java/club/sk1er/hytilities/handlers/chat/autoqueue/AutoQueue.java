package club.sk1er.hytilities.handlers.chat.autoqueue;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import club.sk1er.mods.core.universal.ChatColor;
import club.sk1er.mods.core.util.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.List;

public class AutoQueue implements ChatModule {
    private String command = null;

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        String message = ChatColor.stripColor(event.message.getUnformattedText());

        if (message.startsWith("You died! Want to play again?") && HytilitiesConfig.autoQueue) {
            List<IChatComponent> componentList = event.message.getSiblings();
            for (IChatComponent component : componentList) {
                String compMsg = ChatColor.stripColor(component.getUnformattedText());
                if (compMsg.equals(" Click here! ")) {
                    command = component.getChatStyle().getChatClickEvent().getValue();
                }
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (command != null) {
            switchGame();
        }
    }

    @SubscribeEvent
    public void onMouseEvent(InputEvent.MouseInputEvent event) {
        if (command != null) {
            switchGame();
        }
    }

    private void switchGame() {
        Multithreading.runAsync(() -> {
            try {
                Thread.sleep(HytilitiesConfig.autoQueueDelay * 1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
            command = null;
        });
    }
}