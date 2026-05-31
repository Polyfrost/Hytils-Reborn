/**
 * This code was heavily adapted from Better Hypixel Chat under the GPLv3 license.
 *
 * https://github.com/viciscat/BetterHypixelChat
 * https://modrinth.com/project/3IwykNr3
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.polyfrost.hytils.client.utils

fun String.findFirstDifferentChar(startIndex: Int, vararg chars: Char): Int =
    (startIndex until length).firstOrNull { this[it] !in chars } ?: -1

fun String.findLastDifferentChar(startIndex: Int, vararg chars: Char): Int =
    (startIndex downTo 0).firstOrNull { this[it] !in chars } ?: -1
