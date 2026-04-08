package bot.commands;

import java.util.List;
import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.entities.Member;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.Permission;

/**
 * Administrative command to kick a member from the server temporarily.
 * Requires both a target user and a reason for the kick.
 * Sends a private message to the kicked user with the reason.
 * Unlike ban, the user can rejoin the server after being kicked.
 * Requires appropriate permissions to execute.
 */
public class kick implements command
{

    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "kick"
     */
    @Override
    public String getName() 
    {
        return "kick";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description: "kick a member in a non permanent way"
     */
    @Override
    public String getDescription() 
    {
        return "kick a member in a non permanent way";
    }

    /**
     * Returns the command options/parameters.
     * Requires both a target user and a kick reason as mandatory parameters.
     * 
     * @return List containing user and reason option parameters
     */
    @Override
    public List<OptionData> getOptions()
    {
        return List.of(
            new OptionData(OptionType.USER, "user", "The user to kick", true),
            new OptionData(OptionType.STRING, "reason", "The reason for the kick", true)
        );
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the required parameters for the kick command.
     * 
     * @return Usage string: "/kick [user] [reason]"
     */
    @Override
    public String getUsage() 
    {
        return "/kick [user] [reason]";
    }

    /**
     * Executes the kick command.
     * Kicks the specified user from the server with the provided reason.
     * Sends a private message to the kicked user informing them of the kick.
     * Uses thread management to prevent excessive concurrent operations.
     * Requires both user and reason parameters to be provided.
     * 
     * @param event The slash command interaction event containing command parameters
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                // Set thread name for debugging
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());

                // Check if the command is used in a server
                if (event.getGuild() == null) {
                    event.reply("This command can only be used in a server.").setEphemeral(true).queue();
                    return;
                }

                // Check if the user that use the command have the permission to kick members
                if (!event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                    event.reply("You do not have the permission to kick members.").setEphemeral(true).queue();
                    return;
                }
                
                // Get command parameters
                OptionMapping option = event.getOption("user");
                OptionMapping reasonOption = event.getOption("reason");

                // Check if the user and reason are provided
                if (option == null || reasonOption == null) 
                {
                    event.reply("Please provide a user and a reason.").setEphemeral(true).queue();
                    return;
                }

                // Send private message to the kicked user
                option.getAsUser().openPrivateChannel().flatMap(channel -> {
                    return channel.sendMessage("You have been kicked from the server for the reason: " + reasonOption.getAsString());
                }).queue(); 
                
                // Kick the user
                Member member = option.getAsMember();
                event.getGuild().kick(member).completeAfter(200, TimeUnit.MILLISECONDS);
                event.reply("Kick done!").setEphemeral(true).queue();
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();      
    }

}
