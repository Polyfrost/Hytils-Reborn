package org.polyfrost.hytils.client.data.providers

import com.google.gson.JsonElement
import org.polyfrost.hytils.client.data.DataProvider
import org.polyfrost.oneconfig.utils.v1.JsonUtils

object ArmorStandData : DataProvider {
    @Volatile var armorStandNames = setOf(
        "click", "mystery vault", "daily reward tokens", "advent calendar reward", "bug fix patch", "update",
        "release", "free rewards", "special holiday quests", "hype", "coming soon", " set #", "parkour starts this way",
        "go ahead into the cave", "holiday mode", "new modes & maps", "sign posting", "parkour challenge",
        "your arcade games profile", "featured", "limited time", "stay safe", "recorded on level gain",
        "recorded on achivement completion", "happy easter", "happy halloween", "happy holidays", "easter event",
        "holiday event", "halloween event", "summer event", "goal", "defend", "jump in to score", "bow aiming/pearl clutching"
    )

    override fun load() {
        val response: JsonElement? = JsonUtils.parseFromUrl("$apiBase/armorstands.json")
        response?.asJsonArray?.map { it.asString }?.toSet()?.let { this.armorStandNames = it }
    }
}
