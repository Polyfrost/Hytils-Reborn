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

package cc.woverflow.hytils.handlers.lobby.tab;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.language.LanguageData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Used in {@link cc.woverflow.hytils.forge.HytilsMixinPlugin
 */
@SuppressWarnings("unused")
public class TabChanger {
    private static final LanguageData language = HytilsReborn.INSTANCE.getLanguageHandler().getCurrent();
    private static final Pattern validMinecraftUsername = Pattern.compile("(\\[\\d+])? \\w{1,16}(?: .{1,3}|$)");
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
        if (HypixelUtils.INSTANCE.isHypixel()) {
            final UUID uuid = networkPlayerInfo.getGameProfile().getId();

            if (HytilsConfig.hidePlayerRanksInTab && name.startsWith("[", 2) && LocrawUtil.INSTANCE.getLocrawInfo() != null && !LocrawUtil.INSTANCE.isInGame()) {
                // keep the name color if player rank is removed
                // §b[MVP§c+§b] Steve
                final String color = "\u00a7" + name.charAt(1);

                // add the rank color, and trim off the player rank
                name = color + name.substring(name.indexOf("]") + 2);
            }

            LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
            if (HytilsConfig.hideGuildTagsInTab && name.endsWith("]") && locraw != null && locraw.getGameType() != LocrawInfo.GameType.HOUSING) {
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

    public static IChatComponent modifyFooter(IChatComponent footer) {
        if (HytilsConfig.hideAdsInTab && HypixelUtils.INSTANCE.isHypixel() && footer != null && footer.getFormattedText().contains(language.tabFooterAdvertisement)) {
            String trimmedFooter = footer.getFormattedText().replaceAll((trimChatComponentTextRegex.pattern()), "");
            if (trimmedFooter.matches(language.tabFooterAdvertisement)) {
                footer = null;
            } else {
                for (String line : new ArrayList<>(Arrays.asList(trimmedFooter.split("\n")))) {
                    if (line.contains(language.tabFooterAdvertisement)) {
                        footer = new ChatComponentText(footer.getFormattedText().replace(line, "").replaceAll(("^|(?:\\s|§r|§s)*$"), ""));
                    }
                }
            }
        }
        return footer;
    }

    public static IChatComponent modifyHeader(IChatComponent header) {
        if (HytilsConfig.hideAdsInTab && HypixelUtils.INSTANCE.isHypixel() && header != null && header.getFormattedText().contains(language.tabHeaderAdvertisement)) {
            String trimmedHeader = header.getFormattedText().replaceAll((trimChatComponentTextRegex.pattern()), "");
            if (trimmedHeader.matches(language.tabHeaderAdvertisement)) {
                header = null;
            } else {
                for (String line : new ArrayList<>(Arrays.asList(trimmedHeader.split("\n")))) {
                    if (line.contains(language.tabHeaderAdvertisement)) {
                        header = new ChatComponentText(header.getFormattedText().replace(line, "").replaceAll(("^(?:\\s|§r|§s)*|$"), ""));
                    }
                }
            }
        }
        return header;
    }

    public static boolean shouldRenderPlayerHead(NetworkPlayerInfo networkPlayerInfo) {
        return !HypixelUtils.INSTANCE.isHypixel() || !isSkyblockTabInformationEntry(networkPlayerInfo);
    }

    public static boolean hidePing(NetworkPlayerInfo networkPlayerInfo) {
        return HypixelUtils.INSTANCE.isHypixel() && ((HytilsConfig.hidePingInTab && LocrawUtil.INSTANCE.getLocrawInfo() != null && LocrawUtil.INSTANCE.isInGame()) || isSkyblockTabInformationEntry(networkPlayerInfo));
    }

    private static boolean isSkyblockTabInformationEntry(NetworkPlayerInfo networkPlayerInfo) {
        if (!HytilsConfig.cleanerSkyblockTabInfo) return false;
        return
            LocrawUtil.INSTANCE.getLocrawInfo() != null && LocrawUtil.INSTANCE.getLocrawInfo().getGameType() == LocrawInfo.GameType.SKYBLOCK &&
                skyblockTabInformationEntryGameProfileNameRegex.matcher(networkPlayerInfo.getGameProfile().getName()).matches() &&
                !validMinecraftUsername.matcher(networkPlayerInfo.getDisplayName().getUnformattedText()).matches();
    }
}
