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

package net.wyvest.hytilities;

import gg.essential.api.EssentialAPI;
import gg.essential.universal.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.wyvest.hytilities.command.*;
import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.handlers.cache.CosmeticsHandler;
import net.wyvest.hytilities.handlers.cache.HeightHandler;
import net.wyvest.hytilities.handlers.cache.PatternHandler;
import net.wyvest.hytilities.handlers.chat.ChatHandler;
import net.wyvest.hytilities.handlers.chat.modules.events.AchievementEvent;
import net.wyvest.hytilities.handlers.chat.modules.events.LevelupEvent;
import net.wyvest.hytilities.handlers.chat.modules.triggers.AutoQueue;
import net.wyvest.hytilities.handlers.game.GameCountdown;
import net.wyvest.hytilities.handlers.game.hardcore.HardcoreStatus;
import net.wyvest.hytilities.handlers.game.housing.HousingMusic;
import net.wyvest.hytilities.handlers.game.pit.PitLagReducer;
import net.wyvest.hytilities.handlers.general.AutoStart;
import net.wyvest.hytilities.handlers.general.CommandQueue;
import net.wyvest.hytilities.handlers.general.SoundHandler;
import net.wyvest.hytilities.handlers.language.LanguageHandler;
import net.wyvest.hytilities.handlers.lobby.LobbyChecker;
import net.wyvest.hytilities.handlers.lobby.bossbar.LobbyBossbar;
import net.wyvest.hytilities.handlers.lobby.limbo.LimboLimiter;
import net.wyvest.hytilities.handlers.lobby.limbo.LimboTitleFix;
import net.wyvest.hytilities.handlers.lobby.mysterybox.MysteryBoxStar;
import net.wyvest.hytilities.handlers.lobby.npc.NPCHider;
import net.wyvest.hytilities.handlers.render.ChestHighlighter;
import net.wyvest.hytilities.handlers.silent.SilentRemoval;
import net.wyvest.hytilities.util.HypixelAPIUtils;
import net.wyvest.hytilities.updater.Updater;
import net.wyvest.hytilities.util.friends.FriendCache;
import net.wyvest.hytilities.util.locraw.LocrawUtil;
import net.wyvest.hytilities.util.skyblock.SkyblockChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
    modid = Hytilities.MOD_ID,
    name = Hytilities.MOD_NAME,
    version = Hytilities.VERSION
)
public class Hytilities {

    public static final String MOD_ID = "hytilities-reborn";
    public static final String MOD_NAME = "Hytilities Reborn";
    public static final String VERSION = "1.1.0";

    @Mod.Instance(MOD_ID)
    public static Hytilities INSTANCE;

    public File jarFile;

    public File modDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), MOD_NAME);

    private HytilitiesConfig config;
    private final Logger logger = LogManager.getLogger("Hytilities Reborn");

    private final LanguageHandler languageHandler = new LanguageHandler();
    private final SkyblockChecker skyblockChecker = new SkyblockChecker();
    private final FriendCache friendCache = new FriendCache();
    private final HardcoreStatus hardcoreStatus = new HardcoreStatus();
    private final SilentRemoval silentRemoval = new SilentRemoval();
    private final CommandQueue commandQueue = new CommandQueue();
    private final LobbyChecker lobbyChecker = new LobbyChecker();
    private final ChatHandler chatHandler = new ChatHandler();
    private final LocrawUtil locrawUtil = new LocrawUtil();
    private final AutoQueue autoQueue = new AutoQueue();

    private boolean loadedCall;

    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists()) modDir.mkdirs();
        jarFile = event.getSourceFile();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new HytilitiesConfig();
        this.config.preload();
        new HytilitiesCommand().register();
        new HousingVisitCommand().register();
        final ClientCommandHandler commandRegister = ClientCommandHandler.instance;
        commandRegister.registerCommand(new PlayCommand());
        commandRegister.registerCommand(new SilentRemoveCommand());
        commandRegister.registerCommand(new SkyblockVisitCommand());
        CosmeticsHandler.INSTANCE.initialize();
        PatternHandler.INSTANCE.initialize();
        HeightHandler.INSTANCE.initialize();

        registerHandlers();
        Updater.update();
    }

    @Mod.EventHandler
    public void finishedStarting(FMLLoadCompleteEvent event) {
        this.loadedCall = true;
    }

    private void registerHandlers() {
        final EventBus eventBus = MinecraftForge.EVENT_BUS;

        // general stuff
        eventBus.register(autoQueue);
        eventBus.register(locrawUtil);
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
        eventBus.register(new NPCHider());
        eventBus.register(new LobbyBossbar());
        eventBus.register(new LimboLimiter());
        eventBus.register(new LimboTitleFix());
        eventBus.register(new MysteryBoxStar());

        // specific games
        eventBus.register(new PitLagReducer());
        eventBus.register(new HousingMusic());
        eventBus.register(new GameCountdown());

        // height overlay
        eventBus.register(HeightHandler.INSTANCE);
        eventBus.register(new HypixelAPIUtils());
    }

    public void sendMessage(String message) {
        EssentialAPI.getMinecraftUtil().sendMessage(ChatColor.GOLD + "[Hytilities Reborn] ", ChatColor.Companion.translateAlternateColorCodes('&', message));
    }

    public HytilitiesConfig getConfig() {
        return config;
    }

    public LocrawUtil getLocrawUtil() {
        return locrawUtil;
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
    @SuppressWarnings("unused")
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
