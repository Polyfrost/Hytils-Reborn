package club.sk1er.hytilities.handlers.cleaner;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.mods.core.universal.ChatColor;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
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
    private final Pattern soulBoxFind = Pattern.compile(".+ has found .+ in the Soul Well!");
    private final Pattern gameAnnouncement = Pattern.compile("➤ A .+ game is available to join! CLICK HERE to join!");

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!MinecraftUtils.isHypixel() || event.isCanceled()) {
            return;
        }

        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

        if (HytilitiesConfig.hytilitiesLobbyStatuses) {
            for (String messages : joinMessageTypes) {
                if (message.contains(messages)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        // todo: figure out why chat events don't copy-over
        if (HytilitiesConfig.hytilitiesLineBreaker) {
            if (message.contains("-----------") && message.contains("\n")) {
                event.message = new ChatComponentText(reformatMessage(event.message.getFormattedText()));
                return;
            } else if (message.contains("-----------")){
                event.setCanceled(true);
                return;
            }
        }

        if (HytilitiesConfig.hytilitiesMysteryBoxAnnouncer) {
            Matcher matcher = mysteryBoxFind.matcher(message);

            if (matcher.find()) {
                String player = matcher.group("player");
                boolean playerBox = !player.contains(Minecraft.getMinecraft().thePlayer.getName());

                if (!playerBox || !player.startsWith("You")) {
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

        if (HytilitiesConfig.hytilitiesHypeLimitReminder && message.startsWith("  ➤ You have reached your Hype limit!")) {
            event.setCanceled(true);
            return;
        }

        if (HytilitiesConfig.hytilitiesSoulBoxAnnouncer) {
            Matcher matcher = soulBoxFind.matcher(message);

            if (matcher.find()) {
                event.setCanceled(true);
            }
        }
    }

    // taken from ToggleChat
    private String reformatMessage(String formattedText) {
        if (formattedText.contains("▬▬")) {
            formattedText = formattedText
                .replace("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "")
                .replace("▬▬", "");
            return formattedText;
        } else if (formattedText.contains("---")) {
            formattedText = formattedText
                .replace("----------------------------------------------------\n", "");
            return formattedText.replace("--\n", "").replace("\n--", "").replace("-", "");
        }

        return formattedText;
    }
}
