package glue_gunner4.botd.mixin;

import glue_gunner4.botd.server.AfkPlayer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayerEntity.class)
public class MixinServerPlayerEntity implements AfkPlayer {
    @Unique
    private boolean isAfk;

    public boolean isAfk() {
        return isAfk;
    }

    @Override
    public void setAfk(boolean isAfk) {
        this.isAfk = isAfk;
    }

    @Inject(method = "updateLastActionTime", at = @At("TAIL"))
    private void onActionTimeUpdate(CallbackInfo ci) {
        if (!isAfk) return;
        setAfk(false);
    }
}