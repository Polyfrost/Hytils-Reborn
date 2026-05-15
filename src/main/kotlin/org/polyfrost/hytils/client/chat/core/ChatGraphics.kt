package org.polyfrost.hytils.client.chat.core

import net.minecraft.client.gui.components.ChatComponent
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence
import org.polyfrost.oneconfig.utils.v1.dsl.mc

//~ if <1.21.11 'ChatComponent.ChatGraphicsAccess' -> 'net.minecraft.client.gui.GuiGraphics'
class ChatGraphics(private val graphics: ChatComponent.ChatGraphicsAccess) {
    //? if >=1.21.11
    var hovered = false

    fun drawString(sequence: FormattedCharSequence, x: Int, y: Int, alpha: Float) {
        //? if >=1.21.11 {
        graphics.updatePose { it.translate(x.toFloat(), y.toFloat()) }
        if (graphics.handleMessage(0, alpha, sequence)) {
            hovered = true
        }
        graphics.updatePose { it.translate(-x.toFloat(), -y.toFloat()) }
        //?} else
        //graphics.drawString(mc.font, sequence, x, y, net.minecraft.util.ARGB.white(alpha))
    }

    fun drawString(component: Component, x: Int, y: Int, alpha: Float) {
        drawString(component.visualOrderText, x, y, alpha)
    }

    fun drawCenteredString(component: Component, x: Int, y: Int, alpha: Float) {
        drawString(component, x - width(component) / 2, y, alpha)
    }

    fun drawCenteredString(sequence: FormattedCharSequence, x: Int, y: Int, alpha: Float) {
        drawString(sequence, x - width(sequence) / 2, y, alpha)
    }

    fun fill(x: Int, y: Int, width: Int, height: Int, color: Int) {
        graphics.fill(x, y, width, height, color)
    }
    
    fun width(component: Component): Int = mc.font.width(component)
    
    fun width(sequence: FormattedCharSequence): Int = mc.font.width(sequence)
    
    fun lineHeight(): Int = mc.font.lineHeight
}
