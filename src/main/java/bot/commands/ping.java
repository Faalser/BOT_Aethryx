package bot.commands;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Utility command to measure the bot's latency to Discord servers.
 * Provides a simple way to check the bot's response time and connection quality.
 * Useful for troubleshooting and performance monitoring.
 */
public class ping implements command 
{
    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "ping"
     */
    @Override
    public String getName()
    {
        return "ping";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description in French: "Latence du bot"
     */
    @Override 
    public String getDescription()
    {
        return "Latence du bot";
    }

    /**
     * Returns the usage instructions for this command.
     * Since this command takes no parameters, the usage is simple.
     * 
     * @return Usage string: "/ping"
     */
    @Override
    public String getUsage()
    {
        return "/ping";
    }

    /**
     * Executes the ping command.
     * Measures the bot's gateway latency and responds with the time in milliseconds.
     * Uses thread management to prevent excessive concurrent operations.
     * 
     * @param event The slash command interaction event containing user information
     */
    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                long latency = event.getJDA().getGatewayPing();
                event.reply("Pong ! Latence : **" + latency + "ms**").queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
    }
}
