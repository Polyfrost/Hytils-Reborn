package club.sk1er.hytilities.handlers.chat.events;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.events.HypixelAchievementEvent;
import club.sk1er.hytilities.handlers.chat.ChatModule;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class AchievementEvent implements ChatModule {

    private final List<String> achievementsGotten = new ArrayList<>();

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        String unformattedText = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

        Matcher matcher = getLanguage().achievementRegex.matcher(unformattedText);
        if (matcher.matches()) {
            String achievement = matcher.group("achievement");

            if (!achievementsGotten.contains(achievement)) {
                MinecraftForge.EVENT_BUS.post(new HypixelAchievementEvent(achievement));

                // check to stop spamming of guild chat if achievement is broken and you get it many times
                achievementsGotten.add(achievement);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @SubscribeEvent
    public void onAchievementGet(HypixelAchievementEvent event) {
        if (HytilitiesConfig.broadcastAchievements) {
            Hytilities.INSTANCE.getCommandQueue().queue("/gchat Achievement unlocked! I unlocked the " + event.getAchievement() + " achievement!");
        }
    }
}
