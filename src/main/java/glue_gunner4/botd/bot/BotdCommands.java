package glue_gunner4.botd.bot;

import glue_gunner4.botd.server.BotdServer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.minecraft.text.Text;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BotdCommands extends ListenerAdapter {
    public static void registerCommands(Guild guild) {
        guild.updateCommands().addCommands(new SlashCommandData[]{
                Commands.slash("stop", "Stops the server.").setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                Commands.slash("say", "Displays a message to the server.").addOption(OptionType.STRING, "text", "Text to display.", true).setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                Commands.slash("delete", "Deletes x number of messages.").addOption(OptionType.INTEGER, "number", "Number of messsages to delete.", true).setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                Commands.slash("parrot", "Repeats what you type to this channel.").addOption(OptionType.STRING, "text", "Text to repeat.", true).setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                InfoCommand.COMMAND
        }).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "stop" -> stopCommand(event);
            case "say" -> sayCommand(event);
            case "delete" -> deleteHistoryCommand(event);
            case "parrot" -> parrotCommand(event);
            case "info" -> InfoCommand.infoCommand(event);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (event.getName().equals("info") && event.getFocusedOption().getName().equals("type")) {
            List<Command.Choice> options = Stream.of(InfoCommand.OPTIONS)
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                    .map(word -> new Command.Choice(word, word))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }

    public void parrotCommand(SlashCommandInteractionEvent event) {
        String text = event.getOption("text").getAsString();
        InteractionHook reply = event.reply("Parroting \"" + text + "\"...").setEphemeral(true).complete();
        event.getChannel().sendMessage(text).complete();
        reply.deleteOriginal().complete();
    }

    public void stopCommand(SlashCommandInteractionEvent event) {
        InteractionHook reply = event.reply("Stopping the server...").setEphemeral(true).complete();
        Botd.shouldAlertStop = false;
        BotdServer.server.stop(true);
        reply.deleteOriginal().complete();
    }

    public void sayCommand(SlashCommandInteractionEvent event) {
        InteractionHook action = event.reply("Sending message...").setEphemeral(true).complete();
        String message = event.getOption("text").getAsString();
        BotdServer.server.getPlayerManager().broadcast(Text.of("[Server] " + message), false);
        Botd.sendS2DMessage("Server", message);
        action.deleteOriginal().complete();
    }

    public void deleteHistoryCommand(SlashCommandInteractionEvent event) {
        int number = event.getOption("number").getAsInt();
        InteractionHook reply = event.reply("Deleting " + number + " message" + (number != 0 ? "s" : "") + "...").setEphemeral(true).complete();
        List<Message> messages = Botd.channel.getHistory().retrievePast(number + 1).complete();
        for (int i = 1; i <= number; i++) {
            try {
                messages.get(i).delete().complete();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        reply.deleteOriginal().complete();
    }
}
