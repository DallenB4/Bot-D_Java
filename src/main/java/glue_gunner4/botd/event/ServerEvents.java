package glue_gunner4.botd.event;

import glue_gunner4.botd.bot.Botd;
import glue_gunner4.botd.bot.DiscordMessage;
import glue_gunner4.botd.server.BotdServer;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ServerEvents {
    public static void registerEvents() {
        ServerLifecycleEvents.SERVER_STARTING.register(ServerEvents::onServerStarting);
        ServerLifecycleEvents.SERVER_STARTED.register(ServerEvents::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPED.register(ServerEvents::onServerStopped);


        ServerPlayConnectionEvents.JOIN.register(ServerEvents::onPlayerJoined);
        ServerPlayConnectionEvents.DISCONNECT.register(ServerEvents::onPlayerDisconnect);

        ServerLivingEntityEvents.AFTER_DEATH.register(ServerEvents::onLivingEntityDeath);
    }

    private static Message startingMessage;

    private static void onServerStarting(MinecraftServer server) {
        BotdServer.server = server;
        Botd.jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        Botd.jda.getPresence().setPresence(Activity.playing("Server starting 👀"), false);
        startingMessage = Botd.sendMessage("**Server Starting!**");
    }

    private static void onServerStarted(MinecraftServer server) {
        try {
            startingMessage.editMessage("**Server Start'D!**").complete();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        Botd.setPresence(Botd.Three.UPDATE);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> Botd.setPresence(Botd.Three.UPDATE), 15, 15, TimeUnit.SECONDS);
    }

    private static void onServerStopped(MinecraftServer server) {
        Botd.sendMessage("**Server Stopped!**");
        if (Botd.shouldAlertStop) {
            String adminRole = Botd.guild.getRoleById("1046834715212128407").getAsMention();
            Botd.channel.sendMessage(new DiscordMessage("GAK! " + adminRole + "! The server may have crashed!").generateMessage()).complete();
        }
        Botd.jda.shutdownNow();
    }

    private static void onPlayerJoined(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        Botd.sendMessage("*" + handler.player.getName().getString() + " joined the game*");
        Botd.setPresence(Botd.Three.JOINED);
    }

    private static void onPlayerDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        Botd.sendMessage("*" + handler.player.getName().getString() + " left the game*");
        Botd.setPresence(Botd.Three.LEFT);
    }

    private static void onLivingEntityDeath(LivingEntity livingEntity, DamageSource damageSource) {
        if (livingEntity instanceof PlayerEntity) {
            Text deathMessage = damageSource.getDeathMessage(livingEntity);
            Botd.sendMessage(deathMessage.getString());
        }
    }
}
