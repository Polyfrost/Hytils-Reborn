/**
 * The code in this package and in [org.polyfrost.hytils.mixin.client.chat]
 * was heavily adapted from Better Hypixel Chat under the GPLv3 license.
 *
 * https://github.com/viciscat/BetterHypixelChat
 * https://modrinth.com/project/3IwykNr3
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.polyfrost.hytils.client.chat

//? if >=1.21.11 {
import net.minecraft.client.gui.ActiveTextCollector
import net.minecraft.client.gui.navigation.ScreenRectangle
import org.polyfrost.hytils.ducks.GuiGraphicsDuck
//?}

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.ChatComponent
import net.minecraft.network.chat.Component
import org.polyfrost.hytils.client.chat.core.ChatGraphics
import org.polyfrost.hytils.client.chat.core.ChatLineParser
import org.polyfrost.hytils.client.chat.core.ParsedChatSequence
import org.polyfrost.hytils.client.chat.lines.CenteredLine
import org.polyfrost.hytils.client.chat.lines.LabeledSeparatorLine
import org.polyfrost.hytils.client.chat.lines.SeparatorLine
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object ChatEnhancements {
    const val DEFAULT_CHAT_WIDTH = 320

    private val parsers = listOf(CenteredLine, SeparatorLine, LabeledSeparatorLine)

    @JvmStatic
    fun parseLine(component: Component, raw: String, trimmed: String, chatWidth: Int, font: Font): List<ChatLineParser.ParsedLine>? {
        return parsers
            .filter { it.isEnabled }
            .firstNotNullOfOrNull { it.parse(component, raw, trimmed, chatWidth, font) }
    }

    @JvmStatic
    fun renderSequence(
        parsed: ParsedChatSequence,
        //~ if <1.21.11 'ChatComponent.ChatGraphicsAccess' -> 'net.minecraft.client.gui.GuiGraphics'
        graphics: ChatComponent.ChatGraphicsAccess,
        lineBottom: Int,
        lineTop: Int,
        textTop: Int,
        textAlpha: Float
    ): Boolean {
        val chatGraphics = ChatGraphics(graphics)
        val chatWidth = ChatComponent.getWidth(mc.options.chatWidth().get())

        //? if >=1.21.11 {
        val parameterModifier = graphics as GuiGraphicsDuck
        var previousScissor: ScreenRectangle? = null

        parameterModifier.`hytils$applyParameters` { parameter ->
            previousScissor = parameter.scissor
            parameter.withScissor(0, chatWidth, lineTop, lineBottom)
        }
        //?} else
        //graphics.enableScissor(0, lineTop, chatWidth, lineBottom)

        parsed.renderer.render(
            chatGraphics,
            parsed.sequence,
            0,
            chatWidth,
            lineBottom - lineTop,
            textTop,
            textAlpha
        )

        //? if >=1.21.11 {
        parameterModifier.`hytils$applyParameters` { parameter ->
            ActiveTextCollector.Parameters(parameter.pose, parameter.opacity, previousScissor)
        }
        //?} else
        //graphics.disableScissor()

        //~ if <1.21.11 'chatGraphics.hovered' -> 'true'
        return chatGraphics.hovered
    }

    //? if <1.21.11 {
    /*@JvmStatic
    fun getStyleAt(parsed: ParsedChatSequence, mouseX: Int, font: Font): net.minecraft.network.chat.Style? {
        val chatWidth = ChatComponent.getWidth(mc.options.chatWidth().get())
        return parsed.renderer.getStyleAt(parsed.sequence, mouseX, chatWidth, font)
    }
    *///?}
}
