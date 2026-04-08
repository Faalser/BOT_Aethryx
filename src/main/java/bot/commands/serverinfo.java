package bot.commands;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Utility command that displays information about the Discord server.
 * Shows basic server details including name and member count.
 * Can only be used within a server context (not in direct messages).
 * Useful for users to get quick information about the server they are in.
 */
public class serverinfo implements command 
{
    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "serverinfo"
     */
    @Override
    public String getName() 
    {
        return "serverinfo";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description: "Server information (name, members, creation date)"
     */
    @Override
    public String getDescription() 
    {
        return "Server information (name, members, creation date)";
    }

    /**
     * Returns the usage instructions for this command.
     * Since this command takes no parameters, the usage is simple.
     * 
     * @return Usage string: "/serverinfo"
     */
    @Override 
    public String getUsage()
    {
        return "/serverinfo";
    }

    /**
     * Executes the serverinfo command.
     * Displays basic server information including name and member count.
     * Validates that the command is used in a server context.
     * Uses thread management to prevent excessive concurrent operations.
     * 
     * @param event The slash command interaction event containing server context
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                // Set the thread name for debugging
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                
                // Check if the command is used in a server
                if (event.getGuild() == null)
                {
                    event.reply("This command can only be used in a server.").setEphemeral(true).queue();
                    return;
                }
                
                // Reply with server information
                event.reply("Server information: \n \t Name: " + event.getGuild().getName() + "\n \t Members: " + event.getGuild().getMemberCount()).queue();
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();        
    }
}
