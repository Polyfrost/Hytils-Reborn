/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
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

package club.sk1er.hytilities;

import club.sk1er.hytilities.command.HousingVisitCommand;
import club.sk1er.hytilities.command.HytilitiesCommand;
import club.sk1er.hytilities.command.PlayCommand;
import club.sk1er.hytilities.command.SilentRemoveCommand;
import club.sk1er.hytilities.command.SkyblockVisitCommand;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatHandler;
import club.sk1er.hytilities.handlers.chat.modules.events.AchievementEvent;
import club.sk1er.hytilities.handlers.chat.modules.events.LevelupEvent;
import club.sk1er.hytilities.handlers.chat.modules.triggers.AutoQueue;
import club.sk1er.hytilities.handlers.game.hardcore.HardcoreStatus;
import club.sk1er.hytilities.handlers.game.housing.HousingMusic;
import club.sk1er.hytilities.handlers.game.pit.PitLagReducer;
import club.sk1er.hytilities.handlers.general.AutoStart;
import club.sk1er.hytilities.handlers.general.CommandQueue;
import club.sk1er.hytilities.handlers.language.LanguageHandler;
import club.sk1er.hytilities.handlers.lobby.LobbyChecker;
import club.sk1er.hytilities.handlers.lobby.bossbar.LobbyBossbar;
import club.sk1er.hytilities.handlers.lobby.limbo.LimboLimiter;
import club.sk1er.hytilities.handlers.lobby.mysterybox.MysteryBoxStar;
import club.sk1er.hytilities.handlers.lobby.npc.NPCHider;
import club.sk1er.hytilities.handlers.silent.SilentRemoval;
import club.sk1er.hytilities.tweaker.asm.EntityPlayerSPTransformer;
import club.sk1er.hytilities.tweaker.asm.GuiIngameForgeTransformer;
import club.sk1er.hytilities.util.locraw.LocrawUtil;
import club.sk1er.modcore.ModCoreInstaller;
import club.sk1er.mods.core.universal.ChatColor;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.objectweb.asm.tree.ClassNode;

@Mod(
    modid = Hytilities.MOD_ID,
    name = Hytilities.MOD_NAME,
    version = Hytilities.VERSION
)
public class Hytilities {

    public static final String MOD_ID = "hytilities";
    public static final String MOD_NAME = "Hytilities";
    public static final String VERSION = "0.1";

    @Mod.Instance(MOD_ID)
    public static Hytilities INSTANCE;

    private final HytilitiesConfig config = new HytilitiesConfig();

    private final LanguageHandler languageHandler = new LanguageHandler();
    private final HardcoreStatus hardcoreStatus = new HardcoreStatus();
    private final SilentRemoval silentRemoval = new SilentRemoval();
    private final CommandQueue commandQueue = new CommandQueue();
    private final LobbyChecker lobbyChecker = new LobbyChecker();
    private final ChatHandler chatHandler = new ChatHandler();
    private final LocrawUtil locrawUtil = new LocrawUtil();
    private final AutoQueue autoQueue = new AutoQueue();

    private boolean loadedCall;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModCoreInstaller.initializeModCore(Minecraft.getMinecraft().mcDataDir);
        this.config.preload();

        final ClientCommandHandler commandRegister = ClientCommandHandler.instance;
        commandRegister.registerCommand(new PlayCommand());
        commandRegister.registerCommand(new HytilitiesCommand());
        commandRegister.registerCommand(new HousingVisitCommand());
        commandRegister.registerCommand(new SilentRemoveCommand());
        commandRegister.registerCommand(new SkyblockVisitCommand());

        registerHandlers();
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
        eventBus.register(new MysteryBoxStar());

        // specific games
        eventBus.register(new PitLagReducer());
        eventBus.register(new HousingMusic());
    }

    public void sendMessage(String message) {
        MinecraftUtils.sendMessage(ChatColor.GOLD + "[Hytilities] ", ChatColor.translateAlternateColorCodes('&', message));
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

    /**
     * Used in {@link GuiIngameForgeTransformer#transform(ClassNode, String)}
     */
    @SuppressWarnings("unused")
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

    /**
     * Used in {@link EntityPlayerSPTransformer}
     */
    @SuppressWarnings("unused")
    public ChatHandler getChatHandler() {
        return chatHandler;
    }
}
