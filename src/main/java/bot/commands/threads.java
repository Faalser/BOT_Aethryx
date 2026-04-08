package bot.commands;

import java.util.Map;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Administrative command to display information about active threads in the application.
 * Shows the current number of active threads and the maximum allowed threads.
 * Logs detailed thread information to the console for debugging purposes.
 * Useful for monitoring bot performance and thread usage.
 * Requires appropriate permissions to execute.
 */
public class threads implements command
{

    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "threads"
     */
    @Override
    public String getName()
    {
        return "threads";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description: "List threads"
     */
    @Override
    public String getDescription()
    {
        return "List threads";
    }

    /**
     * Returns the usage instructions for this command.
     * Since this command takes no parameters, the usage is simple.
     * 
     * @return Usage string: "/threads"
     */
    @Override
    public String getUsage()
    {
        return "/threads";
    }

    /**
     * Executes the threads command.
     * Displays information about active threads in the application.
     * Logs detailed thread information to the console for debugging.
     * Shows current thread count and maximum allowed threads.
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
                // Set the thread name for debugging
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                
                // Get all threads and log their information
                Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
                System.out.println("Number of threads: " + threads.size());
                threads.keySet().forEach(t -> 
                    System.out.println(t.getName() + " - " + t.getState())
                );

                // Reply with thread information
                event.reply("Threads in progress: " + this.nombreDeThreads() + "\nNumber of Threads possible: " + this.nombreDeThreadsMax()).queue();
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();
    }

}
