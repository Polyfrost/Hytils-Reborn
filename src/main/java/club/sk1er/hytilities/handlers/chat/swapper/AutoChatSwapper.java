package club.sk1er.hytilities.handlers.chat.swapper;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import club.sk1er.mods.core.util.Multithreading;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

public class AutoChatSwapper implements ChatModule {

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        final Matcher statusMatcher = getLanguage().autoChatSwapperPartyStatusRegex.matcher(event.message.getUnformattedText());
        if (statusMatcher.matches()) {
            MinecraftForge.EVENT_BUS.register(new ChatChannelMessagePreventer());
            switch (HytilitiesConfig.chatSwapperReturnChannel) {
                case 0:
                default:
                    Hytilities.INSTANCE.getCommandQueue().queue("/chat a");
                    break;
                case 1:
                    Hytilities.INSTANCE.getCommandQueue().queue("/chat g");
                    break;
                case 2:
                    Hytilities.INSTANCE.getCommandQueue().queue("/chat o");
                    break;
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.chatSwapper;
    }

    public static class ChatChannelMessagePreventer {

        private boolean hasDetected;
        private final ScheduledFuture<?> unregisterTimer;

        ChatChannelMessagePreventer() { // if the message somehow doesn't send, we unregister after 20 seconds
            this.unregisterTimer = Multithreading.schedule(() -> { // to prevent blocking the next time it's used
                if (!this.hasDetected) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }, 20, TimeUnit.SECONDS);
        }


        @SubscribeEvent
        public void checkForAlreadyInThisChannelThing(ClientChatReceivedEvent event) {
            if (Hytilities.INSTANCE.getLanguageHandler().getCurrent().autoChatSwapperAlreadyInChannel.equals(event.message.getUnformattedText())
                || (HytilitiesConfig.hideAllChatMessage &&
                Hytilities.INSTANCE.getLanguageHandler().getCurrent().autoChatSwapperChannelSwapRegex.matcher(event.message.getUnformattedText()).matches())
            ) {
                unregisterTimer.cancel(false);
                hasDetected = true;
                event.setCanceled(true);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }

}
