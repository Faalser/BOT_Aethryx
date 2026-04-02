package bot.commands;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Simple greeting command that responds with a personalized hello message.
 * This is a basic command that demonstrates the command structure and thread management.
 * No parameters are required - it simply greets the user who executed the command.
 */
public class hello implements command
{
    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "hello"
     */
    @Override
    public String getName()
    {
        return "hello";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description in French: "Le bot te salue par ton nom"
     */
    @Override
    public String getDescription()
    {
        return "Le bot te salue par ton nom";
    }

    /**
     * Returns the usage instructions for this command.
     * Since this command takes no parameters, the usage is simple.
     * 
     * @return Usage string: "/hello"
     */
    @Override 
    public String getUsage()
    {
        return "/hello";
    }

    /**
     * Executes the hello command.
     * Sends a personalized greeting message to the user who executed the command.
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
                event.reply("Hello " + event.getUser().getName() + " !").queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
    }
}
