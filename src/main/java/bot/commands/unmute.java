package bot.commands;

import bot.command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;

/**
 * Moderation command to unmute a user who was previously muted.
 * Removes the timeout restriction from the specified user.
 * Allows the user to send messages in the server again.
 * Requires appropriate permissions to execute.
 */
public class unmute implements command
{
    
    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "unmute"
     */
    @Override
    public String getName() 
    {
        return "unmute";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description: "Unmute a user"
     */
    @Override
    public String getDescription() 
    {
        return "Unmute a user";
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the required parameter for the unmute command.
     * 
     * @return Usage string: "/unmute [user]"
     */
    @Override
    public String getUsage() 
    {
        return "/unmute [user]";
    }

    /**
     * Returns the command options/parameters.
     * Requires a target user as a mandatory parameter.
     * 
     * @return List containing the user option parameter
     */
    @Override
    public List<OptionData> getOptions() 
    {
        return List.of(
            new OptionData(OptionType.USER, "user", "User to unmute", true)
        );
    }

    /**
     * Executes the unmute command.
     * Removes the timeout restriction from the specified user.
     * Allows the user to send messages in the server again.
     * Uses thread management to prevent excessive concurrent operations.
     * 
     * @param event The slash command interaction event containing command parameters
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                Member member = event.getOption("user").getAsMember();
                member.removeTimeout().queue();
                event.reply("User " + member.getUser() + " has been unmuted.").setEphemeral(true).queue();
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();
    }

}
