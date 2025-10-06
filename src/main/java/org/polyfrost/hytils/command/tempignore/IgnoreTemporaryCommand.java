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

package org.polyfrost.hytils.command.tempignore;

import com.mojang.serialization.JsonOps;
import dev.deftu.omnicore.api.client.OmniClient;
import dev.deftu.omnicore.api.client.chat.OmniClientChat;
import dev.deftu.omnicore.api.client.chat.OmniClientChatSender;
import dev.deftu.omnicore.api.scheduling.TickSchedulers;
import dev.deftu.omnicore.api.serialization.OmniJson;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import org.polyfrost.oneconfig.api.config.v1.ConfigManager;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Command("ignoretemp")
public class IgnoreTemporaryCommand {
    private static final File IGNORE_FILE = ConfigManager.active().getFolder().resolve("tempignore.json").toFile();
    private static final Pattern REGEX = Pattern.compile("(\\d+)( ?)((month|day|hour|minute|second|millisecond|y|m|d|h|s)s?)", Pattern.CASE_INSENSITIVE);

    private static List<TemporaryIgnore> activeIgnores = new ArrayList<>();

    static {
        initialize();
        TickSchedulers.client().every(5, () -> {
            WorldClient world = OmniClient.getWorld();
            EntityPlayerSP player = OmniClient.getPlayer();
            if (world == null || player == null || activeIgnores.isEmpty()) {
                return;
            }

            Date now = new Date();
            List<TemporaryIgnore> toRemove = new ArrayList<>();
            for (TemporaryIgnore ignore : activeIgnores) {
                if (ignore.expiry > now.getTime()) {
                    continue;
                }

                toRemove.add(ignore);
                OmniClientChatSender.queue("/ignore remove " + ignore.username);
            }

            if (!toRemove.isEmpty()) {
                activeIgnores.removeAll(toRemove);
                Multithreading.submit(() -> {
                    try {
                        FileUtils.writeStringToFile(IGNORE_FILE, TemporaryIgnore.LIST_CODEC.encodeStart(JsonOps.INSTANCE, activeIgnores).getOrThrow(false, System.err::println).toString(), StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    @Handler
    private void main(GameProfile playerName, String time) {
        Multithreading.submit(() -> {
            try {
                long millis = addMillis(time.replace(",", "").replace(" ", ""));
                if (millis <= 0L) {
                    return;
                }

                Date expiryDate = new Date(new Date().getTime() + millis);
                TemporaryIgnore newIgnore = new TemporaryIgnore(playerName.getName(), expiryDate.getTime());
                for (TemporaryIgnore ignore : activeIgnores) {
                    if (ignore.username.equalsIgnoreCase(playerName.getName())) {
                        return;
                    }
                }

                activeIgnores.add(newIgnore);
                FileUtils.writeStringToFile(IGNORE_FILE, TemporaryIgnore.LIST_CODEC.encodeStart(JsonOps.INSTANCE, activeIgnores).getOrThrow(false, System.err::println).toString(), StandardCharsets.UTF_8);
                OmniClientChatSender.send("/ignore add " + playerName.getName());
                OmniClientChat.displayChatMessage("&r&aSuccessfully ignored &r&6&l" + playerName.getName() + "&r&a for &r&e&l" + time + "&r&a. The ignore will be removed after the specified period.");
            } catch (Exception e) {
                e.printStackTrace();
                OmniClientChat.displayChatMessage("&cAn error has occured and the user has not been ignored.");
            }
        });
    }

    @Handler
    private void add(GameProfile playerName, String time) {
        main(playerName, time);
    }

    @Handler
    private void remove(GameProfile playerName) {
        TemporaryIgnore toRemove = null;
        for (TemporaryIgnore ignore : activeIgnores) {
            if (ignore.username.equalsIgnoreCase(playerName.getName())) {
                toRemove = ignore;
                break;
            }
        }

        if (toRemove != null) {
            activeIgnores.remove(toRemove);
            Multithreading.submit(() -> {
                try {
                    FileUtils.writeStringToFile(IGNORE_FILE, TemporaryIgnore.LIST_CODEC.encodeStart(JsonOps.INSTANCE, activeIgnores).getOrThrow(false, System.err::println).toString(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            OmniClientChatSender.send("/ignore remove " + playerName.getName());
            OmniClientChat.displayChatMessage("&r&aSuccessfully removed temporary ignore for &r&6&l" + playerName.getName() + "&r&a.");
        } else {
            OmniClientChat.displayChatMessage("&cNo active temporary ignore found for &r&6&l" + playerName.getName() + "&r&c.");
        }
    }

    private static void initialize() {
        if (!IGNORE_FILE.exists()) {
            try {
                IGNORE_FILE.createNewFile();
                FileUtils.writeStringToFile(IGNORE_FILE, "[]", Charsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        Multithreading.submit(() -> {
            try {
                String content = FileUtils.readFileToString(IGNORE_FILE, Charsets.UTF_8);
                JsonElement element = OmniJson.parseJsonOrThrow(content);
                if (element.isJsonArray()) {
                    activeIgnores = TemporaryIgnore.LIST_CODEC.decode(JsonOps.INSTANCE, element).getOrThrow(false, System.err::println).getFirst();
                } else if (element.isJsonObject()) {
                    // update legacy format
                    JsonObject obj = element.getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                        if (entry.getValue().isJsonPrimitive() && entry.getValue().getAsJsonPrimitive().isNumber()) {
                            activeIgnores.add(new TemporaryIgnore(entry.getKey(), entry.getValue().getAsLong()));
                        }
                    }

                    FileUtils.writeStringToFile(IGNORE_FILE, TemporaryIgnore.LIST_CODEC.encodeStart(JsonOps.INSTANCE, activeIgnores).getOrThrow(false, System.err::println).toString(), StandardCharsets.UTF_8);
                }
            } catch (Exception e) {
                e.printStackTrace();
                IGNORE_FILE.delete();
                initialize();
            }
        });
    }

    private static long addMillis(String time) {
        long millis = 0L;
        Matcher matcher = REGEX.matcher(time);
        if (matcher.find()) {
            long timeTime = Long.parseLong(matcher.group(1));
            String name = matcher.group(4);
            for (TimeUnit unit : TimeUnit.values()) {
                if (name.equalsIgnoreCase(unit.pair.getKey()) || name.equalsIgnoreCase(unit.pair.getValue())) {
                    millis += unit.millis * timeTime;
                    break;
                }
            }
            String next = StringUtils.substringAfter(time, matcher.group());
            if (StringUtils.isNotBlank(next)) {
                millis += addMillis(next);
            }
        }
        return millis;
    }

    private enum TimeUnit {
        MONTH(new ImmutablePair<>("month", null), 2628000000L),
        DAY(new ImmutablePair<>("day", "d"), 86400000L),
        HOUR(new ImmutablePair<>("hour", "h"), 3600000L),
        MINUTE(new ImmutablePair<>("minute", "m"), 60000L),
        SECOND(new ImmutablePair<>("second", "s"), 1000L),
        MILLISECOND(new ImmutablePair<>("millisecond", "ms"), 1L);

        public final Pair<String, String> pair;
        public final Long millis;

        TimeUnit(Pair<String, String> pair, Long millis) {
            this.pair = pair;
            this.millis = millis;
        }
    }
}
