package org.polyfrost.hytils.client.data

interface DataProvider {
    val apiBase
        get() = "https://data.polyfrost.org/hytils"

    fun load()
}
