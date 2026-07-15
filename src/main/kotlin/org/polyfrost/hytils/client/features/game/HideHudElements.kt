package org.polyfrost.hytils.client.features.game

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.hypixel.data.type.GameType
import net.minecraft.world.item.Items
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object HideHudElements {
    private val shouldHide
        get() = HytilsRebornConfig.isEnabled && HytilsRebornConfig.hideHudElements && HypixelUtils.isHypixel()

    private val EMPTY_HUD_ELEMENT: HudElement = { _, _ -> }

    fun init() {
        HudElementRegistry.replaceElement(VanillaHudElements.HEALTH_BAR) { hudElement ->
            if (shouldHide && shouldHideHearts()) EMPTY_HUD_ELEMENT else hudElement
        }

        HudElementRegistry.replaceElement(VanillaHudElements.FOOD_BAR) { hudElement ->
            if (shouldHide && shouldHideHunger()) EMPTY_HUD_ELEMENT else hudElement
        }

        HudElementRegistry.replaceElement(VanillaHudElements.ARMOR_BAR) { hudElement ->
            if (shouldHide && shouldHideArmorBar()) EMPTY_HUD_ELEMENT else hudElement
        }

        HudElementRegistry.replaceElement(VanillaHudElements.AIR_BAR) { hudElement ->
            if (shouldHide && shouldHideAirBubbles()) EMPTY_HUD_ELEMENT else hudElement
        }
    }

    private fun shouldHideHearts(): Boolean {
        val player = mc.player ?: return false
        val location = HypixelUtils.getLocation()
        if (!location.inGame() || location.gameType.isEmpty || location.serverName.orElse(null) == "limbo") {
            if (location.inLobby() && location.gameType.orElse(null) == GameType.DUELS) {
                // check if the player is in the battle pit
                return player.inventory.getItem(8).item != Items.BARRIER
            }

            // rudimentary check for whether the player has engaged in pvp
            return player.health == player.maxHealth
        }

        val gameMode = location.mode.orElse("")

        when (location.gameType.get()) {
            GameType.HOUSING, GameType.MURDER_MYSTERY, GameType.BUILD_BATTLE, GameType.QUAKECRAFT, GameType.REPLAY
                -> return player.health == player.maxHealth

            GameType.TNTGAMES -> {
                if (gameMode.contains("CAPTURE") || gameMode.contains("PVPRUN")) return false
                return player.health == player.maxHealth
            }

            else -> {}
        }

        when (gameMode) {
            "DUELS_PARKOUR", "DUELS_BOWSPLEEF_DUEL", "DUELS_BOXING_DUEL", "PIXEL_PARTY", "PIXEL_PARTY_HYPER",
            "HOLE_IN_THE_WALL", "SOCCER", "DRAW_THEIR_THING", "DROPPER" ->
                return player.health == player.maxHealth

            // game uses lowered health for decoration and does not alter gameplay
            "ENDER" -> return true
        }

        return false
    }

    private fun shouldHideHunger(): Boolean {
        val location = HypixelUtils.getLocation()

        if (!location.inGame() || location.gameType.isEmpty || location.serverName.orElse(null) == "limbo")
            return true

        val gameMode = location.mode.orElse("")

        when (location.gameType.get()) {
            GameType.BEDWARS, GameType.MURDER_MYSTERY, GameType.HOUSING, GameType.PAINTBALL, GameType.PIT, GameType.DUELS,
            GameType.BUILD_BATTLE, GameType.QUAKECRAFT, GameType.WOOL_GAMES, GameType.SKYBLOCK, GameType.REPLAY
                -> return true

            GameType.TNTGAMES -> return !gameMode.contains("CAPTURE")

            else -> {}
        }

        when (gameMode) {
            "PIXEL_PARTY", "PIXEL_PARTY_HYPER", "PVP_CTW", "ZOMBIES_DEAD_END", "ZOMBIES_BAD_BLOOD",
            "ZOMBIES_ALIEN_ARCADIUM", "HIDE_AND_SEEK_PROP_HUNT", "HIDE_AND_SEEK_PARTY_POOPER", "MINI_WALLS", "STARWARS",
            "HOLE_IN_THE_WALL", "SOCCER", "ONEINTHEQUIVER", "DRAW_THEIR_THING", "ENDER", "DROPPER", "DISASTERS"
                -> return true
        }

        return false
    }

    private fun shouldHideArmorBar(): Boolean {
        val location = HypixelUtils.getLocation()

        if (!location.inGame() || location.gameType.isEmpty || location.serverName.orElse(null) == "limbo")
            return true

        val gameMode = location.mode.orElse("")

        when (location.gameType.get()) {
            GameType.MURDER_MYSTERY, GameType.BUILD_BATTLE, GameType.QUAKECRAFT,
            GameType.TNTGAMES, GameType.SKYBLOCK, GameType.REPLAY
                -> return true

            GameType.DUELS -> return !gameMode.contains("DUELS_SW_DUEL") && !gameMode.contains("DUELS_UHC_MEETUP_DUEL")

            else -> {}
        }

        when (gameMode) {
            "SOCCER", "ONEINTHEQUIVER", "ENDER", "DROPPER" -> return true
        }

        return false
    }

    private fun shouldHideAirBubbles(): Boolean {
        val location = HypixelUtils.getLocation()

        if (!location.inGame() || location.gameType.isEmpty || location.serverName.orElse(null) == "limbo")
            return true

        when (location.gameType.get()) {
            GameType.BUILD_BATTLE, GameType.REPLAY -> return true
            GameType.ARCADE -> return location.mode.orElse("").contains("DROPPER")

            else -> {}
        }

        return false
    }
}
