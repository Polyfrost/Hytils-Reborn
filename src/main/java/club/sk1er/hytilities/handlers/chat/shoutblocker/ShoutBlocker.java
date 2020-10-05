package club.sk1er.hytilities.handlers.chat.shoutblocker;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatSendModule;
import club.sk1er.hytilities.handlers.chat.ChatReceiveModule;
import club.sk1er.hytilities.handlers.game.GameType;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.text.DecimalFormat;

/**
 * Blocks using /shout in Bedwars and Skywars modes, where there are multiple people in 1 team and also in Capture The Wool.
 * If there are more modes supporting /shout, feel free to add support for them.
 */
public class ShoutBlocker implements ChatSendModule, ChatReceiveModule {
    private long shoutCooldown = 0L;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.#"); // only 1 decimal

    @Override
    public boolean shouldSendMessage(String message) {
        if (message.startsWith("/shout ")) {
            if (shoutCooldown < System.currentTimeMillis()) {
                shoutCooldown = System.currentTimeMillis() + (getCooldownLengthInSeconds() * 1000L);
                return true;
            } else {
                long secondsLeft = (shoutCooldown - System.currentTimeMillis()) / 1000L;
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Hytilities blocked you from shouting. The chat cooldown is "+decimalFormat.format(secondsLeft)+" more second"+(secondsLeft == 1 ? "." : "s.")));
                return false;
            }
        }
        return true;
    }

    private long getCooldownLengthInSeconds() {
        LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (locraw != null && locraw.getGameType() != null && !"LOBBY".equals(locraw.getGameMode())) {
            if (locraw.getGameType() == GameType.BED_WARS && !locraw.getGameMode().equals("BEDWARS_EIGHT_ONE")) return 60L;
            else if (locraw.getGameType() == GameType.SKY_WARS) return 3L;
            else if (locraw.getGameType() == GameType.ARCADE_GAMES && locraw.getGameMode().equals("PVP_CTW")) return 10L;
        }
        return 0L;
    }

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (locraw != null && (
            (locraw.getGameType() == GameType.SKY_WARS && event.message.getFormattedText().equals(getLanguage().cannotShoutBeforeSkywars)) || // fun fact: there is no message when you shout after a skywars game
            event.message.getFormattedText().equals(getLanguage().cannotShoutAfterGame) ||
            event.message.getFormattedText().equals(getLanguage().cannotShoutBeforeGame) ||
            event.message.getFormattedText().equals(getLanguage().noSpectatorCommands)
        )) {
            shoutCooldown = 0L;
        }
    }

    @Override
    public boolean isReceiveModuleEnabled() {
        return HytilitiesConfig.preventShoutingOnCooldown;
    }

    @Override
    public boolean isSendModuleEnabled() {
        return HytilitiesConfig.preventShoutingOnCooldown;
    }
}
