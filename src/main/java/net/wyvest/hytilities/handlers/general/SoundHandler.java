/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
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

package net.wyvest.hytilities.handlers.general;

import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.config.HytilitiesConfig;
import gg.essential.api.EssentialAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SoundHandler {
    private boolean shouldPlay = true;


    @SubscribeEvent
    public void onWorldLeave(WorldEvent.Unload e) {
        shouldPlay = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            if (EssentialAPI.getMinecraftUtil().isHypixel() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null) {
                if (HytilitiesConfig.blockNotify && !Hytilities.INSTANCE.getLobbyChecker().playerIsInLobby()) {
                    if (Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
                        if (Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().stackSize <= HytilitiesConfig.blockNumber && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().stackSize > 0) {
                            if (HytilitiesConfig.spamBlockNotify) {
                                playSound();
                            } else {
                                if (shouldPlay) {
                                    playSound();
                                    shouldPlay = false;
                                }
                            }
                        } else if (!shouldPlay) {
                            shouldPlay = true;
                        }
                    }
                }
            }
        }
    }

    public void playSound() {
        if (!Minecraft.getMinecraft().playerController.gameIsSurvivalOrAdventure()) return;
        switch (HytilitiesConfig.blockNotifySound) {
            case 0: {
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1f, 1f);
                return;
            }
            case 1: {
                Minecraft.getMinecraft().thePlayer.playSound("mob.irongolem.hit", 1f, 1f);
                return;
            }
            case 2: {
                Minecraft.getMinecraft().thePlayer.playSound("mob.blaze.hit", 1f, 1f);
                return;
            }
            case 3: {
                Minecraft.getMinecraft().thePlayer.playSound("random.anvil_land", 1f, 1f);
                return;
            }
            case 4: {
                Minecraft.getMinecraft().thePlayer.playSound("mob.horse.death", 1f, 1f);
                return;
            }
            case 5: {
                Minecraft.getMinecraft().thePlayer.playSound("mob.ghast.scream", 1f, 1f);
                return;
            }
            case 6: {
                Minecraft.getMinecraft().thePlayer.playSound("mob.guardian.land.hit", 1f, 1f);
                return;
            }
            case 7: {
                Minecraft.getMinecraft().thePlayer.playSound("mob.cat.meow", 1f, 1f);
                return;
            }
            case 8: {
                Minecraft.getMinecraft().thePlayer.playSound("mob.wolf.bark", 1f, 1f);
            }
        }
    }
}
