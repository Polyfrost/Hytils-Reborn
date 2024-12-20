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

package org.polyfrost.hytils.handlers.lobby.tab;

import net.hypixel.data.type.GameType;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.language.LanguageData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.polyfrost.hytils.forge.HytilsMixinPlugin;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Used in {@link HytilsMixinPlugin}
 */
@SuppressWarnings("unused")
public class TabChanger {
    private static final LanguageData language = HytilsReborn.INSTANCE.getLanguageHandler().getCurrent();
    private static final Pattern validMinecraftUsername = Pattern.compile("\\[\\d+] \\w{1,16}(?: .{1,3}|$)");
    private static final Pattern skyblockTabInformationEntryGameProfileNameRegex = Pattern.compile("![A-D]-[a-v]");
    private static final Pattern trimChatComponentTextRegex = Pattern.compile("^(?:\\s|§r|§s)*|(?:\\s|§r|§s)*$");

    /**
     * Adds a star to the display name of a player in Tab.
     * If the star is added before or after the name is determined by the config value of highlightFriendsInTab
     * For example, the input "§b[MVP§c+§b] Steve §6[GUILD]" will return "§9✯ §r§b[MVP§c+§b] Steve §6[GUILD]" if
     * highlightFriendsInTab is set to "Left of Name"
     *
     * @param displayName The name of the player as appears in tab menu
     * @return The displayName that was given as input but with a star added
     */
    private static String addStarToNameFriend(String displayName) {
        switch (HytilsConfig.highlightFriendsInTab) {
            case 1:
                return "§9✯ §r" + displayName;
            case 2:
                return displayName + "§r §9✯";
            default:
                HytilsReborn.INSTANCE.getLogger()
                    .warn("Method TabChanger#addStarToName called when highlightFriendsInTab was not enabled");
                return "§9✯ §r" + displayName;
        }
    }

    /**
     * Adds a star to the display name of the user in Tab.
     * If the star is added before or after the name is determined by the config value of highlightSelfInTab
     * For example, the input "§b[MVP§c+§b] Steve §6[GUILD]" will return "§9✯ §r§b[MVP§c+§b] Steve §6[GUILD]" if
     * highlightSelfInTab is set to "Left of Name"
     *
     * @param displayName The name of the player as appears in tab menu
     * @return The displayName that was given as input but with a star added
     */
    private static String addStarToNameSelf(String displayName) {
        switch (HytilsConfig.highlightSelfInTab) {
            case 1:
                return "§5✯ §r" + displayName;
            case 2:
                return displayName + "§r §5✯";
            default:
                HytilsReborn.INSTANCE.getLogger()
                    .warn("Method TabChanger#addStarToName called when highlightSelfInTab was not enabled");
                return "§5✯ §r" + displayName;
        }
    }

    public static String modifyName(String name, NetworkPlayerInfo networkPlayerInfo) {
        if (HypixelUtils.isHypixel()) {
            final UUID uuid = networkPlayerInfo.getGameProfile().getId();

            if (HytilsConfig.hidePlayerRanksInTab && name.startsWith("[", 2) && !HypixelUtils.getLocation().inGame()) {
                // keep the name color if player rank is removed
                // §b[MVP§c+§b] Steve
                final String color = "\u00a7" + name.charAt(1);

                // add the rank color, and trim off the player rank
                name = color + name.substring(name.indexOf("]") + 2);
            }

            HypixelUtils.Location location = HypixelUtils.getLocation();
            if (HytilsConfig.hideGuildTagsInTab && name.endsWith("]") && (location.getGameType().orElse(null) != GameType.HOUSING && !location.inGame())) {
                // trim off the guild tag
                // e.g. Steve §6[GUILD]
                name = name.substring(0, name.lastIndexOf("[") - 3);
            }

            if (HytilsConfig.highlightFriendsInTab != 0) {
                Set<UUID> friendList = HytilsReborn.INSTANCE.getFriendCache().getFriendUUIDs();
                // friendList will be null if the friend list has not been cached
                if (friendList != null && friendList.contains(uuid)) {
                    name = addStarToNameFriend(name);
                }
            }

            if (HytilsConfig.highlightSelfInTab != 0) {
                if (uuid.equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
                    name = addStarToNameSelf(name);
                }
            }
        }

        return name;
    }

    public static List<String> modifyFooter(FontRenderer instance, String formattedFooter, int wrapWidth) {
        if (HytilsConfig.hideAdsInTab && HypixelUtils.isHypixel() && formattedFooter.contains(language.tabFooterAdvertisement)) {
            String trimmedFooter = formattedFooter.replaceAll((trimChatComponentTextRegex.pattern()), "");
            if (trimmedFooter.matches(language.tabFooterAdvertisement)) {
                return new ArrayList<>();
            } else {
                for (String line : new ArrayList<>(Arrays.asList(trimmedFooter.split("\n")))) {
                    if (line.contains(language.tabFooterAdvertisement)) {
                        formattedFooter = formattedFooter.replace(line, "").replaceAll(("^|(?:\\s|§r|§s)*$"), "");
                    }
                }
            }
        }
        return instance.listFormattedStringToWidth(formattedFooter, wrapWidth - 50);
    }

    public static List<String> modifyHeader(FontRenderer instance, String formattedHeader, int wrapWidth) {
        if (HytilsConfig.hideAdsInTab && HypixelUtils.isHypixel() && formattedHeader.contains(language.tabHeaderAdvertisement)) {
            String trimmedHeader = formattedHeader.replaceAll((trimChatComponentTextRegex.pattern()), "");
            if (trimmedHeader.matches(language.tabHeaderAdvertisement)) {
                return new ArrayList<>();
            } else {
                for (String line : new ArrayList<>(Arrays.asList(trimmedHeader.split("\n")))) {
                    if (line.contains(language.tabHeaderAdvertisement)) {
                        formattedHeader = formattedHeader.replace(line, "").replaceAll(("^(?:\\s|§r|§s)*|$"), "");
                    }
                }
            }
        }
        return instance.listFormattedStringToWidth(formattedHeader, wrapWidth - 50);
    }

    public static boolean shouldRenderPlayerHead(NetworkPlayerInfo networkPlayerInfo) {
        return !HypixelUtils.isHypixel() || !isSkyblockTabInformationEntry(networkPlayerInfo);
    }

    public static boolean hidePing(NetworkPlayerInfo networkPlayerInfo) {

        return HypixelUtils.isHypixel() && ((HytilsConfig.hidePingInTab && HypixelUtils.getLocation().inGame()) || isSkyblockTabInformationEntry(networkPlayerInfo));
    }

    private static boolean isSkyblockTabInformationEntry(NetworkPlayerInfo networkPlayerInfo) {
        if (!HytilsConfig.cleanerSkyblockTabInfo) return false;
        return
            HypixelUtils.getLocation().getGameType().orElse(null) == GameType.SKYBLOCK &&
                skyblockTabInformationEntryGameProfileNameRegex.matcher(networkPlayerInfo.getGameProfile().getName()).matches() &&
                !validMinecraftUsername.matcher(networkPlayerInfo.getDisplayName().getUnformattedText()).matches();
    }
}
