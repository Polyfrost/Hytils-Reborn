package club.sk1er.hytilities;

import club.sk1er.hytilities.command.HytilitiesCommand;
import club.sk1er.hytilities.command.SilentRemoveCommand;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatHandler;
import club.sk1er.hytilities.handlers.chat.autoqueue.AutoQueue;
import club.sk1er.hytilities.handlers.chat.events.AchievementEvent;
import club.sk1er.hytilities.handlers.chat.events.LevelupEvent;
import club.sk1er.hytilities.handlers.game.hardcore.HardcoreStatus;
import club.sk1er.hytilities.handlers.general.AutoStart;
import club.sk1er.hytilities.handlers.lobby.LobbyChecker;
import club.sk1er.hytilities.handlers.lobby.bossbar.LobbyBossbar;
import club.sk1er.hytilities.handlers.lobby.limbo.LimboLimiter;
import club.sk1er.hytilities.handlers.lobby.npc.NPCHider;
import club.sk1er.hytilities.handlers.silent.SilentRemoval;
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

    private HardcoreStatus hardcoreStatus;
    private SilentRemoval silentRemoval;
    private LobbyChecker lobbyChecker;
    private LocrawUtil locrawUtil;
    private AutoQueue autoQueue;

    private boolean loadedCall;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModCoreInstaller.initializeModCore(Minecraft.getMinecraft().mcDataDir);
        config.preload();

        ClientCommandHandler.instance.registerCommand(new HytilitiesCommand());
        ClientCommandHandler.instance.registerCommand(new SilentRemoveCommand());

        registerHandlers();
    }

    @Mod.EventHandler
    public void finishedStarting(FMLLoadCompleteEvent event) {
        this.loadedCall = true;
    }

    private void registerHandlers() {
        // general stuff
        MinecraftForge.EVENT_BUS.register(locrawUtil = new LocrawUtil());
        MinecraftForge.EVENT_BUS.register(autoQueue = new AutoQueue());
        MinecraftForge.EVENT_BUS.register(new AutoStart());

        // chat
        MinecraftForge.EVENT_BUS.register(silentRemoval = new SilentRemoval());
        MinecraftForge.EVENT_BUS.register(hardcoreStatus = new HardcoreStatus());
        MinecraftForge.EVENT_BUS.register(new ChatHandler());
        MinecraftForge.EVENT_BUS.register(new AchievementEvent());
        MinecraftForge.EVENT_BUS.register(new LevelupEvent());

        // lobby
        MinecraftForge.EVENT_BUS.register(lobbyChecker = new LobbyChecker());
        MinecraftForge.EVENT_BUS.register(new NPCHider());
        MinecraftForge.EVENT_BUS.register(new LobbyBossbar());
        MinecraftForge.EVENT_BUS.register(new LimboLimiter());
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
}
