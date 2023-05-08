/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022, 2023  Polyfrost, Sk1er LLC and contributors
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

package cc.woverflow.hytils.handlers.general;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SoundHandler {

    private int ticks = -1;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            if (HypixelUtils.INSTANCE.isHypixel() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null) {
                if (HytilsConfig.blockNotify && LocrawUtil.INSTANCE.getLocrawInfo() != null && LocrawUtil.INSTANCE.isInGame()) {
                    switch (LocrawUtil.INSTANCE.getLocrawInfo().getGameType()) {
                        case BUILD_BATTLE:
                        case HOUSING:
                        case SKYBLOCK:
                            return;
                    }
                    if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock && !(((ItemBlock) Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem()).block instanceof BlockTNT) && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().stackSize <= HytilsConfig.blockNumber && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().stackSize > 4) {
                        ticks++;
                        if (ticks == 0) {
                            playSound();
                            return;
                        } else if (ticks == 20) {
                            playSound();
                            return;
                        }
                        if (ticks > 40) ticks = -1;
                    } else if (ticks != -1) {
                        ticks = -1;
                    }
                }
            }
        }
    }

    public void playSound() {
        if (!Minecraft.getMinecraft().playerController.gameIsSurvivalOrAdventure()) return;
        switch (HytilsConfig.blockNotifySound) {
            case 0:
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1f, 1f);
                break;
            case 1:
                Minecraft.getMinecraft().thePlayer.playSound("mob.irongolem.hit", 1f, 1f);
                break;
            case 2:
                Minecraft.getMinecraft().thePlayer.playSound("mob.blaze.hit", 1f, 1f);
                break;
            case 3:
                Minecraft.getMinecraft().thePlayer.playSound("random.anvil_land", 1f, 1f);
                break;
            case 4:
                Minecraft.getMinecraft().thePlayer.playSound("mob.horse.death", 1f, 1f);
                break;
            case 5:
                Minecraft.getMinecraft().thePlayer.playSound("mob.ghast.scream", 1f, 1f);
                break;
            case 6:
                Minecraft.getMinecraft().thePlayer.playSound("mob.guardian.land.hit", 1f, 1f);
                break;
            case 7:
                Minecraft.getMinecraft().thePlayer.playSound("mob.cat.meow", 1f, 1f);
                break;
            case 8:
                Minecraft.getMinecraft().thePlayer.playSound("mob.wolf.bark", 1f, 1f);
        }
    }
}
