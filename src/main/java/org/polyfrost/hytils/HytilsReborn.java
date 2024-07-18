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

package org.polyfrost.hytils;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.Notifications;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import org.polyfrost.hytils.command.*;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.*;
import org.polyfrost.hytils.handlers.chat.ChatHandler;
import org.polyfrost.hytils.handlers.chat.modules.events.AchievementEvent;
import org.polyfrost.hytils.handlers.chat.modules.events.LevelupEvent;
import org.polyfrost.hytils.handlers.chat.modules.triggers.AutoQueue;
import org.polyfrost.hytils.handlers.chat.modules.triggers.SilentRemoval;
import org.polyfrost.hytils.handlers.game.dropper.DropperHurtSound;
import org.polyfrost.hytils.handlers.game.duels.SumoRenderDistance;
import org.polyfrost.hytils.handlers.game.hardcore.HardcoreStatus;
import org.polyfrost.hytils.handlers.game.housing.HousingMusic;
import org.polyfrost.hytils.handlers.game.miniwalls.MiddleBeaconMiniWalls;
import org.polyfrost.hytils.handlers.game.pit.PitLagReducer;
import org.polyfrost.hytils.handlers.game.titles.CountdownTitles;
import org.polyfrost.hytils.handlers.game.titles.GameEndingTitles;
import org.polyfrost.hytils.handlers.game.titles.GameStartingTitles;
import org.polyfrost.hytils.handlers.game.uhc.MiddleWaypointUHC;
import org.polyfrost.hytils.handlers.general.AutoStart;
import org.polyfrost.hytils.handlers.general.CommandQueue;
import org.polyfrost.hytils.handlers.general.SoundHandler;
import org.polyfrost.hytils.handlers.language.LanguageHandler;
import org.polyfrost.hytils.handlers.lobby.armorstands.ArmorStandHider;
import org.polyfrost.hytils.handlers.lobby.limbo.LimboLimiter;
import org.polyfrost.hytils.handlers.lobby.limbo.LimboPmDing;
import org.polyfrost.hytils.handlers.lobby.limbo.LimboTitle;
import org.polyfrost.hytils.handlers.lobby.npc.NPCHandler;
import org.polyfrost.hytils.handlers.lobby.sound.SilentLobby;
import org.polyfrost.hytils.handlers.render.ChestHighlighter;
import org.polyfrost.hytils.util.HypixelAPIUtils;
import org.polyfrost.hytils.util.friends.FriendCache;
import org.polyfrost.hytils.util.ranks.RankType;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
    modid = HytilsReborn.MOD_ID,
    name = HytilsReborn.MOD_NAME,
    version = HytilsReborn.VERSION
)
public class HytilsReborn {
    public static final String MOD_ID = "@ID@";
    public static final String MOD_NAME = "@NAME@";
    public static final String VERSION = "@VER@";

    @Mod.Instance(MOD_ID)
    public static HytilsReborn INSTANCE;

