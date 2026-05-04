package org.polyfrost.hytils.client.utils

import com.google.gson.JsonObject
import com.mojang.authlib.exceptions.AuthenticationException
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCTextStyle
import dev.deftu.textile.minecraft.TextColors
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.JsonUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

// FIXME: Polyfrost's Ursa Minor instance seems to be broken
object HypixelAPIUtils {
    private const val API_BASE = "https://api.polyfrost.org/ursa/v1/hypixel"

    private var token: String? = null
    private var expiry: Instant? = null
    private var username: String? = null
    private var serverId: String? = null

    private val httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build()

    private val playerUUID: String
        get() = mc.user.profileId.toString().replace("-", "")

    /**
     * Gets the player's guild experience.
     */
    fun getGEXP(): String? = getGEXPFromUUID(playerUUID)

    /**
     * Gets the specified player's guild experience.
     */
    fun getGEXP(username: String): String? {
        val uuid = getUUID(username) ?: return null
        return getGEXPFromUUID(uuid)
    }

    private fun getGEXPFromUUID(uuid: String): String? {
        val jsonObject = getJsonObjectAuth("$API_BASE/guild/$uuid") ?: return null
        val guildMembers = jsonObject.getAsJsonObject("guild")?.getAsJsonArray("members") ?: return null

        val currentDate = LocalDate.now(ZoneId.of("America/New_York")).toString()

        for (e in guildMembers) {
            val member = e.asJsonObject
            if (member.get("uuid").asString == uuid) {
                return member.getAsJsonObject("expHistory")?.get(currentDate)?.asInt?.toString()
            }
        }

        return null
    }

    /**
     * Gets the player's weekly guild experience.
     */
    fun getWeeklyGEXP(): String? = getWeeklyGEXPFromUUID(playerUUID)

    /**
     * Gets the specified player's weekly guild experience.
     */
    fun getWeeklyGEXP(username: String): String? {
        val uuid = getUUID(username) ?: return null
        return getWeeklyGEXPFromUUID(uuid)
    }

    private fun getWeeklyGEXPFromUUID(uuid: String): String? {
        val jsonObject = getJsonObjectAuth("$API_BASE/guild/$uuid") ?: return null
        val guildMembers = jsonObject.getAsJsonObject("guild")?.getAsJsonArray("members") ?: return null

        for (e in guildMembers) {
            val member = e.asJsonObject
            if (member.get("uuid").asString == uuid) {
                val expHistory = member.getAsJsonObject("expHistory") ?: return null
                return expHistory.entrySet().sumOf { it.value.asInt }.toString()
            }
        }
        return null
    }

    /**
     * Gets the player's current winstreak.
     */
    fun getWinstreak(): String? = getWinstreakFromUUID(playerUUID)

    /**
     * Gets the specified player's current winstreak for the current game.
     */
    fun getWinstreak(username: String): String? {
        val uuid = getUUID(username) ?: return null
        return getWinstreakFromUUID(uuid)
    }

    /**
     * Gets the specified player's current winstreak for the specified game.
     */
    fun getWinstreak(username: String, game: String?): String? {
        val uuid = getUUID(username) ?: return null
        return getWinstreakFromUUID(uuid, game)
    }

