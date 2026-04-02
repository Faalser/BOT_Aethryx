package bot.commands;

import java.util.List;
import java.util.concurrent.TimeUnit;
import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.entities.Member;
import java.lang.Thread;

/**
 * Administrative command to permanently ban a member from the server.
 * Requires both a target user and a reason for the ban.
 * Sends a private message to the banned user with the reason.
 * Requires appropriate permissions to execute.
 */
public class ban implements command
{

    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "ban"
     */
    @Override
    public String getName() 
    {
        return "ban";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description: "Ban a member in a permanent way"
     */
    @Override
    public String getDescription() 
    {
        return "Ban a member in a permanent way";
    }

    /**
     * Returns the command options/parameters.
     * Requires both a target user and a ban reason as mandatory parameters.
     * 
     * @return List containing user and reason option parameters
     */
    @Override
    public List<OptionData> getOptions()
    {
        return List.of(
            new OptionData(OptionType.USER, "user", "The user to ban", true),
            new OptionData(OptionType.STRING, "reason", "The reason of ban", true)
        );
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the required parameters for the ban command.
     * 
     * @return Usage string: "/ban [user] [reason]"
     */
    @Override
    public String getUsage()
    {
        return "/ban [user] [reason]";
    }

    /**
     * Executes the ban command.
     * Bans the specified user from the server with the provided reason.
     * Sends a private message to the banned user informing them of the ban.
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
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                OptionMapping option = event.getOption("user");
                OptionMapping reasonOption = event.getOption("reason");
                if (option == null || reasonOption == null) 
                {
                    event.reply("Please provide a user and a reason.").setEphemeral(true).queue();
                    return;
                }
                option.getAsUser().openPrivateChannel().flatMap(channel -> {
                    return channel.sendMessage("You have been banned from the server for the reason: " + reasonOption.getAsString());
                }).queue();
                Member member = option.getAsMember();
                event.getGuild().ban(member, 7, java.util.concurrent.TimeUnit.DAYS).completeAfter(200, TimeUnit.MILLISECONDS);
                event.reply("Banishment completed!").setEphemeral(true).queue();
            });
            thread.start();
        }
        event.reply("Sorry, too many requests in progress.").queue();
        return;
    }

}
