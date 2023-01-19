package glue_gunner4.botd.bot;

import com.mojang.datafixers.util.Pair;
import glue_gunner4.botd.config.BotdConfig;
import glue_gunner4.botd.config.PlayersConfig;
import glue_gunner4.botd.server.BotdServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.minecraft.text.Text;

public class Botd {
    public static JDA jda;

    public static Guild guild;
    public static TextChannel channel;
    public static boolean shouldAlertStop = true;

    public static void start (String token) {
        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .addEventListeners(new BotReplacd(), new BotdCommands(), new ShutdownCommand())
                .build();
        try {
            jda.getPresence().setStatus(OnlineStatus.INVISIBLE);
            jda.awaitReady();
            guild = jda.getGuildById(BotdConfig.BOTDCONFIG.getGuild());
            channel = guild.getTextChannelById(BotdConfig.BOTDCONFIG.getChannel());
            BotdCommands.registerCommands(guild);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sendS2DMessage(String player, String message) {
        for (Pair<String, String> p : PlayersConfig.PLAYERS) {
            message = message.replaceAll("@".concat(p.getFirst()), jda.getUserById(p.getSecond()).getAsMention());
        }
        String finalMessage = player.concat(": ".concat(message));
        channel.sendMessage(new DiscordMessage(finalMessage).generateMessage()).queue();
    }

    public static void sendD2SMessage(User author, String message) {
        BotdServer.server.getPlayerManager().broadcast(Text.of("<" + author.getName() + "> " + message), false);
    }

    public static Message sendMessage(String message) {
        message = BotReplacd.replace(message);
        return channel.sendMessage(message).complete();
    }

    public static int playerCount = 0;

    public static void setPresence(Three joined) {
        switch (joined) {
            case JOINED -> playerCount++;
            case LEFT -> playerCount--;
        }
        jda.getPresence().setPresence(Activity.playing(playerCount + " players online"), false);
        jda.getPresence().setStatus(playerCount == 0 ? OnlineStatus.IDLE : OnlineStatus.ONLINE);
    }

    public enum Three {
        JOINED,
        LEFT,
        UPDATE,
    }
}
