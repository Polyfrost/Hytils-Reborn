/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.hytils.util.notification;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.RenderEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UMouse;
import cc.polyfrost.oneconfig.libs.universal.UResolution;
import cc.polyfrost.oneconfig.utils.MathUtils;
import cc.polyfrost.oneconfig.utils.color.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Taken from XanderLib under GPL-3.0
 * https://github.com/isXander/XanderLib/blob/main/LICENSE
 */
public final class NotificationManager {

    private static final int TOAST_WIDTH = 200;
    private static final int TOAST_PADDING_WIDTH = 5;
    private static final int TOAST_PADDING_HEIGHT = 3;
    private static final int TOAST_TEXT_DISTANCE = 2;
    public static final NotificationManager INSTANCE = new NotificationManager();

    private final ConcurrentLinkedDeque<Notification> currentNotifications = new ConcurrentLinkedDeque<>();

    public NotificationManager() {
        EventManager.INSTANCE.register(this);
    }

    /**
     * Linearly interpolates between a and b by t.
     *
     * @param start         Start value
     * @param end           End value
     * @param interpolation Interpolation between two floats
     * @return interpolated value between a - b
     * @author isXander
     */
    private static float lerp(float start, float end, float interpolation) {
        return start + (end - start) * MathUtils.clamp(interpolation);
    }

    private static void drawRect(float left, float top, float right, float bottom, int color) {
        float i;
        if (left < right) {
            i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            i = top;
            top = bottom;
            bottom = i;
        }

        float f = (float) (color >> 24 & 255) / 255.0F;
        float g = (float) (color >> 16 & 255) / 255.0F;
        float h = (float) (color >> 8 & 255) / 255.0F;
        float j = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(g, h, j, f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, top, 0.0D).endVertex();
        worldRenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public void push(String title, String description, Runnable runnable) {
        if (title == null || description == null) throw new NullPointerException("Title or Description is null.");
        currentNotifications.add(new Notification(title, description, runnable));
    }

    public void push(String title, String description) {
        push(title, description, null);
    }

    @Subscribe
    private void onRender(RenderEvent event) {
        if (event.stage != Stage.END) return;

        if (currentNotifications.size() == 0) return;
        Notification notification = currentNotifications.peekFirst();

        float time = notification.time;
        float opacity = 200;

        if (time <= 1 || time >= 10) {
            float easeTime = Math.min(time, 1);
            opacity = easeTime * 200;
        }
        String[] wrappedTitle = wrapTextLines(EnumChatFormatting.BOLD + notification.title, Minecraft.getMinecraft().fontRendererObj, TOAST_WIDTH, " ");
        String[] wrappedText = wrapTextLines(notification.description, Minecraft.getMinecraft().fontRendererObj, TOAST_WIDTH, " ");
        int textLines = wrappedText.length + wrappedTitle.length;
        float rectWidth = notification.width = lerp(notification.width, TOAST_WIDTH + (TOAST_PADDING_WIDTH * 2), event.deltaTicks / 4f);
        float rectHeight = (TOAST_PADDING_HEIGHT * 2) + (textLines * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) + ((textLines - 1) * TOAST_TEXT_DISTANCE);
        float rectX = UResolution.getScaledWidth() / 2f - (rectWidth / 2f);
        float rectY = 5;

        double mouseX = UMouse.Scaled.getX();
        double mouseY = UMouse.Scaled.getY();
        boolean mouseOver = mouseX >= rectX && mouseX <= rectX + rectWidth && mouseY >= rectY && mouseY <= rectY + rectHeight;

        opacity += notification.mouseOverAdd = lerp(notification.mouseOverAdd, (mouseOver ? 40 : 0), event.deltaTicks / 4f);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        drawRect(rectX, rectY, rectWidth + rectX, rectHeight + rectY, ColorUtils.getColor(0, 0, 0, (int) MathUtils.clamp(opacity, 5, 255)));
        if (notification.time > 0.1f) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor((int) ((rectX * Minecraft.getMinecraft().displayWidth) / UResolution.getScaledWidth()), (int) (((UResolution.getScaledHeight() - (rectY + rectHeight)) * Minecraft.getMinecraft().displayHeight) / UResolution.getScaledHeight()), (int) (rectWidth * Minecraft.getMinecraft().displayWidth / UResolution.getScaledWidth()), (int) (rectHeight * Minecraft.getMinecraft().displayHeight / UResolution.getScaledHeight()));
            int color = new Color(255, 255, 255, (int) MathUtils.clamp(opacity, 2, 255)).getRGB();
            int i = 0;
            for (String line : wrappedTitle) {
                Minecraft.getMinecraft().fontRendererObj.drawString(EnumChatFormatting.BOLD + line, UResolution.getScaledWidth() / 2f - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(line) / 2f), rectY + TOAST_PADDING_HEIGHT + (TOAST_TEXT_DISTANCE * i) + (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * i), color, true);
                i++;
            }
            for (String line : wrappedText) {
                Minecraft.getMinecraft().fontRendererObj.drawString(line, UResolution.getScaledWidth() / 2f - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(line) / 2f), rectY + TOAST_PADDING_HEIGHT + (TOAST_TEXT_DISTANCE * i) + (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * i), color, false);
                i++;
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        GlStateManager.popMatrix();
        if (notification.time >= 3f) {
            notification.closing = true;
        }
        if (!notification.clicked && mouseOver && Mouse.getEventButtonState()) {
            notification.clicked = true;
            if (notification.runnable != null) notification.runnable.run();
            notification.closing = true;
            if (notification.time > 1f) notification.time = 1f;
        }
        if (!((mouseOver && notification.clicked) && notification.time > 1f)) {
            notification.time += (notification.closing ? -0.02f : 0.02f) * (event.deltaTicks * 3f);
        }
        if (notification.closing && notification.time <= 0) {
            currentNotifications.remove(notification);
        }
    }

    private static String wrapText(String text, FontRenderer fontRenderer, int lineWidth, String split) {
        // split with line ending too
        String[] words = text.split("(" + split + "|\n)");
        // current line width
        int lineLength = 0;
        // string concatenation in loop is bad
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            // add a the word splitter after the word every time except the last word
            if (i != words.length - 1) {
                word += split;
            }

            // length of next word
            int wordLength = fontRenderer.getStringWidth(word);

            if (lineLength + wordLength <= lineWidth) { // if the current line length plus this next word is less than the maximum line width
                // if the condition is met, we can just append the word to the current line as it is small enough
                output.append(word);
                lineLength += wordLength;
            } else if (wordLength <= lineWidth) { // the word is not big enough to be larger than the whole line max width
                // make a new line before adding the word
                output.append("\n").append(word);
                // the next line has just been made and has been populated with only one word. reset line length and add the word we just added
                lineLength = wordLength;
            } else {
                // the single word will not fit so run the function again with just this word
                // and tell it that every character is it's own word
                output.append(wrapText(word, fontRenderer, lineWidth, "")).append(split);
            }
        }

        return output.toString();
    }

    private static String[] wrapTextLines(String text, FontRenderer fontRenderer, int lineWidth, String split) {
        String wrapped = wrapText(text, fontRenderer, lineWidth, split);
        if (wrapped.equals("")) {
            return new String[]{};
        }

        return wrapped.split("\n");
    }

}
