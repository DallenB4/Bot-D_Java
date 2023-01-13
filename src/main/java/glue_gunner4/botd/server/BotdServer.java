package glue_gunner4.botd.server;

import glue_gunner4.botd.bot.Botd;
import glue_gunner4.botd.config.BotdConfig;
import glue_gunner4.botd.config.PlayersConfig;
import glue_gunner4.botd.event.ServerEvents;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.SERVER)
public class BotdServer implements DedicatedServerModInitializer {
    private static final Logger LOGGER = LogManager.getLogger("Bot'D");
    public static void logInfo(String message) {
        LOGGER.log(Level.INFO, message);
    }public static void logError(String message) {
        LOGGER.log(Level.ERROR, message);
    }
    public static MinecraftServer server;

    @Override
    public void onInitializeServer() {
        BotdConfig.registerConfig();
        PlayersConfig.registerConfig();
        Botd.start(BotdConfig.BOTDCONFIG.getToken());
        ServerEvents.registerEvents();
    }
}
