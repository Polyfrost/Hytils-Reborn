/**
 * This code was heavily adapted from Better Hypixel Chat under the GPLv3 license.
 *
 * https://github.com/viciscat/BetterHypixelChat
 * https://modrinth.com/project/3IwykNr3
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.polyfrost.hytils.client.utils

object StringUtils {
    fun String.findFirstDifferentChar(startIndex: Int, vararg chars: Char): Int {
        for (i in startIndex until this.length) {
            if (this[i] !in chars) return i
        }
        return -1
    }

    fun String.findLastDifferentChar(startIndex: Int, vararg chars: Char): Int {
        for (i in startIndex downTo 0) {
            if (this[i] !in chars) return i
        }
        return -1
    }
}
