package glue_gunner4.botd.mixin;

import glue_gunner4.botd.bot.Botd;
import glue_gunner4.botd.server.AfkPlayer;
import glue_gunner4.botd.server.BotdServer;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {
    @Final
    @Shadow()
    private
    MinecraftServer server;
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "handleDecoratedMessage", at = @At(value = "TAIL"))
    private void handleDecoratedMessage(SignedMessage message, CallbackInfo ci) {
        Botd.sendS2DMessage(server.getPlayerManager().getPlayer(message.getSender()).getName().getString(), message.getContent().getString());
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick (CallbackInfo ci) {
        AfkPlayer afkPlayer = (AfkPlayer) player;
        if (afkPlayer.isAfk()) return;
        long afkDuration = (long) ((Util.getMeasuringTimeMs() - this.player.getLastActionTime()) / 1000d);
        if (afkDuration > 180) {
            afkPlayer.setAfk(true);
        }
    }
}
