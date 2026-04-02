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
     * @return Description in French: "Affiche l'avatar d'un utilisateur"
     */
    @Override
    public String getDescription()
    {
        return "Affiche l'avatar d'un utilisateur";
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
            new OptionData(OptionType.USER, "utilisateur", "L'utilisateur dont vous voulez l'avatar", false)
        );
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the syntax for using the avatar command.
     * 
     * @return Usage string: "/avatar [utilisateur]"
     */
    @Override
    public String getUsage()
    {
        return "/avatar [utilisateur]";
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
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                OptionMapping option = event.getOption("utilisateur");
                User user = (option != null) ? option.getAsUser() : event.getUser();
                String avatarurl = user.getEffectiveAvatarUrl() + "?size=512";
                event.reply("Avatar de **" + user.getName() + "** : " + avatarurl).queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
    }

}
