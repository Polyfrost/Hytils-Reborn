package org.polyfrost.hytils.handlers.chat.modules.triggers;

import cc.polyfrost.oneconfig.events.event.WorldLoadEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.PatternHandler;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class AutoGG implements ChatReceiveModule {
    public static AutoGG INSTANCE = new AutoGG();
    private boolean matchFound = false;

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        if (!hasGameEnded(message)) {
            return;
        }

        if (!matchFound) {
            matchFound = true;
            String ggMessage1 = "random".equals(HytilsConfig.ggMessage) ? getRandomGG() : HytilsConfig.ggMessage;
            String ggMessage2 = "random".equals(HytilsConfig.ggMessage2) ? getRandomGG() : HytilsConfig.ggMessage2;

            Multithreading.schedule(() -> UChat.say("/ac " + ggMessage1), (long) (HytilsConfig.autoGGFirstPhraseDelay * 1000), TimeUnit.MILLISECONDS);
            if (HytilsConfig.autoGGSecondMessage) {
                Multithreading.schedule(() -> UChat.say("/ac " + ggMessage2), (long) ((HytilsConfig.autoGGSecondPhraseDelay + HytilsConfig.autoGGFirstPhraseDelay) * 1000), TimeUnit.MILLISECONDS);
            }
            // Schedule the reset of matchFound after the second message has been sent
            Multithreading.schedule(() -> matchFound = false, (long) ((HytilsConfig.autoGGSecondPhraseDelay + HytilsConfig.autoGGFirstPhraseDelay) * 1000) + 5000, TimeUnit.MILLISECONDS);
        }
    }

    private boolean hasGameEnded(String message) {
        if (!matchFound && !PatternHandler.INSTANCE.gameEnd.isEmpty()) {
            for (Pattern triggers : PatternHandler.INSTANCE.gameEnd) {
                if (triggers.matcher(message).matches()) {
                    return true;
                }
            }
        }

        // TODO: UNTESTED!
        return getLanguage().casualGameEndRegex.matcher(message).matches();
    }

    @Subscribe
    public void onWorldLoad(WorldLoadEvent event) {
        matchFound = false;
    }

    public boolean isEnabled() {
        return HytilsConfig.autoGG && (!HytilsReborn.INSTANCE.isSk1erAutoGG || !club.sk1er.mods.autogg.AutoGG.INSTANCE.getAutoGGConfig().isModEnabled()); // If Sk1er's AutoGG is enabled, we don't want to interfere with it.
    }

    public int getPriority() {
        return 3;
    }

    private static final Random random = new Random();

    public static String getRandomGG() {
        String base;

        int roll = random.nextInt(258);

        if (roll < 2) {
            base = "gg";
        } else if (roll < 130) {
            base = "goodgame";
        } else {
            base = "good game";
        }

        char[] chars = base.toCharArray();
        int capsCount = 0;

        for (int i = 0; i < chars.length; i++) {
            if (!Character.isLetter(chars[i])) continue; // Skip spaces

            if (random.nextBoolean()) {
                chars[i] = Character.toUpperCase(chars[i]);
                capsCount++;
            } else {
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }

        if (capsCount % 2 != 0) {
            for (int i = chars.length - 1; i >= 0; i--) {
                if (Character.isLetter(chars[i])) {
                    // Flip case of the last letter
                    if (Character.isUpperCase(chars[i])) {
                        chars[i] = Character.toLowerCase(chars[i]);
                    } else {
                        chars[i] = Character.toUpperCase(chars[i]);
                    }
                    break;
                }
            }
        }

        return new String(chars);
    }
}
