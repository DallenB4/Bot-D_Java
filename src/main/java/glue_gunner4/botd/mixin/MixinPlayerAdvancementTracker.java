package glue_gunner4.botd.mixin;

import glue_gunner4.botd.bot.Botd;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerAdvancementTracker.class)
public class MixinPlayerAdvancementTracker {
    @Shadow
    private ServerPlayerEntity owner;

    @Inject(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/PlayerAdvancementTracker;updateDisplay(Lnet/minecraft/advancement/Advancement;)V"))
    public void grantCriterion(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        if (advancement == null || advancement.getDisplay() == null || !advancement.getDisplay().shouldAnnounceToChat())
            return;
        Botd.sendMessage(owner.getName().getString().concat(" ") + Text.translatable("chat.type.advancement." + advancement.getDisplay().getFrame().getId()).getString() +
                " *" + advancement.getDisplay().getTitle().getString() +
                "*\n>>> " + advancement.getDisplay().getDescription().getString());
    }
}