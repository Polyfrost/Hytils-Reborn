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

package org.polyfrost.hytils.handlers.general;

import dev.deftu.omnicore.api.OmniIdentifier;
import dev.deftu.omnicore.api.client.OmniClient;
import dev.deftu.omnicore.api.client.sound.OmniClientSound;
import dev.deftu.omnicore.api.items.OmniItemStacks;
import dev.deftu.omnicore.api.sound.OmniSound;
import dev.deftu.omnicore.api.sound.OmniSounds;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import org.polyfrost.oneconfig.api.event.v1.events.TickEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

//#if MC >= 1.12.2
//$$ import net.minecraft.sound.Sounds;
//#endif

public class SoundHandler {
    private OmniSound IRON_GOLEM_HIT;
    private OmniSound BLAZE_HIT;
    private OmniSound HORSE_DEATH;
    private OmniSound GHAST_SCREAM;
    private OmniSound GUARDIAN_HIT;
    private OmniSound CAT_MEOW;

    private int ticks = -1;

    @Subscribe
    public void onTick(TickEvent.Start e) {
        WorldClient world = OmniClient.getWorld();
        EntityPlayerSP player = OmniClient.getPlayer();
        if (
            !HypixelUtils.isHypixel() ||
            player == null ||
            world == null
        ) {
            return;
        }

        ItemStack equippedStack = player.getCurrentEquippedItem();
        if (equippedStack == null) {
            return;
        }

        int equippedStackSize = OmniItemStacks.count(equippedStack);
        if (equippedStackSize > HytilsConfig.blockNumber || equippedStackSize <= 4) {
            if (ticks != -1) {
                ticks = -1;
            }

            return;
        }

        Item equippedItem = equippedStack.getItem();
        if (
            !(equippedItem instanceof ItemBlock) ||
            (((ItemBlock) equippedItem).getBlock() instanceof BlockTNT)
        ) {
            if (ticks != -1) {
                ticks = -1;
            }

            return;
        }

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (HytilsConfig.blockNotify && location.inGame() && location.getGameType().isPresent()) {
            switch (location.getGameType().get()) {
                case BUILD_BATTLE: case HOUSING: case SKYBLOCK:
                    return;
            }

            ticks++;
            if (ticks == 0) {
                playSound();
                return;
            } else if (ticks == 20) {
                playSound();
                return;
            }

            if (ticks > 40) {
                ticks = -1;
            }
        }
    }

    public void playSound() {
        if (!Minecraft.getMinecraft().playerController.gameIsSurvivalOrAdventure()) {
            return;
        }

        OmniSound sound = null;
        switch (HytilsConfig.blockNotifySound) {
            case 0:
                sound = OmniSounds.ENTITY.getExperienceOrb();
                break;
            case 1:
                sound = getIronGolemHit();
                break;
            case 2:
                sound = getBlazeHit();
                break;
            case 3:
                sound = OmniSounds.BLOCK.getAnvilLand();
                break;
            case 4:
                sound = getHorseDeath();
                break;
            case 5:
                sound = getGhastScream();
                break;
            case 6:
                sound = getGuardianHit();
                break;
            case 7:
                sound = getCatMeow();
                break;
            case 8:
                sound = OmniSounds.WOLF.getBark();
                break;
        }

        if (sound != null) {
            OmniClientSound.play(sound, 1f, 1f);
        }
    }

    private OmniSound getIronGolemHit() {
        if (IRON_GOLEM_HIT == null) {
            IRON_GOLEM_HIT = OmniSound.of(
                //#if MC >= 1.12.2
                //$$ Sounds.ENTITY_IRONGOLEM_HURT
                //#else
                OmniIdentifier.createOrThrow("mob.irongolem.hit")
                //#endif
            );
        }

        return IRON_GOLEM_HIT;
    }

    private OmniSound getBlazeHit() {
        if (BLAZE_HIT == null) {
            BLAZE_HIT = OmniSound.of(
                //#if MC >= 1.12.2
                //$$ Sounds.ENTITY_BLAZE_HURT
                //#else
                OmniIdentifier.createOrThrow("mob.blaze.hit")
                //#endif
            );
        }

        return BLAZE_HIT;
    }

    private OmniSound getHorseDeath() {
        if (HORSE_DEATH == null) {
            HORSE_DEATH = OmniSound.of(
                //#if MC >= 1.12.2
                //$$ Sounds.ENTITY_HORSE_DEATH
                //#else
                OmniIdentifier.createOrThrow("mob.horse.death")
                //#endif
            );
        }

        return HORSE_DEATH;
    }

    private OmniSound getGhastScream() {
        if (GHAST_SCREAM == null) {
            GHAST_SCREAM = OmniSound.of(
                //#if MC >= 1.12.2
                //$$ Sounds.ENTITY_GHAST_SCREAM
                //#else
                OmniIdentifier.createOrThrow("mob.ghast.scream")
                //#endif
            );
        }

        return GHAST_SCREAM;
    }

    private OmniSound getGuardianHit() {
        if (GUARDIAN_HIT == null) {
            GUARDIAN_HIT = OmniSound.of(
                //#if MC >= 1.12.2
                //$$ Sounds.ENTITY_GUARDIAN_HURT_LAND
                //#else
                OmniIdentifier.createOrThrow("mob.guardian.land.hit")
                //#endif
            );
        }

        return GUARDIAN_HIT;
    }

    private OmniSound getCatMeow() {
        if (CAT_MEOW == null) {
            CAT_MEOW = OmniSound.of(
                //#if MC >= 1.12.2
                //$$ Sounds.ENTITY_CAT_AMBIENT
                //#else
                OmniIdentifier.createOrThrow("mob.cat.meow")
                //#endif
            );
        }

        return CAT_MEOW;
    }
}
