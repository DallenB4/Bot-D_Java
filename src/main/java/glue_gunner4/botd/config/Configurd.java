package glue_gunner4.botd.config;

import glue_gunner4.botd.server.BotdServer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface Configurd {
    static Path getConfigFilePath (String name) {
        Path path = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "/botd/" + name);
        path.getParent().toFile().mkdirs();
        return path;
    }
}
