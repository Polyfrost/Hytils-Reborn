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

import gg.essential.api.EssentialAPI;
import gg.essential.api.gui.ConfirmationModalBuilder;
import gg.essential.api.utils.Multithreading;
import gg.essential.elementa.WindowScreen;
import kotlin.Unit;
import net.wyvest.hytilities.Hytilities;

import java.io.File;

public class DownloadGui extends WindowScreen {
    public DownloadGui() {
        super(true, true, true, -1);
    }

    @Override
    public void initScreen(int width, int height) {
        super.initScreen(width, height);
        EssentialAPI.getEssentialComponentFactory().build(makeModal()).setChildOf(getWindow());
    }

    private ConfirmationModalBuilder makeModal() {
        ConfirmationModalBuilder builder = new ConfirmationModalBuilder();
        builder.setText("Are you sure you want to update?");
        builder.setSecondaryText("(This will update from v" + Hytilities.VERSION + " to " + Updater.latestTag + ")");
        builder.setOnConfirm((wyvest) -> {
            restorePreviousScreen();
            Multithreading.runAsync(() -> {
                if (Updater.download(
                    Updater.updateUrl,
                    new File(
                        "mods/" + Hytilities.MOD_NAME + "-" + Updater.latestTag.substring(Updater.latestTag.indexOf("v")) + ".jar"
                    )
                ) && Updater.download(
                    "https://github.com/Wyvest/Deleter/releases/download/v1.2/Deleter-1.2.jar",
                    new File(Hytilities.INSTANCE.modDir.getParentFile(), "Deleter-1.2.jar")
                )
                ) {
                    EssentialAPI.getNotifications()
                        .push(Hytilities.MOD_NAME, "The ingame updater has successfully installed the newest version.");
                    Updater.addShutdownHook();
                    Updater.shouldUpdate = false;
                } else {
                    EssentialAPI.getNotifications().push(
                        Hytilities.MOD_NAME,
                        "The ingame updater has NOT installed the newest version as something went wrong."
                    );
                }
            });
            return Unit.INSTANCE;
        });

        builder.setOnDeny(() -> {
            restorePreviousScreen();
            return Unit.INSTANCE;
        });

        return builder;
    }
}
