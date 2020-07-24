package club.sk1er.hytilities.util;

import club.sk1er.mods.core.util.MinecraftUtils;
import club.sk1er.mods.core.util.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Taken from LobbyGlow (by biscut)/LobbySounds
 */
public class LobbyChecker {

    private static final String COMPASS_NAME = EnumChatFormatting.GREEN + "Game Menu " + EnumChatFormatting.GRAY + "(Right Click)";
    private static final String COMPASS_LORE =
        EnumChatFormatting.DARK_PURPLE.toString() + EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.GRAY + "Right Click to bring up the Game Menu!";
    private static final Minecraft mc = Minecraft.getMinecraft();

    private static boolean lobbyStatus;

    public static void runLobbyCheckerTimer() {
        if (!MinecraftUtils.isHypixel()) {
            lobbyStatus = false;
            return;
        }

        Multithreading.schedule(LobbyChecker::checkForItem, 0, 3, TimeUnit.SECONDS);
    }

    private static void checkForItem() {
        if (mc.thePlayer != null && mc.thePlayer.inventory != null) {
            ItemStack compass = mc.thePlayer.inventory.getStackInSlot(0);

            if (compass != null && compass.hasDisplayName() && compass.getDisplayName().equals(COMPASS_NAME)) {
                List<String> tooltip = compass.getTooltip(mc.thePlayer, false);

                lobbyStatus = tooltip.get(1).equals(COMPASS_LORE);
            } else {
                lobbyStatus = false;
            }
        }
    }

    public static boolean playerIsInLobby() {
        return lobbyStatus;
    }
}
