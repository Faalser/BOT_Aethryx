package bot.commands;

import bot.command;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

/**
 * Utility command that displays detailed information about a Discord user.
 * Shows user's name, roles, creation date, and server join date.
 * Can display information about the command user or a specified target user.
 * Useful for getting quick user information for moderation or general purposes.
 */
public class userinfo implements command
{

    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "userinfo"
     */
    @Override
    public String getName() {
        return "userinfo";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description in French: "Infos sur un utilisateur (rôles, date de création, date d'arrivée)"
     */
    @Override
    public String getDescription() {
        return "Infos sur un utilisateur (rôles, date de création, date d'arrivée)";
    }

    /**
     * Returns the command options/parameters.
     * Defines an optional user parameter to specify whose information to display.
     * If no user is specified, the command user's information will be shown.
     * 
     * @return List containing the user option parameter
     */
    @Override
    public List<OptionData> getOptions()
    {
        return List.of(
            new OptionData(OptionType.USER, "utilisateur", "L'utilisateur dont vous voulez les infos", false)
        );
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the syntax for using the userinfo command.
     * 
     * @return Usage string: "/userinfo [utilisateur]"
     */
    @Override
    public String getUsage()
    {
        return "/userinfo [utilisateur]";
    }

    /**
     * Executes the userinfo command.
     * Displays detailed information about the specified user including name, roles, and dates.
     * If no user is specified, defaults to the command executor's information.
     * Shows user's creation date, server join date, and list of roles.
     * Uses thread management to prevent excessive concurrent operations.
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
                Member member = (option != null) ? option.getAsMember() : event.getMember();
                List<Role> roles = member.getRoles();
                String rolesStr = roles.stream()
                        .map(role -> role.getName())
                        .collect(java.util.stream.Collectors.joining(", "));
                event.reply("User info : " + "\n \t Name : " + user.getName() + "\n \t Rôles : " + rolesStr + "\n \t Créé le : " + user.getTimeCreated().toString() + "\n \t Rejoint le : " + member.getTimeJoined().toString()).queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();        
    }

}