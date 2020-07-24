package club.sk1er.hytilities.handlers.cleaner;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.mods.core.universal.ChatColor;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCleaner {

    private final List<String> joinMessageTypes = Arrays.asList(
        "joined the lobby", // normal
        "spooked in the lobby" // halloween
    );

    private final Pattern mysteryBoxFind = Pattern.compile("(?<player>[a-zA-Z0-9_]{1,16}) found a .+ Mystery Box!");
    private final Pattern gameAnnouncement = Pattern.compile("➤ A .+ game is available to join! CLICK HERE to join!");

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!MinecraftUtils.isHypixel()) {
            return;
        }

        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

        System.out.println("message =" + message);

        if (HytilitiesConfig.hytilitiesLobbyStatuses) {
            for (String messages : joinMessageTypes) {
                if (message.contains(messages)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        // todo: rewrite
        // friends list sends as one message
        // cancelling this entirely removes the friends list
        // handle special cases, effectively stripping breakers
        // instead of entirely removing the message from chat.
        if (HytilitiesConfig.hytilitiesLineBreaker) {
            if (message.contains("-------") || message.contains("=======")) {
                event.setCanceled(true);
                return;
            }
        }

        if (HytilitiesConfig.hytilitiesMysteryBoxAnnouncer) {
            Matcher matcher = mysteryBoxFind.matcher(message);

            if (matcher.find()) {
                String player = matcher.group("player");
                boolean playerBox = !player.contains(Minecraft.getMinecraft().thePlayer.getName());

                if (!playerBox && !player.startsWith("You")) {
                    event.setCanceled(true);
                    return;
                }
            } else if (message.startsWith("[Mystery Box]")) {
                event.setCanceled(true);
                return;
            }
        }

        if (HytilitiesConfig.hytilitiesGameAnnouncements) {
            Matcher matcher = gameAnnouncement.matcher(message);

            if (matcher.find()) {
                event.setCanceled(true);
                return;
            }
        }

        if (HytilitiesConfig.hytilitiesHypeLimitReminder) {
            if (message.startsWith("  ➤ You have reached your Hype limit!")) {
                event.setCanceled(true);
            }
        }
    }
}
