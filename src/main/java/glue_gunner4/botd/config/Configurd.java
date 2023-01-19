package glue_gunner4.botd.config;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface Configurd {
    static Path getConfigFilePath (String name) {
        Path path = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "/botd/" + name);
        path.getParent().toFile().mkdirs();
        return path;
    }
}
