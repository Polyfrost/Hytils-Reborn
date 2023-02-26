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

package cc.woverflow.hytils.command;

import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.commands.annotations.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Command("ignoretemp")
public class IgnoreTemporaryCommand {
    private static JsonObject json;
    private static final JsonParser PARSER = new JsonParser();
    private static final File ignoreFile = ConfigUtils.getProfileFile("tempignore.json");
    private static final Pattern regex = Pattern.compile("(\\d+)( ?)((month|day|hour|minute|second|millisecond|y|m|d|h|s)s?)", Pattern.CASE_INSENSITIVE);

    static {
        initialize();
        Multithreading.runAsync(() -> {
            Timer timer = new Timer(1, ignored -> {
                if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
                    if (json != null) {
                        Date date = new Date();
                        boolean dirty = false;
                        for (Map.Entry<String, JsonElement> next : json.entrySet()) {
                            if (date.getTime() > next.getValue().getAsLong()) {
                                json.remove(next.getKey());
                                UChat.say("/ignore remove " + next.getKey());
                                dirty = true;
                            }
                        }
                        if (dirty) {
                            try {
                                FileUtils.writeStringToFile(ignoreFile, json.toString(), StandardCharsets.UTF_8);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            timer.start();
        });
    }

    @Main
    private void handle() {
        UChat.chat("Usage: /ignoretemp <player> <time>");
    }

    //@Main
    private void handle(@Description("Player Name") GameProfile playerName, @Description("Time") @Greedy String time) {
        Multithreading.runAsync(() -> {
            try {
                long millis = addMillis(time.replace(",", "").replace(" ", ""));
                json.addProperty(playerName.getName(), millis + new Date().getTime());
                try {
                    FileUtils.writeStringToFile(ignoreFile, json.toString(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                UChat.say("/ignore add " + playerName.getName());
                UChat.chat("&r&aSuccessfully ignored &r&6&l" + playerName.getName() + "&r&a for &r&e&l" + time + "&r&a. The ignore will be removed after the specified period.");
            } catch (Exception e) {
                e.printStackTrace();
                UChat.chat("&cAn error has occured and the user has not been ignored.");
            }
        });
    }

    @SubCommand(description = "Adds a player to the ignore list for a specified amount of time.")
    private void add(@Description("Player Name") GameProfile playerName, @Description("Time") @Greedy String time) {
        handle(playerName, time);
    }

    @SubCommand(description = "Removes a player from the ignore list.")
    private void remove(@Description("Player Name") GameProfile playerName) {
        json.remove(playerName.getName());
        Multithreading.runAsync(() -> {
            try {
                FileUtils.writeStringToFile(ignoreFile, json.toString(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        UChat.say("/ignore remove " + playerName.getName());
    }

    private static void initialize() {
        if (ignoreFile.exists()) {
            try {
                json = PARSER.parse(FileUtils.readFileToString(ignoreFile, StandardCharsets.UTF_8)).getAsJsonObject();
            } catch (Exception e) {
                e.printStackTrace();
                ignoreFile.delete();
                initialize();
            }
        } else {
            try {
                ignoreFile.createNewFile();
                FileUtils.writeStringToFile(ignoreFile, new JsonObject().toString(), Charsets.UTF_8);
                initialize();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private static long addMillis(String time) {
        long millis = 0L;
        Matcher matcher = regex.matcher(time);
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
