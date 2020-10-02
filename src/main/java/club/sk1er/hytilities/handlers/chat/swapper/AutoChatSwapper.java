package club.sk1er.hytilities.handlers.chat.swapper;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import club.sk1er.mods.core.util.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoChatSwapper implements ChatModule {

    private final Pattern partyStatusRegex = Pattern.compile("^(?:" +
            "You have been kicked from the party by (?:\\[.+] )?\\w{1,16}|" +
            "(?:\\[.+] )?\\w{1,16} has disbanded the party!|" +
            "You left the party." +
    ")$");

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        final Matcher statusMatcher = this.partyStatusRegex.matcher(event.message.getUnformattedText());
        if (statusMatcher.matches()) {
            MinecraftForge.EVENT_BUS.register(new ChatChannelMessagePreventer());

            switch (HytilitiesConfig.chatSwapperReturnChannel) {
                case 1:
                    Hytilities.INSTANCE.getCommandQueue().queue("/chat g");
                    break;
                case 2:
                    Hytilities.INSTANCE.getCommandQueue().queue("/chat o");
                    break;
                default:
                case 0:
                    Hytilities.INSTANCE.getCommandQueue().queue("/chat a");
                    break;
            }

        }
    }

    @Override
    public boolean condition() {
        return HytilitiesConfig.chatSwapper;
    }

    public static class ChatChannelMessagePreventer {

        private boolean hasDetected;
        private final ScheduledFuture<?> unregisterTimer;
        private final Pattern channelSwapRegex = Pattern.compile("^(?:You are now in the (?<channel>ALL|GUILD|OFFICER) channel)");

        ChatChannelMessagePreventer() { // if the message somehow doesn't send, we unregister after 20 seconds
            unregisterTimer = Multithreading.schedule(() -> { // to prevent blocking the next time it's used
                if (!hasDetected) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }, 20, TimeUnit.SECONDS);
        }


        @SubscribeEvent
        public void checkForAlreadyInThisChannelThing(ClientChatReceivedEvent event) {
            if ("You're already in this channel!".equals(event.message.getUnformattedText())
                    || (HytilitiesConfig.hideAllChatMessage &&
                    channelSwapRegex.matcher(event.message.getUnformattedText()).matches())
            ) {
                unregisterTimer.cancel(false);
                hasDetected = true;
                event.setCanceled(true);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }

}
