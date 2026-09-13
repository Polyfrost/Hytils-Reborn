package org.polyfrost.hytils.test

//? if >=1.21.11 {
import net.minecraft.client.gui.ActiveTextCollector
import net.minecraft.client.gui.navigation.ScreenRectangle
import org.joml.Matrix3x2f
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.polyfrost.hytils.client.features.chat.enhancements.ChatEnhancements

class ChatEnhancementsScissorTest {
    private val screen = ScreenRectangle(0, 0, 640, 330)

    private fun lineAt(top: Int) = ActiveTextCollector.Parameters(Matrix3x2f()).withScissor(0, 320, top, top + 9)

    @Test
    fun `line inside the screen is unchanged`() {
        Assertions.assertEquals(ScreenRectangle(0, 100, 320, 9), ChatEnhancements.clipToScreen(lineAt(100), screen)?.scissor)
    }

    @Test
    fun `line crossing the top edge is clipped`() {
        Assertions.assertEquals(ScreenRectangle(0, 0, 320, 4), ChatEnhancements.clipToScreen(lineAt(-5), screen)?.scissor)
    }

    @Test
    fun `line above the screen is dropped`() {
        Assertions.assertNull(ChatEnhancements.clipToScreen(lineAt(-48), screen))
    }

    @Test
    fun `parameters without a scissor pass through`() {
        val parameters = ActiveTextCollector.Parameters(Matrix3x2f())
        Assertions.assertSame(parameters, ChatEnhancements.clipToScreen(parameters, screen))
    }
}
//?}
