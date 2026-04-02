package bot.commands;

import bot.command;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;

/**
 * Command to display a user's avatar image.
 * Allows users to get the avatar URL of themselves or another user.
 * The avatar is displayed in 512x512 resolution for optimal quality.
 */
public class avatar implements command
{
    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "avatar"
     */
    @Override
    public String getName()
    {
        return "avatar";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description: "Displays the avatar of a user"
     */
    @Override
    public String getDescription()
    {
        return "Displays the avatar of a user";
    }

    /**
     * Returns the command options/parameters.
     * Defines an optional user parameter to specify whose avatar to display.
     * If no user is specified, the command user's avatar will be shown.
     * 
     * @return List containing the user option parameter
     */
    @Override
    public List<OptionData> getOptions()
    {
        return List.of(
            new OptionData(OptionType.USER, "user", "The user whose avatar you want to see", false)
        );
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the syntax for using the avatar command.
     * 
     * @return Usage string: "/avatar [user]"
     */
    @Override
    public String getUsage()
    {
        return "/avatar [user]";
    }

    /**
     * Executes the avatar command.
     * Retrieves the specified user's avatar URL and displays it in the Discord channel.
     * Uses thread management to prevent excessive concurrent operations.
     * If no user is specified, defaults to the command executor's avatar.
     * 
     * @param event The slash command interaction event containing user input and context
     */
    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                OptionMapping option = event.getOption("user");
                User user = (option != null) ? option.getAsUser() : event.getUser();
                String avatarurl = user.getEffectiveAvatarUrl() + "?size=512";
                event.reply("Avatar of **" + user.getName() + "** : " + avatarurl).queue();
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();
    }

}
