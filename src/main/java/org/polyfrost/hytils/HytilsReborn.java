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

//#if FORGE
//$$ import net.minecraftforge.fml.common.Mod;
//$$ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//$$ import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
//$$ import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
//$$ import net.minecraft.client.Minecraft;
//#else
import net.fabricmc.api.ClientModInitializer;
//#endif

import dev.deftu.omnicore.api.loader.OmniLoader;
import dev.deftu.omnicore.api.client.chat.OmniClientChat;
import dev.deftu.omnicore.api.client.player.OmniClientPlayer;
import dev.deftu.textile.minecraft.MCSimpleMutableTextHolder;
import dev.deftu.textile.minecraft.MCSimpleTextHolder;
import dev.deftu.textile.minecraft.MCTextFormat;
import dev.deftu.textile.minecraft.MCTextHolder;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.ui.v1.Notifications;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;
import org.polyfrost.hytils.command.*;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.*;
import org.polyfrost.hytils.handlers.chat.ChatHandler;
import org.polyfrost.hytils.handlers.chat.modules.events.AchievementEvent;
import org.polyfrost.hytils.handlers.chat.modules.events.LevelupEvent;
import org.polyfrost.hytils.handlers.chat.modules.triggers.AutoGG;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//#if FORGE
//$$ @Mod(modid = HytilsReborn.ID, version = HytilsReborn.VERSION, name = HytilsReborn.NAME)
//#endif
public class HytilsReborn
    //#if FABRIC
    implements ClientModInitializer
    //#endif
{
    public static final String ID = "@MOD_ID@";
    public static final String NAME = "@MOD_NAME@";
    public static final String VERSION = "@MOD_VERSION@";

    //#if FORGE
    //$$ @Mod.Instance(ID)
    //#endif
    public static HytilsReborn INSTANCE;

    private static MCTextHolder<?> prefix;

    //#if FORGE && MC == 1.8.9
    //$$ public File oldModDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), NAME);
    //#endif

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

    private void initialize() {
        //#if FABRIC
        //$$ INSTANCE = FabricLoader.getInstance()
        //$$     .getEntrypointContainers("client", ClientModInitializer.class)
        //$$     .stream()
        //$$     .map(EntrypointContainer::getEntrypoint)
        //$$     .filter(HytilsReborn.class::isInstance)
        //$$     .map(HytilsReborn.class::cast)
        //$$     .findFirst()
        //$$     .orElseThrow(() -> new IllegalStateException("Could not find HytilsReborn entrypoint"));
        //#endif

        config = new HytilsConfig();

        CommandManager.register(new HousingVisitCommand());
        CommandManager.register(new HytilsCommand());
        CommandManager.register(new IgnoreTemporaryCommand());
        CommandManager.register(new RequeueCommand());
        CommandManager.register(new SilentRemoveCommand());
        CommandManager.register(new SkyblockVisitCommand());

        // We initialize it a different way because it requires the
        // GameNameParser to be initialized, and that depends on networking.
        PlayCommand.init();

        ArmorStandHandler.INSTANCE.initialize();
        CosmeticsHandler.INSTANCE.initialize();
        PatternHandler.INSTANCE.initialize();
        BedLocationHandler.INSTANCE.initialize();
        LocrawGamesHandler.INSTANCE.initialize();
        HeightHandler.INSTANCE.initialize();
        new HypixelAPIUtils().initialize();

        registerHandlers();
    }

    private void postInitialize() {
        if (OmniLoader.isLoaded("tabulous")) {
            config.hideTabulous();
        }

        isPatcher = OmniLoader.isLoaded("patcher");
        isChatting = OmniLoader.isLoaded("chatting");
        if (isChatting) {
            try {
                Class.forName("org.polyfrost.chatting.chat.ChatTabs");
            } catch (Exception e) {
                isChatting = false;
                if (HytilsConfig.chattingIntegration) {
                    HytilsConfig.chattingIntegration = false;
                    config.save();
                    Notifications.enqueue(Notifications.Type.Warning, "Hytils Reborn", "Hytils Reborn has detected Chatting, but it is not the latest version. Please update Chatting to the latest version.");
                }
            }
        }

        Multithreading.submit(() -> rank = HypixelAPIUtils.getRank(OmniClientPlayer.getPlayerName()));
    }

    //#if FORGE
    //$$ @Mod.EventHandler
    //$$ public void fmlInit(FMLInitializationEvent event) {
    //$$     initialize();
    //$$ }

    //$$ @Mod.EventHandler
    //$$ public void fmlPostInit(FMLPostInitializationEvent event) {
    //$$     postInitialize();
    //$$ }

    //$$ @Mod.EventHandler
    //$$ public void finishedStarting(FMLLoadCompleteEvent event) {
    //$$     this.loadedCall = true;
    //$$ }
    //#else
    @Override
    public void onInitializeClient() {
          initialize();
          postInitialize();
    }
    //#endif

    private void registerHandlers() {
        final EventManager eventBus = EventManager.INSTANCE;

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
        EventManager.INSTANCE.register(AutoGG.INSTANCE);

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
    }

    public void sendMessage(MCTextHolder<?> text) {
        if (prefix == null) {
            prefix = new MCSimpleTextHolder("[" + NAME + "] ").withFormatting(MCTextFormat.GOLD);
        }

        MCSimpleMutableTextHolder newText = new MCSimpleMutableTextHolder("");
        newText.append(prefix);
        newText.append(text);
        text = newText;

        OmniClientChat.displayChatMessage(text);
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
