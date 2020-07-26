package club.sk1er.hytilities;

import club.sk1er.hytilities.command.HytilitiesCommand;
import club.sk1er.hytilities.command.SilentRemoveCommand;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.adblock.AdBlocker;
import club.sk1er.hytilities.handlers.chat.adblock.ExternalAdBlocker;
import club.sk1er.hytilities.handlers.lobby.bossbar.LobbyBossbar;
import club.sk1er.hytilities.handlers.chat.cleaner.ChatCleaner;
import club.sk1er.hytilities.handlers.chat.whitechat.WhiteChat;
import club.sk1er.hytilities.handlers.game.GameChecker;
import club.sk1er.hytilities.handlers.lobby.limbo.LimboLimiter;
import club.sk1er.hytilities.handlers.lobby.npc.NPCHider;
import club.sk1er.hytilities.handlers.chat.restyler.ChatRestyler;
import club.sk1er.hytilities.handlers.server.ServerChecker;
import club.sk1er.hytilities.handlers.silent.SilentRemoval;
import club.sk1er.modcore.ModCoreInstaller;
import club.sk1er.mods.core.universal.ChatColor;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
    modid = Hytilities.MOD_ID,
    name = Hytilities.MOD_NAME,
    version = Hytilities.VERSION
)
public class Hytilities {

    public static final String MOD_ID = "hytilities";
    public static final String MOD_NAME = "Hytilities";
    public static final String VERSION = "1.0";

    @Mod.Instance(MOD_ID)
    public static Hytilities INSTANCE;

    private final HytilitiesConfig config = new HytilitiesConfig();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModCoreInstaller.initializeModCore(Minecraft.getMinecraft().mcDataDir);
        config.preload();

        ClientCommandHandler.instance.registerCommand(new HytilitiesCommand());
        ClientCommandHandler.instance.registerCommand(new SilentRemoveCommand());

        registerHandlers();
    }

    private void registerHandlers() {
        // general stuff
        MinecraftForge.EVENT_BUS.register(new ServerChecker());

        // chat
        MinecraftForge.EVENT_BUS.register(new AdBlocker());
        MinecraftForge.EVENT_BUS.register(new ExternalAdBlocker());
        MinecraftForge.EVENT_BUS.register(new ChatCleaner());
        MinecraftForge.EVENT_BUS.register(new ChatRestyler());
        MinecraftForge.EVENT_BUS.register(new SilentRemoval());
        MinecraftForge.EVENT_BUS.register(new WhiteChat());

        // lobby
        MinecraftForge.EVENT_BUS.register(new NPCHider());
        MinecraftForge.EVENT_BUS.register(new LobbyBossbar());
        MinecraftForge.EVENT_BUS.register(new LimboLimiter());
        MinecraftForge.EVENT_BUS.register(new GameChecker());
    }

    public void sendMessage(String message) {
        MinecraftUtils.sendMessage(ChatColor.GOLD + "[Hytilities] ", ChatColor.translateAlternateColorCodes('&', message));
    }

    public HytilitiesConfig getConfig() {
        return config;
    }
}
