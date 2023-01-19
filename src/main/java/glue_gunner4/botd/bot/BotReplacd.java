package glue_gunner4.botd.bot;

import glue_gunner4.botd.config.BotdConfig;
import glue_gunner4.botd.config.EmojisConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class BotReplacd extends ListenerAdapter implements EventListener {
    private static final String[] edStrings = {"ed", "ED", "Ed"};

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        replace(event.getMessage());
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        replace(event.getMessage());
    }

    public static String replace(String text) {
        if (!BotdConfig.BOTDCONFIG.getShouldReplace())
            return text;
        return text.replaceAll(String.join("|", edStrings), "'D");
    }

    public static void replace(Message message) {
        if (!BotdConfig.BOTDCONFIG.getShouldReplace())
            return;
        if (message.getAuthor() == Botd.jda.getSelfUser()) {
            return;
        }
        if (message.getChannel() == Botd.channel) {
            Botd.sendD2SMessage(message.getAuthor(), replace(message.getContentDisplay()));
        }
        String messageText = message.getContentDisplay();

        for (String s : edStrings) {
            if (messageText.contains(s)) {
                String replacd = messageText.replaceAll(String.join("|", edStrings), "'D");
                MessageCreateData messageToSend = new MessageCreateBuilder()
                        .addContent("I have fix'D your message:\n>>> " + replacd)
                                .build();
                message.reply(messageToSend).complete().delete().queueAfter(1L, TimeUnit.MINUTES);
                return;
            }
        }
        if (messageText.contains("'D")) {
            message.addReaction(Emoji.fromFormatted(EmojisConfig.getEmojiFromName("the_d"))).queue();
        }
    }
}
