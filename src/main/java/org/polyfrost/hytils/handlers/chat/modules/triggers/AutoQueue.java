package org.polyfrost.hytils.handlers.chat.modules.triggers;

import cc.polyfrost.oneconfig.utils.Multithreading;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.LocrawGamesHandler;
import org.polyfrost.hytils.handlers.cache.PatternHandler;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;

public class AutoQueue
    implements ChatReceiveModule {
    private String command = null;
    private boolean sentCommand;

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (!HytilsConfig.autoQueueGameEnd && !HytilsConfig.autoQueueDie) {
            return;
        }
        String message = this.getStrippedMessage(event.message);
        Matcher matcher = this.getLanguage().autoQueuePrefixGlobalRegex.matcher(message);

        if (((matcher.matches() && HytilsConfig.autoQueueDie) ||
            (this.hasGameEnded(message) && HytilsConfig.autoQueueGameEnd)) &&
            this.getLocraw() != null) {
            String game = this.getLocraw().getGameMode();
            String value = LocrawGamesHandler.locrawGames.get(this.getLocraw().getRawGameType().toLowerCase() + "_" + game.toLowerCase());
            if (value != null) {
                game = value;
            }
            this.command = "/play " + game.toLowerCase();
            this.switchGame();
        }
    }

    private boolean hasGameEnded(String message) {
        if (!PatternHandler.INSTANCE.gameEnd.isEmpty()) {
            for (Pattern triggers : PatternHandler.INSTANCE.gameEnd) {
                if (!triggers.matcher(message).matches()) continue;
                return true;
            }
        }
        return false;
    }

    private void switchGame() {
        Multithreading.schedule(() -> {
            if (!this.sentCommand) {
                HytilsReborn.INSTANCE.getCommandQueue().queue(this.command);
                this.sentCommand = true;
                this.command = null;
            }
            Multithreading.schedule(() -> {
                this.sentCommand = false;
            }, 5L, TimeUnit.SECONDS);
        }, HytilsConfig.autoQueueDelay, TimeUnit.SECONDS);
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoQueueGameEnd || HytilsConfig.autoQueueDie;
    }

    /**
     * We want this to activate early so that it catches the queue message.
     */
    @Override
    public int getPriority() {
        return -11;
    }
}
