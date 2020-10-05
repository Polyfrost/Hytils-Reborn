package club.sk1er.hytilities.handlers.lobby.tab;

import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.tweaker.asm.GuiPlayerTabOverlayTransformer;
import club.sk1er.mods.core.util.MinecraftUtils;
import org.objectweb.asm.tree.ClassNode;

public class GuildTagHider {

    /**
     * Used in {@link GuiPlayerTabOverlayTransformer#transform(ClassNode, String)}
     */
    @SuppressWarnings("unused")
    public static String hideTabGuildTags(String name) {
        if (MinecraftUtils.isHypixel() && HytilitiesConfig.hideGuildTagsInTab && name.endsWith("]")) {
            // e.g. §b[MVP+] Steve §6[GUILD]
            return name.substring(0, name.lastIndexOf("[") - 3);
        } else {
            return name;
        }
    }
}
