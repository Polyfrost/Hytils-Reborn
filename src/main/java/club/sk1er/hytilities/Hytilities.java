package club.sk1er.hytilities;

import club.sk1er.hytilities.command.HytilitiesCommand;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.adblock.AdBlocker;
import club.sk1er.hytilities.handlers.adblock.ExternalAdBlocker;
import club.sk1er.hytilities.handlers.bossbar.LobbyBossbar;
import club.sk1er.hytilities.handlers.cleaner.ChatCleaner;
import club.sk1er.hytilities.handlers.limbo.LimboLimiter;
import club.sk1er.hytilities.handlers.npc.NPCHider;
import club.sk1er.hytilities.handlers.server.ServerChecker;
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

        registerHandlers();
    }

    private void registerHandlers() {
        // general stuff
        MinecraftForge.EVENT_BUS.register(new ServerChecker());

        // chat
        MinecraftForge.EVENT_BUS.register(new AdBlocker());
        MinecraftForge.EVENT_BUS.register(new ExternalAdBlocker());
        MinecraftForge.EVENT_BUS.register(new ChatCleaner());

        // lobby
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
}
