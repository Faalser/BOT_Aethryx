package bot.commands;

import bot.command;
import net.dv8tion.jda.api.Permission;
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
                // Set the thread name for debugging
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                
                // Get command parameter
                Member member = event.getOption("user").getAsMember();

                // Check if the command is used in a server
                if (event.getGuild() == null) {
                    event.reply("This command can only be used in a server.").setEphemeral(true).queue();
                    return;
                }

                // Check if the user that use the command have the permission to unmute members
                if (!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
                    event.reply("You do not have the permission to unmute members.").setEphemeral(true).queue();
                    return;
                }

                // Send private message to the muted user
                member.getUser().openPrivateChannel().flatMap(channel -> {
                    return channel.sendMessage("You have been unmuted from the server.");
                }).queue();

                // Unmute the user
                member.removeTimeout().queue();

                // Reply to the command user
                event.reply("User " + member.getUser() + " has been unmuted.").setEphemeral(true).queue();
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();
    }

}
