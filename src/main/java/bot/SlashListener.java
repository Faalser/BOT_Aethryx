package bot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.session.ReadyEvent;

/**
 * Event listener for Discord slash commands and bot readiness events.
 * Extends ListenerAdapter to handle Discord API events.
 * Routes slash command interactions to the appropriate command implementations.
 */
public class SlashListener extends ListenerAdapter 
{

    /**
     * Called when the bot is successfully connected and ready to receive events.
     * Logs a confirmation message to the console.
     * 
     * @param event The ready event from Discord API
     */
    @Override
    public void onReady(ReadyEvent event)
    {
        System.out.println("SlashListener ready!");
    }

    /**
     * Called when a slash command interaction is received from Discord.
     * Finds the matching command implementation and executes it.
     * Logs the received command for debugging purposes.
     * 
     * @param event The slash command interaction event containing command details
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) 
    {
        System.out.println("Command received: /" + event.getName());
        Bot.COMMANDS.stream()
            .filter(cmd -> cmd.getName().equals(event.getName()))
            .findFirst()
            .ifPresent(cmd -> cmd.execute(event));
    }

}
