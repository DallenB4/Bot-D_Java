package glue_gunner4.botd.server;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;

public interface AfkPlayer {
    boolean isAfk();

    void setAfk(boolean isAfk);

    default long getAfkTime() {
        if (this instanceof ServerPlayerEntity player) {
            return (long) ((Util.getMeasuringTimeMs() - player.getLastActionTime()) / 1000d);
        }
        return 0L;
    }
}
