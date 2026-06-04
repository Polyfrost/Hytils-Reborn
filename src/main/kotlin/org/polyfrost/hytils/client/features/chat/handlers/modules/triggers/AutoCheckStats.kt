package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import net.hypixel.data.type.GameType
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.events.TitleEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.HypixelAPIUtils
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import java.util.*

// TODO: notifications
object AutoCheckStats : ChatReceiveModule {
    var gameEnded = false

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!gameEnded && event.unformattedMessage.matches(LanguageData.GAME_END)) {
            showNotification()
        }
    }

    @Subscribe
    fun onTitle(event: TitleEvent) {
        if (!gameEnded) {
            val title = event.unformattedTitle.uppercase(Locale.ROOT)
            if (title == "VICTORY!" || title == "GAME OVER" || title == "GAME OVER!" || title.endsWith(" WINS!")) {
                showNotification()
            }
        }
    }

    private fun showNotification() {
        gameEnded = true

//        if (HytilsRebornConfig.autoGetGEXP) {
//            try {
//                val gexp = when (HytilsRebornConfig.gexpMode) {
//                    1 -> HypixelAPIUtils.getWeeklyGEXP()
//                    else -> HypixelAPIUtils.getGEXP()
//                }
//                Notifications.enqueue(
//                    Notifications.Type.Info,
//                    HytilsRebornConstants.NAME,
//                    "You currently have $gexp ${if (HytilsRebornConfig.gexpMode == 0) "daily" else "weekly"} guild EXP."
//                )
//            } catch (_: Exception) {
//                Notifications.enqueue(
//                    Notifications.Type.Error,
//                    HytilsRebornConstants.NAME,
//                    "There was a problem trying to get your GEXP."
//                )
//            }
//        }

//        if (HytilsRebornConfig.autoGetWinstreak && isSupportedMode()) {
//            try {
//                Notifications.enqueue(
//                    Notifications.Type.Info,
//                    HytilsRebornConstants.NAME,
//                    "You currently have a ${HypixelAPIUtils.getWinstreak()} winstreak."
//                )
//            } catch (_: Exception) {
//                Notifications.enqueue(
//                    Notifications.Type.Error,
//                    HytilsRebornConstants.NAME,
//                    "There was a problem trying to get your winstreak."
//                )
//            }
//        }
    }

    private fun isSupportedMode(): Boolean {
        val location = HypixelUtils.getLocation()
        if (location.gameType.isPresent) {
            return when (location.gameType.get()) {
                GameType.BEDWARS, GameType.SKYWARS, GameType.DUELS -> true
                else -> false
            }
        }
        return false
    }

    override val isEnabled
        get() = HytilsRebornConfig.autoGetGEXP || HytilsRebornConfig.autoGetWinstreak
}
