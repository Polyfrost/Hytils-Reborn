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

package cc.woverflow.hytils;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import cc.woverflow.hytils.command.*;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.cache.*;
import cc.woverflow.hytils.handlers.chat.ChatHandler;
import cc.woverflow.hytils.handlers.chat.modules.events.AchievementEvent;
import cc.woverflow.hytils.handlers.chat.modules.events.LevelupEvent;
import cc.woverflow.hytils.handlers.chat.modules.triggers.AutoQueue;
import cc.woverflow.hytils.handlers.game.GameEndingCountdownTitles;
import cc.woverflow.hytils.handlers.game.GameEndingTitles;
import cc.woverflow.hytils.handlers.game.GameStartingTitles;
import cc.woverflow.hytils.handlers.game.duels.SumoRenderDistance;
import cc.woverflow.hytils.handlers.game.hardcore.HardcoreStatus;
import cc.woverflow.hytils.handlers.game.housing.HousingMusic;
import cc.woverflow.hytils.handlers.game.miniwalls.MiddleBeaconMiniWalls;
import cc.woverflow.hytils.handlers.game.pit.PitLagReducer;
import cc.woverflow.hytils.handlers.game.uhc.MiddleWaypointUHC;
import cc.woverflow.hytils.handlers.general.AutoStart;
import cc.woverflow.hytils.handlers.general.CommandQueue;
import cc.woverflow.hytils.handlers.general.SoundHandler;
import cc.woverflow.hytils.handlers.language.LanguageHandler;
import cc.woverflow.hytils.handlers.lobby.LobbyChecker;
import cc.woverflow.hytils.handlers.lobby.armorstands.ArmorStandHider;
import cc.woverflow.hytils.handlers.lobby.bossbar.LobbyBossbar;
import cc.woverflow.hytils.handlers.lobby.limbo.LimboLimiter;
import cc.woverflow.hytils.handlers.lobby.limbo.LimboPmDing;
import cc.woverflow.hytils.handlers.lobby.limbo.LimboTitle;
import cc.woverflow.hytils.handlers.lobby.mysterybox.MysteryBoxStar;
import cc.woverflow.hytils.handlers.lobby.npc.NPCHandler;
import cc.woverflow.hytils.handlers.render.ChestHighlighter;
import cc.woverflow.hytils.handlers.silent.SilentRemoval;
import cc.woverflow.hytils.util.HypixelAPIUtils;
import cc.woverflow.hytils.util.friends.FriendCache;
import cc.woverflow.hytils.util.skyblock.SkyblockChecker;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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

    public File modDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), MOD_NAME);

    private HytilsConfig config;
    private final Logger logger = LogManager.getLogger("Hytils Reborn");

    private final LanguageHandler languageHandler = new LanguageHandler();
    private final SkyblockChecker skyblockChecker = new SkyblockChecker();
    private final FriendCache friendCache = new FriendCache();
    private final HardcoreStatus hardcoreStatus = new HardcoreStatus();
    private final SilentRemoval silentRemoval = new SilentRemoval();
    private final CommandQueue commandQueue = new CommandQueue();
    private final LobbyChecker lobbyChecker = new LobbyChecker();
    private final ChatHandler chatHandler = new ChatHandler();
    private final AutoQueue autoQueue = new AutoQueue();

    public boolean isPatcher;
    public boolean isChatting;
    private boolean loadedCall;

    public String rank;

    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {

        if (!modDir.exists() && !modDir.mkdirs()) {
            throw new RuntimeException("Failed to create mod directory! Please report this https://polyfrost.cc/discord");
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new HytilsConfig();

        CommandManager.INSTANCE.registerCommand(new HousingVisitCommand());
        CommandManager.INSTANCE.registerCommand(new HytilsCommand());
        CommandManager.INSTANCE.registerCommand(new IgnoreTemporaryCommand());
        CommandManager.INSTANCE.registerCommand(new LimboCommand());
        CommandManager.INSTANCE.registerCommand(new SilentRemoveCommand());
        CommandManager.INSTANCE.registerCommand(new SkyblockVisitCommand());

        // We initialize it a different way because it requires the
        // GameNameParser to be initialized, and that depends on networking.
        PlayCommand.init();

        ArmorStandHandler.INSTANCE.initialize();
        CosmeticsHandler.INSTANCE.initialize();
        PatternHandler.INSTANCE.initialize();
        //BedLocationHandler.INSTANCE.initialize();
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

        rank = HypixelAPIUtils.getRank(Minecraft.getMinecraft().getSession().getUsername());
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
        eventBus.register(lobbyChecker);
        eventBus.register(new ArmorStandHider());
        eventBus.register(new NPCHandler());
        eventBus.register(new LobbyBossbar());
        eventBus.register(new LimboLimiter());
        eventBus.register(new LimboTitle());
        eventBus.register(new LimboPmDing());
        eventBus.register(new MysteryBoxStar());

        // specific games
        eventBus.register(new PitLagReducer());
        eventBus.register(new HousingMusic());
        eventBus.register(new GameStartingTitles());
        eventBus.register(new GameEndingTitles());
        eventBus.register(new GameEndingCountdownTitles());
        eventBus.register(new SumoRenderDistance());
        eventBus.register(new MiddleBeaconMiniWalls());
        eventBus.register(new MiddleWaypointUHC());

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

    public SilentRemoval getSilentRemoval() {
        return silentRemoval;
    }

    public LobbyChecker getLobbyChecker() {
        return lobbyChecker;
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

    public SkyblockChecker getSkyblockChecker() {
        return skyblockChecker;
    }

    public FriendCache getFriendCache() {
        return friendCache;
    }
}
