package org.polyfrost.hytils.client.data

interface DataProvider {
    val apiBase
        get() = "https://data-v2.polyfrost.org/hytils"

    fun load()
}
