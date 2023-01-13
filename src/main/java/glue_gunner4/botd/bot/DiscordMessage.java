package glue_gunner4.botd.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.ArrayList;

public class DiscordMessage {
    private String text;
    public DiscordMessage(String text) {
        this.text = text;
    }

    public MessageCreateData generateMessage() {
        MessageCreateBuilder out = new MessageCreateBuilder();
        out.addContent(text);
        final ArrayList<Message.MentionType> mentions = new ArrayList<>();
        mentions.add(Message.MentionType.USER);
        mentions.add(Message.MentionType.HERE);
        out.setAllowedMentions(mentions);
        return out.build();
    }
}
