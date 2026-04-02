package bot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;
import java.lang.Math;

/**
 * Interface defining the contract for all bot commands.
 * Every command implementation must implement this interface to be recognized by the bot.
 * Provides default implementations for thread management and basic command execution.
 */
public interface command 
{

    /**
     * Returns the name of the command.
     * This name is used to register the command with Discord.
     * 
     * @return The command name as a string
     */
    String getName();

    /**
     * Returns the description of the command.
     * This description is shown to users in the Discord command interface.
     * 
     * @return The command description as a string
     */
    String getDescription();

    /**
     * Returns the usage instructions for the command.
     * This should show how to use the command with its parameters.
     * 
     * @return The usage string showing command syntax
     */
    String getUsage();

    /**
     * Returns the list of options/parameters for this command.
     * Default implementation returns an empty list for commands with no parameters.
     * 
     * @return List of OptionData objects defining command parameters
     */
    default List<OptionData> getOptions() {
        return List.of();
    }

    /**
     * Counts the current number of active threads in the application.
     * Used for thread management to prevent excessive concurrent operations.
     * 
     * @return The number of currently active threads
     */
    default int nombreDeThreads()
    {
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parentGroup;
        while ((parentGroup = rootGroup.getParent()) != null)
        {
            rootGroup = parentGroup;
        }
        int count = rootGroup.activeCount();
        return count;
    }

    /**
     * Calculates the maximum number of threads allowed for the application.
     * Based on available processors with a multiplier for optimal performance.
     * 
     * @return The maximum allowed number of threads
     */
    default int nombreDeThreadsMax()
    {
        return (int) Math.pow(Runtime.getRuntime().availableProcessors(), 2) * 10;
    }

    /**
     * Executes the command when triggered by a user interaction.
     * Default implementation provides thread management and basic error handling.
     * Command implementations should override this method for specific functionality.
     * 
     * @param event The slash command interaction event containing user input and context
     */
    default void execute(SlashCommandInteractionEvent event)
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                event.reply("Command not defined yet.").queue();
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();
    }

}
