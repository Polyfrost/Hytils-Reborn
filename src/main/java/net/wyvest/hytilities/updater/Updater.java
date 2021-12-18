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

package net.wyvest.hytilities.updater;

import com.google.gson.JsonObject;
import gg.essential.api.EssentialAPI;
import gg.essential.api.utils.Multithreading;
import gg.essential.api.utils.WebUtil;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.config.HytilitiesConfig;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Updater {
    public static String updateUrl = "";
    public static String latestTag;
    public static boolean shouldUpdate = false;

    public static void update() {
        Multithreading.runAsync(() -> {
            try {
                JsonObject latestRelease = WebUtil.fetchJSON("https://api.github.com/repos/W-OVERFLOW/" + Hytilities.MOD_ID + "/releases/latest").getObject();
                latestTag = latestRelease.get("tag_name").getAsString();
                DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(StringUtils.substringBefore(Hytilities.VERSION, "-"));
                DefaultArtifactVersion latestVersion = new DefaultArtifactVersion(StringUtils.substringBefore(StringUtils.substringAfter(latestTag, "v"), "-"));
                if (currentVersion.compareTo(latestVersion) >= 0) {
                    return;
                }
                updateUrl = latestRelease.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
                if (!updateUrl.isEmpty()) {
                    if (HytilitiesConfig.showUpdate) {
                        EssentialAPI.getNotifications().push(Hytilities.MOD_NAME, Hytilities.MOD_NAME + " has a new update (" + latestTag + ")! Click here to download it automatically!", Updater::openUpdateGui);
                    }
                    shouldUpdate = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public static Unit openUpdateGui() {
        EssentialAPI.getGuiUtil().openScreen(new DownloadGui());
        return Unit.INSTANCE;
    }

    public static boolean download(String url, File file) {
        if (file.exists()) return true;
        url = url.replace(" ", "%20");
        try {
            WebUtil.downloadToFile(url, file, "Hytilities/" + Hytilities.VERSION);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return file.exists();
    }

    /**
     * Adapted from RequisiteLaunchwrapper under LGPLv3
     * https://github.com/Qalcyo/RequisiteLaunchwrapper/blob/main/LICENSE
     */
    public static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Opening Deleter task...");
            try {
                String runtime = getJavaRuntime();
                if (Minecraft.isRunningOnMac) {
                    Desktop.getDesktop().open(Hytilities.INSTANCE.jarFile.getParentFile());
                }
                File file = new File(Hytilities.INSTANCE.modDir.getParentFile(), "Deleter-1.2.jar");
                Runtime.getRuntime()
                    .exec("\"" + runtime + "\" -jar \"" + file.getAbsolutePath() + "\" \"" + Hytilities.INSTANCE.jarFile.getAbsolutePath() + "\"");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.currentThread().interrupt();
        }));
    }

    /**
     * Gets the current Java runtime being used.
     * @link https://stackoverflow.com/a/47925649
     */
    public static String getJavaRuntime() throws IOException {
        String os = System.getProperty("os.name");
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator +
            (os != null && os.toLowerCase(Locale.ENGLISH).startsWith("windows") ? "java.exe" : "java");
        if (!new File(java).isFile()) {
            throw new IOException("Unable to find suitable java runtime at "+java);
        }
        return java;
    }
}
