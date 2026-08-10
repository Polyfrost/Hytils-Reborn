package org.polyfrost.hytils.test

import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.minecraft.SharedConstants
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.server.Bootstrap
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.modules.modifiers.ShortPMChannelNames

class ShortPMChannelNamesTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun setupEnvironment() {
            SharedConstants.tryDetectVersion()
            Bootstrap.bootStrap()
        }

        private fun message(type: String) = """
            {"text":"","extra":[{"text":"$type ","color":"light_purple","strikethrough":false},{"text":"[MVP","color":"aqua"},{"text":"++","color":"blue"},{"text":"] qtLuna","color":"aqua"},{"text":": ","color":"gray","strikethrough":false},{"text":"this is a test","color":"gray"}]}
        """.trimIndent()
    }

    private fun shorten(json: String): Component {
        val parsed = ComponentSerialization.CODEC
            .parse(JsonOps.INSTANCE, JsonParser.parseString(json))
            .getOrThrow { AssertionError("Failed to parse test component: $it") }

        val event = ChatReceiveEvent(parsed, false)
        ShortPMChannelNames.onChatReceived(event)
        return event.message
    }

    @Test
    fun `outgoing private messages keep their contents`() {
        Assertions.assertEquals(
            "PM > [MVP++] qtLuna: this is a test",
            shorten(message("To")).string
        )
    }

    @Test
    fun `incoming private messages keep their contents`() {
        Assertions.assertEquals(
            "PM < [MVP++] qtLuna: this is a test",
            shorten(message("From")).string
        )
    }

    @Test
    fun `non private messages are left alone`() {
        val json = """{"text":"","extra":[{"text":"Friend > ","color":"gray"},{"text":"qtLuna joined.","color":"gray"}]}"""
        Assertions.assertEquals("Friend > qtLuna joined.", shorten(json).string)
    }
}
