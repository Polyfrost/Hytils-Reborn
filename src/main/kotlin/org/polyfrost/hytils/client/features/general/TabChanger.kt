package org.polyfrost.hytils.client.features.general

import net.hypixel.data.type.GameType
import net.minecraft.ChatFormatting
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object TabChanger {
    @JvmStatic
    fun modifyName(component: MutableComponent, playerInfo: PlayerInfo): MutableComponent {
        val location = HypixelUtils.getLocation()

        if (HytilsRebornConfig.hidePlayerRanksInTab && !location.inGame()) {
            if (component.siblings.isNotEmpty() && component.string.startsWith("[", 2)) {
                component.siblings.removeFirst()
            }
        }

        if (HytilsRebornConfig.hideGuildTagsInTab && !location.inGame()) {
            if (component.siblings.isNotEmpty() && component.string.endsWith("]")) {
                component.siblings.removeLast()
            }
        }

        if (HytilsRebornConfig.highlightSelfInTab != 0 && playerInfo.profile.id == mc.player?.uuid) {
            when (HytilsRebornConfig.highlightSelfInTab) {
                1 -> return Component.empty()
                    .append(Component.literal("✯ ").withStyle(ChatFormatting.DARK_PURPLE))
                    .append(component)

                2 -> return Component.empty()
                    .append(component)
                    .append(Component.literal(" ✯").withStyle(ChatFormatting.DARK_PURPLE))
            }
        }

        return component
    }

    @JvmStatic
    fun modifyHeader(header: Component): Component {
        if (!HytilsRebornConfig.hideAdsInTab) return header
        val header = header.copy()
        header.siblings.removeIf { it == LanguageData.HEADER_ADVERTISEMENT }
        return header
    }

    @JvmStatic
    fun modifyFooter(footer: Component): Component {
        if (!HytilsRebornConfig.hideAdsInTab) return footer

        val footer = footer.copy()
        footer.siblings.removeIf { sibling ->
            sibling == LanguageData.FOOTER_ADVERTISEMENT || sibling.siblings.contains(LanguageData.FOOTER_ADVERTISEMENT)
        }

        // trim any newlines/whitespace-only siblings left behind
        while (footer.siblings.isNotEmpty() && footer.siblings.last().string.isBlank()) {
            footer.siblings.removeLast()
        }

        return footer
    }

    @JvmStatic
    fun hidePing(playerInfo: PlayerInfo): Boolean {
        return HypixelUtils.isHypixel()
                && ((HytilsRebornConfig.hidePingInTab && HypixelUtils.getLocation().inGame())
                || isSkyblockTabInformationEntry(playerInfo))
    }

    @JvmStatic
    fun shouldRenderHead(playerInfo: PlayerInfo): Boolean {
        return !HypixelUtils.isHypixel() || !isSkyblockTabInformationEntry(playerInfo)
    }

    private fun isSkyblockTabInformationEntry(playerInfo: PlayerInfo): Boolean {
        if (!HytilsRebornConfig.cleanerSkyblockTabInfo) return false
        return HypixelUtils.getLocation().gameType.orElse(null) == GameType.SKYBLOCK
                //~ if <1.21.11 '.name()' -> '.name'
                && LanguageData.SKYBLOCK_TAB_INFO_ENTRY_NAME.matches(playerInfo.profile.name())
                && !LanguageData.SKYBLOCK_TAB_PLAYER_ENTRY_NAME.matches(playerInfo.tabListDisplayName?.string ?: "")
    }
}