    private fun getWinstreakFromUUID(uuid: String, game: String? = null): String? {
        val jsonObject = getJsonObjectAuth("$API_BASE/player/$uuid") ?: return null
        val playerStats = jsonObject.getAsJsonObject("player")?.getAsJsonObject("stats") ?: return null

        val gameToUse = game
            ?: HypixelUtils.getLocation().gameType.orElse(null)?.name
            ?: return null

        return try {
            when (gameToUse.lowercase(Locale.ROOT)) {
                "bedwars" -> playerStats.getAsJsonObject("Bedwars")?.get("winstreak")?.asInt?.toString()
                "skywars" -> playerStats.getAsJsonObject("SkyWars")?.get("win_streak")?.asInt?.toString()
                "duels" -> playerStats.getAsJsonObject("Duels")?.get("current_winstreak")?.asInt?.toString()
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Gets the Hypixel rank of the specified player.
     *
     * @return Player rank, [RankType.UNKNOWN] if the player does not exist or the API key is empty
     */
    fun getRank(username: String): RankType {
        val uuid = getUUID(username) ?: return RankType.UNKNOWN
        try {
            val jsonObject = getJsonObjectAuth("$API_BASE/player/$uuid") ?: return RankType.UNKNOWN
            val playerNode = jsonObject.getAsJsonObject("player") ?: return RankType.UNKNOWN

            for (value in arrayOf("rank", "monthlyPackageRank", "newPackageRank", "packageRank")) {
                if (playerNode.has(value)) {
                    val rankStr = playerNode.get(value).asString
                    if (!rankStr.matches("NONE|NORMAL".toRegex())) {
                        return RankType.getRank(rankStr)
                    }
                }
            }

            return RankType.NON
        } catch (e: Exception) {
            e.printStackTrace()
            return RankType.UNKNOWN
        }
    }

    /**
     * Gets a UUID based on the username provided.
     */
    fun getUUID(username: String): String? {
        try {
            val maybeUuidResponse = JsonUtils.parseFromUrl("https://api.mojang.com/users/profiles/minecraft/$username")
            if (maybeUuidResponse != null && maybeUuidResponse.isJsonObject) {
                val uuidResponse = maybeUuidResponse.asJsonObject
                if (uuidResponse.has("error")) {
                    OmniClientChat.displayChatMessage(
                        Text.literal("Failed with error: ${uuidResponse.get("reason").asString}")
                            .setStyle(MCTextStyle.color(TextColors.RED))
                    )
                    return null
                }
                return uuidResponse.get("id").asString
            }
        } catch (_: Exception) {
            OmniClientChat.displayChatMessage(
                Text.literal("Failed to fetch $username's data. Please make sure this user exists.")
                    .setStyle(MCTextStyle.color(TextColors.RED))
            )
        }
        return null
    }

    private fun authorize(): Boolean {
        try {
            val serverId = UUID.randomUUID().toString()
            //~ if <1.21.11 '.services().sessionService' -> '.minecraftSessionService'
            mc.services().sessionService.joinServer(mc.user.profileId, mc.user.accessToken, serverId)
            this.username = mc.user.name
            this.serverId = serverId
            return true
        } catch (e: AuthenticationException) {
            e.printStackTrace()
            return false
        }
    }

    @Suppress("UastIncorrectHttpHeaderInspection")
    private fun getJsonObjectAuth(url: String): JsonObject? {
        val requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .timeout(Duration.ofSeconds(5))
            .header("User-Agent", "Hytils-Reborn/${HytilsRebornConstants.VERSION}")

        if (token != null && expiry != null && expiry!!.isAfter(Instant.now())) {
            requestBuilder.header("x-ursa-token", token)
        } else {
            token = null
            expiry = null
            if (authorize()) {
                requestBuilder.header("x-ursa-username", username)
                requestBuilder.header("x-ursa-serverid", serverId)
            } else {
                return null
            }
        }

        val request = requestBuilder.build()

        return try {
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))

            if (username != null && serverId != null) {
                response.headers().firstValue("x-ursa-token").ifPresent { token = it }
                response.headers().firstValue("x-ursa-expires").ifPresent { expiryStr ->
                    expiry = calculateExpiry(expiryStr)
                }
                username = null
                serverId = null
            }

            if (response.statusCode() !in 200..299) {
                return null
            }

            val element = JsonUtils.parse(response.body())
            return element.takeIf { it.isJsonObject }?.asJsonObject
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun calculateExpiry(expiryStr: String?): Instant {
        if (expiryStr == null) return Instant.now().plus(Duration.ofMinutes(55))
        return try {
            Instant.ofEpochMilli(expiryStr.toLong())
        } catch (_: NumberFormatException) {
            Instant.now().plus(Duration.ofMinutes(55))
        }
    }

    enum class RankType(private val rank: String) {
        UNKNOWN("UNKNOWN"),
        NON("DEFAULT"),
        VIP("VIP"),
        VIP_PLUS("VIP_PLUS"),
        MVP("MVP"),
        MVP_PLUS("MVP_PLUS"),
        MVP_PLUS_PLUS("SUPERSTAR"),
        YOUTUBE("YOUTUBER"),
        GAME_MASTER("GAME_MASTER"),
        ADMIN("ADMIN");

        companion object {
            fun getRank(rank: String?): RankType {
                if (rank == null) return UNKNOWN
                return entries.find { it.rank == rank } ?: UNKNOWN
            }
        }
    }
}
