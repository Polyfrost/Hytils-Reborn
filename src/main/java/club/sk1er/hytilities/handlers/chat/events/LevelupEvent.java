package club.sk1er.hytilities.handlers.chat.events;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.events.HypixelLevelupEvent;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelupEvent implements ChatModule {

    private final Pattern levelUpPattern = Pattern.compile("You are now Hypixel Level (?<level>\\d+)!");

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        String unformattedText = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

        Matcher matcher = levelUpPattern.matcher(unformattedText.trim());
        if (matcher.find()) {
            String level = matcher.group("level");

            if (StringUtils.isNumeric(level)) {
                MinecraftForge.EVENT_BUS.post(new HypixelLevelupEvent(Integer.parseInt(level)));
            }
        }
    }

    @SubscribeEvent
    public void levelUpEvent(HypixelLevelupEvent event) {
        if (HytilitiesConfig.broadcastLevelup) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/gchat Levelup! I am now Hypixel Level: " + event.getLevel() + "!");
        }
    }
}
