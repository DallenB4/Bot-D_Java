package glue_gunner4.botd.mixin;

import glue_gunner4.botd.bot.Botd;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
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

    @Inject(method = "handleDecoratedMessage", at = @At(value = "TAIL"))
    private void handleDecoratedMessage(SignedMessage message, CallbackInfo ci) {
        Botd.sendS2DMessage(server.getPlayerManager().getPlayer(message.getSender()).getName().getString(), message.getContent().getString());
    }
}
