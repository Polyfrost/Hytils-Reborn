/**
 * The code in this package and in [org.polyfrost.hytils.mixin.client.chat]
 * was heavily adapted from Better Hypixel Chat under the GPLv3 license.
 *
 * https://github.com/viciscat/BetterHypixelChat
 * https://modrinth.com/project/3IwykNr3
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.polyfrost.hytils.client.features.chat.enhancements

//? if >=1.21.11 {
import net.minecraft.client.gui.ActiveTextCollector
import net.minecraft.client.gui.navigation.ScreenRectangle
import org.polyfrost.hytils.ducks.ChatGraphicsAccessDuck
//?}

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.ChatComponent
import net.minecraft.client.gui.components.ComponentRenderUtils
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence
import org.polyfrost.hytils.client.features.chat.enhancements.core.ChatGraphics
import org.polyfrost.hytils.client.features.chat.enhancements.core.ChatTextBuilder
import org.polyfrost.hytils.client.features.chat.enhancements.core.CustomChatLine
import org.polyfrost.hytils.client.features.chat.enhancements.lines.LabeledSeparatorLine
import org.polyfrost.hytils.client.features.chat.enhancements.lines.SeparatorLine
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object ChatEnhancements {
    const val DEFAULT_CHAT_WIDTH = 320

    private val parsers = listOf(SeparatorLine, LabeledSeparatorLine)

    @JvmStatic
    fun parseChatLines(component: Component, chatWidth: Int, font: Font): List<FormattedCharSequence> = buildList {
        val builder = ChatTextBuilder()
        component.visualOrderText.accept(builder)

        for (text in builder.getTexts()) {
            val rawString = text.string
            val trimmedString = rawString.trim()

            val customLines = parsers
                .asSequence()
                .filter { it.isEnabled }
                .firstNotNullOfOrNull { it.parse(text, rawString, trimmedString, chatWidth, font) }
            addAll(customLines ?: ComponentRenderUtils.wrapComponents(text, chatWidth, font))
        }
    }

    @JvmStatic
    fun renderCustomLine(
        customLine: CustomChatLine,
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
        val parameterModifier = graphics as ChatGraphicsAccessDuck
        val screen = ScreenRectangle(0, 0, mc.window.guiScaledWidth, mc.window.guiScaledHeight)
        var previousScissor: ScreenRectangle? = null
        var visible = true

        parameterModifier.`hytils$applyParameters` { parameter ->
            previousScissor = parameter.scissor
            val clipped = clipToScreen(parameter.withScissor(0, chatWidth, lineTop, lineBottom), screen)
            visible = clipped != null
            clipped ?: parameter
        }

        if (visible) {
            customLine.render(chatGraphics, 0, chatWidth, lineBottom - lineTop, textTop, textAlpha)
        }

        parameterModifier.`hytils$applyParameters` { parameter ->
            ActiveTextCollector.Parameters(parameter.pose, parameter.opacity, previousScissor)
        }

        return chatGraphics.hovered
        //?} else {
        /*graphics.enableScissor(0, lineTop, chatWidth, lineBottom)
        customLine.render(chatGraphics, 0, chatWidth, lineBottom - lineTop, textTop, textAlpha)
        graphics.disableScissor()
        return true
        *///?}
    }

    //? if >=1.21.11 {
    /**
     * Clips the text scissor to the screen (returns null if it lies entirely outside it).
     * Vanilla only does this for the bottom or right edges, so a scissor past the top or
     * left edge would cause a crash (e.g. a chat line scrolled above the window).
     */
    @JvmStatic
    fun clipToScreen(parameters: ActiveTextCollector.Parameters, screen: ScreenRectangle): ActiveTextCollector.Parameters? {
        val scissor = parameters.scissor ?: return parameters
        val clipped = screen.intersection(scissor) ?: return null
        return parameters.withScissor(clipped)
    }
    //?}

    //? if <1.21.11 {
    /*@JvmStatic
    fun getStyleAt(customLine: CustomChatLine, mouseX: Int, font: Font): net.minecraft.network.chat.Style? {
        val chatWidth = ChatComponent.getWidth(mc.options.chatWidth().get())
        return customLine.getStyleAt(mouseX, chatWidth, font)
    }
    *///?}
}
