/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.hytils.handlers.chat.modules.triggers;

import cc.woverflow.hytils.config.HytilsConfig;
import gg.essential.api.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;

import static com.sun.xml.internal.bind.WhiteSpaceProcessor.replace;

public class AutoWB {
    @SubscribeEvent
    //The chat receive event
    public void onChat(ClientChatReceivedEvent event){
        String msg = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        //Trimming of the message so its left only with the name
        if (msg.startsWith("Guild > ") && msg.endsWith(" joined.") && (HytilsConfig.AutoWB) && (HytilsConfig.guildAutoWB) || msg.startsWith("G > ") && msg.endsWith(" joined.") && (HytilsConfig.AutoWB) && (HytilsConfig.guildAutoWB)){
            System.out.println("Worked");
            String msgTrimmed = msg.replace("Guild > ","").replace(" joined.","").replace("G > ", "");
            String message = HytilsConfig.AutoWBsendMessage1.replace("%player%", msgTrimmed);
            //What happens when you have random message enabled
            if (HytilsConfig.randomAutoWB){
                while(true) {
                    int r = (int) (Math.random()*10);
                    String sendMessage = new String [] {
                        HytilsConfig.AutoWBsendMessage1,
                        HytilsConfig.AutoWBsendMessage2,
                        HytilsConfig.AutoWBsendMessage3,
                        HytilsConfig.AutoWBsendMessage4,
                        HytilsConfig.AutoWBsendMessage5,
                        HytilsConfig.AutoWBsendMessage6,
                        HytilsConfig.AutoWBsendMessage7,
                        HytilsConfig.AutoWBsendMessage8,
                        HytilsConfig.AutoWBsendMessage9,
                        HytilsConfig.AutoWBsendMessage10
                    }[r].replace("%player%", msgTrimmed);
                    if (!sendMessage.equals("")){
                        Multithreading.schedule(() -> {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage(
                                "/gc " + sendMessage
                            );}, HytilsConfig.AutoWBsendSeconds, TimeUnit.SECONDS);
                        break;
                    }
                }
                //What happens when you have the random message disabled
            }else{ Multithreading.schedule(() -> {
                Minecraft.getMinecraft().thePlayer.sendChatMessage(
                    "/gc " + message
                );
            }, HytilsConfig.AutoWBsendSeconds, TimeUnit.SECONDS);
            }
        }

        if (msg.startsWith("Friend > ") && msg.endsWith(" joined.") && (HytilsConfig.AutoWB) && (HytilsConfig.friendsAutoWB) || msg.startsWith("F > ") && msg.endsWith(" joined.") && (HytilsConfig.AutoWB) && (HytilsConfig.friendsAutoWB)){
            String name = msg.replace("Friend > ","").replace(" joined.","").replace("F > ", "");
            String message = HytilsConfig.AutoWBsendMessage1.replace("%player%", name);
            //What happens when you have random message enabled
            if (HytilsConfig.randomAutoWB){
                while(true) {
                    int r = (int) (Math.random()*10);
                    String sendMessage = new String [] {
                        HytilsConfig.AutoWBsendMessage1,
                        HytilsConfig.AutoWBsendMessage2,
                        HytilsConfig.AutoWBsendMessage3,
                        HytilsConfig.AutoWBsendMessage4,
                        HytilsConfig.AutoWBsendMessage5,
                        HytilsConfig.AutoWBsendMessage6,
                        HytilsConfig.AutoWBsendMessage7,
                        HytilsConfig.AutoWBsendMessage8,
                        HytilsConfig.AutoWBsendMessage9,
                        HytilsConfig.AutoWBsendMessage10
                    }[r].replace("%player%", name);

                    if (!sendMessage.equals("")){
                        Multithreading.schedule(() -> {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage(
                                "/msg " + name + " " + sendMessage
                            );}, HytilsConfig.AutoWBsendSeconds, TimeUnit.SECONDS);
                        break;
                    }
                }
                //What happens when you have the random message disabled
            }else{ Multithreading.schedule(() -> {
                Minecraft.getMinecraft().thePlayer.sendChatMessage(
                    "/msg " + name + message
                );
            }, HytilsConfig.AutoWBsendSeconds, TimeUnit.SECONDS);
            }
        }
    }
}
