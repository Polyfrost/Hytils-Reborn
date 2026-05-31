/**
 * This code was heavily adapted from Better Hypixel Chat under the GPLv3 license.
 *
 * https://github.com/viciscat/BetterHypixelChat
 * https://modrinth.com/project/3IwykNr3
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.polyfrost.hytils.client.utils

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.util.FormattedCharSequence
import net.minecraft.util.Unit
import java.util.*

fun FormattedCharSequence.asString(): String {
    val builder = java.lang.StringBuilder()
    this.accept { _, _, codePoint ->
        builder.appendCodePoint(codePoint)
        true
    }
    return builder.toString()
}

fun FormattedCharSequence.trim(): FormattedCharSequence {
    val s = this.asString()
    val i = s.findFirstDifferentChar(0, ' ')
    val j = s.findLastDifferentChar(s.length - 1, ' ')
    if (i < 0 || j < 0) return this
    return this.subText(i, j)
}

fun Component.trim(): Component {
    val s = this.string
    val i = s.findFirstDifferentChar(0, ' ')
    val j = s.findLastDifferentChar(s.length - 1, ' ')
    if (i < 0 || j < 0) return this
    return this.subText(i, j)
}

fun FormattedCharSequence.subText(start: Int, end: Int): FormattedCharSequence {
    return FormattedCharSequence { visitor ->
        var i = -1
        this.accept { index, style, codePoint ->
            i++
            if (i < start) return@accept true
            if (i > end) return@accept false
            visitor.accept(index, style, codePoint)
        }
        i > end
    }
}

fun Component.subText(start: Int, end: Int): Component {
    val sub = Component.empty()
    var currentStart = start
    var currentLength = end - start + 1

    this.visit({ style, asString ->
        var str = asString
        if (str.length <= currentStart) {
            currentStart -= str.length
            return@visit Optional.empty<Unit>()
        } else if (currentStart != 0) {
            str = str.substring(currentStart)
            currentStart = 0
        }

        if (str.length <= currentLength) {
            currentLength -= str.length
            sub.append(Component.literal(str).setStyle(style))
        } else {
            str = str.substring(0, currentLength)
            sub.append(Component.literal(str).setStyle(style))
            currentLength = 0
        }
        if (currentLength == 0) Optional.of(Unit.INSTANCE) else Optional.empty()
    }, Style.EMPTY)

    return sub
}

fun FormattedCharSequence.getFirstColor(): TextColor? {
    var color: TextColor? = null
    this.accept { _, style, _ ->
        if (style.color != null) {
            color = style.color
            false
        } else true
    }
    return color
}

fun Component.getFirstColor(): TextColor? {
    var color: TextColor? = null
    this.visit({ style, _ ->
        style.color?.let { color = it }
        Optional.empty<Unit>()
    }, Style.EMPTY)
    return color
}
