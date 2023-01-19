package glue_gunner4.botd.bot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class ShutdownCommand extends ListenerAdapter {
    public static final SlashCommandData COMMAND = Commands.slash("shutdown", "Shuts down Bot'D, but not the Minecraft server.");

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().equals("shutdown")) {
            Shutdown(event);
        }
    }

    private void Shutdown(ButtonInteractionEvent event) {
        event.reply("Shutting down Bot'D...").complete();
        Botd.jda.shutdown();
    }

    public static void ShutdownBot(SlashCommandInteractionEvent event) {
        Button shutdownButton = Button.danger("shutdown", "Shutdown");

        MessageCreateData message = new MessageCreateBuilder()
                .addContent("Are you sure you want to shutdown the bot?")
                .addActionRow(shutdownButton)
                .build();
        event.reply(message).setEphemeral(true).complete();
    }
}