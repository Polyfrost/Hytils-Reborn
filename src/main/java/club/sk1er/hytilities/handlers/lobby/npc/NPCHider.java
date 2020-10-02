package club.sk1er.hytilities.handlers.lobby.npc;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.tweaker.asm.GuiPlayerTabOverlayTransformer;
import club.sk1er.mods.core.util.MinecraftUtils;
import com.google.common.collect.Collections2;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.objectweb.asm.tree.ClassNode;

import java.util.Collection;

public class NPCHider {

    @SubscribeEvent
    public void onEntityRender(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (!HytilitiesConfig.npcHider || !MinecraftUtils.isHypixel() || !Hytilities.INSTANCE.getLobbyChecker().playerIsInLobby()) {
            return;
        }

        // hypixel marks npc uuids as version 2, also hide the Quest Master
        if (event.entity.getUniqueID().version() == 2 || (event.entity instanceof EntityVillager)) {
            event.setCanceled(true);
        }
    }

    /**
     * Used in {@link GuiPlayerTabOverlayTransformer#transform(ClassNode, String)}
     */
    @SuppressWarnings("unused")
    public static Collection<NetworkPlayerInfo> hideTabNpcs(Collection<NetworkPlayerInfo> playerInfoCollection) {
        if (!MinecraftUtils.isHypixel() || !HytilitiesConfig.hideNpcsInTab) {
            return playerInfoCollection;
        } else {
            return Collections2.filter(playerInfoCollection, player -> player != null && player.getGameProfile().getId().version() != 2);
        }
    }
}
