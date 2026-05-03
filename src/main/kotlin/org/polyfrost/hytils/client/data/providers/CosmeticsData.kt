package org.polyfrost.hytils.client.data.providers

import com.google.gson.JsonElement
import org.polyfrost.hytils.client.data.DataProvider
import org.polyfrost.oneconfig.utils.v1.JsonUtils

object CosmeticsData : DataProvider {
    @Volatile var particleCosmetics = setOf(
        "splash", "entity_effect", "witch", "note", "lava", "heart", "dust",
        "item_slime", "large_smoke", "explosion", "flame", "happy_villager"
    )
    @Volatile var itemCosmeticsEquals = setOf(
        "block.minecraft.diamond_block", "block.minecraft.iron_block", "block.minecraft.gold_block",
        "block.minecraft.redstone_block", "block.minecraft.lapis_block", "block.minecraft.coal_block",
        "block.minecraft.carved_pumpkin", "item.minecraft.bone", "item.minecraft.diamond", "item.minecraft.gold_ingot",
        "item.minecraft.gold_nugget", "item.minecraft.slime_ball", "item.minecraft.pumpkin_pie"
    )
    @Volatile var itemCosmeticsStartsWith = listOf("item.minecraft.music_disc_")
    @Volatile var itemCosmeticsEndsWith = listOf("_dye")

    override fun load() {
        val response: JsonElement? = JsonUtils.parseFromUrl("$apiBase/cosmetics.json")
        val obj = response?.asJsonObject ?: return
        val items = obj.getAsJsonObject("items")

        obj.getAsJsonArray("particles")?.map { it.asString }?.toSet()?.let { this.particleCosmetics = it }
        items?.getAsJsonArray("equals")?.map { it.asString }?.toSet()?.let { this.itemCosmeticsEquals = it }
        items?.getAsJsonArray("startsWith")?.map { it.asString }?.toList()?.let { this.itemCosmeticsStartsWith = it }
        items?.getAsJsonArray("endsWith")?.map { it.asString }?.toList()?.let { this.itemCosmeticsEndsWith = it }
    }

    @JvmStatic
    fun isItemCosmetic(item: String): Boolean {
        return itemCosmeticsEquals.contains(item) ||
                itemCosmeticsStartsWith.any { item.startsWith(it) } ||
                itemCosmeticsEndsWith.any { item.endsWith(it) }
    }
}