    public File oldModDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), MOD_NAME);

    private HytilsConfig config;
    private final Logger logger = LogManager.getLogger("Hytils Reborn");

    private final LanguageHandler languageHandler = new LanguageHandler();
    private final FriendCache friendCache = new FriendCache();
    private final HardcoreStatus hardcoreStatus = new HardcoreStatus();
    private final SilentRemoval silentRemoval = new SilentRemoval();
    private final CommandQueue commandQueue = new CommandQueue();
    private final ChatHandler chatHandler = new ChatHandler();
    private final AutoQueue autoQueue = new AutoQueue();

    public boolean isPatcher;
    public boolean isChatting;
    public boolean isSk1erAutoGG;
    private boolean loadedCall;

    private RankType rank;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new HytilsConfig();

        CommandManager.INSTANCE.registerCommand(new HousingVisitCommand());
        CommandManager.INSTANCE.registerCommand(new HytilsCommand());
        CommandManager.INSTANCE.registerCommand(new IgnoreTemporaryCommand());
        CommandManager.INSTANCE.registerCommand(new RequeueCommand());
        CommandManager.INSTANCE.registerCommand(new SilentRemoveCommand());
        CommandManager.INSTANCE.registerCommand(new SkyblockVisitCommand());

        // We initialize it a different way because it requires the
        // GameNameParser to be initialized, and that depends on networking.
        PlayCommand.init();

        ArmorStandHandler.INSTANCE.initialize();
        CosmeticsHandler.INSTANCE.initialize();
        PatternHandler.INSTANCE.initialize();
        BedLocationHandler.INSTANCE.initialize();
        LocrawGamesHandler.INSTANCE.initialize();
        HeightHandler.INSTANCE.initialize();

        registerHandlers();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("tabulous")) {
            config.hideTabulous();
        }
        isPatcher = Loader.isModLoaded("patcher");
        isChatting = Loader.isModLoaded("chatting");
        if (isChatting) {
            try {
                Class.forName("org.polyfrost.chatting.chat.ChatTabs");
            } catch (Exception e) {
                isChatting = false;
                if (HytilsConfig.chattingIntegration) {
                    HytilsConfig.chattingIntegration = false;
                    config.save();
                    Notifications.INSTANCE.send("Hytils Reborn", "Hytils Reborn has detected Chatting, but it is not the latest version. Please update Chatting to the latest version.");
                }
            }
        }

        Multithreading.runAsync(() -> rank = HypixelAPIUtils.getRank(Minecraft.getMinecraft().getSession().getUsername()));
    }

    @Mod.EventHandler
    public void finishedStarting(FMLLoadCompleteEvent event) {
        this.loadedCall = true;
    }

    private void registerHandlers() {
        final EventBus eventBus = MinecraftForge.EVENT_BUS;

        // general stuff
        eventBus.register(autoQueue);
        eventBus.register(commandQueue);
        eventBus.register(languageHandler);
        eventBus.register(new AutoStart());
        eventBus.register(new SoundHandler());
        eventBus.register(new ChestHighlighter());

        // chat
        eventBus.register(chatHandler);
        eventBus.register(silentRemoval);
        eventBus.register(hardcoreStatus);
        eventBus.register(new AchievementEvent());
        eventBus.register(new LevelupEvent());

        // lobby
        eventBus.register(new ArmorStandHider());
        eventBus.register(new NPCHandler());
        eventBus.register(new LimboLimiter());
        eventBus.register(new LimboTitle());
        eventBus.register(new LimboPmDing());
        eventBus.register(new SilentLobby());

        // specific games
        eventBus.register(new PitLagReducer());
        eventBus.register(new HousingMusic());
        eventBus.register(new GameStartingTitles());
        eventBus.register(new GameEndingTitles());
        eventBus.register(new CountdownTitles());
        eventBus.register(new SumoRenderDistance());
        eventBus.register(new MiddleBeaconMiniWalls());
        eventBus.register(new MiddleWaypointUHC());
        eventBus.register(new DropperHurtSound());

        // height overlay
        EventManager.INSTANCE.register(HeightHandler.INSTANCE);

        eventBus.register(new HypixelAPIUtils());
    }

    public void sendMessage(String message) {
        UChat.chat(ChatColor.GOLD + "[" + MOD_NAME + "] " + ChatColor.Companion.translateAlternateColorCodes('&', message));
    }

    public HytilsConfig getConfig() {
        return config;
    }

    public HardcoreStatus getHardcoreStatus() {
        return hardcoreStatus;
    }

    public AutoQueue getAutoQueue() {
        return autoQueue;
    }

    public boolean isLoadedCall() {
        return loadedCall;
    }

    public void setLoadedCall(boolean loadedCall) {
        this.loadedCall = loadedCall;
    }

    public CommandQueue getCommandQueue() {
        return commandQueue;
    }

    public LanguageHandler getLanguageHandler() {
        return languageHandler;
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public Logger getLogger() {
        return logger;
    }

    public FriendCache getFriendCache() {
        return friendCache;
    }

    public RankType getRank() {
        return rank;
    }
}
