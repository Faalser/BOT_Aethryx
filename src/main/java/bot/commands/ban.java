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
     * @return Description in French: "Bannit un membre de manière permanente"
     */
    @Override
    public String getDescription() 
    {
        return "Bannit un membre de manière permanente";
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
            new OptionData(OptionType.USER, "utilisateur", "L'utilisateur à bannir", true),
            new OptionData(OptionType.STRING, "raison", "La raison du bannissement", true)
        );
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the required parameters for the ban command.
     * 
     * @return Usage string: "/ban [utilisateur] [raison]"
     */
    @Override
    public String getUsage()
    {
        return "/ban [utilisateur] [raison]";
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
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                OptionMapping option = event.getOption("utilisateur");
                OptionMapping reasonOption = event.getOption("raison");
                if (option == null || reasonOption == null) 
                {
                    event.reply("Veuillez fournir un utilisateur et une raison.").setEphemeral(true).queue();
                    return;
                }
                option.getAsUser().openPrivateChannel().flatMap(channel -> {
                    return channel.sendMessage("Vous avez été banni du serveur pour la raison : " + reasonOption.getAsString());
                }).queue();
                Member member = option.getAsMember();
                event.getGuild().ban(member, 7, java.util.concurrent.TimeUnit.DAYS).completeAfter(200, TimeUnit.MILLISECONDS);
                event.reply("Bannissement effectué !").setEphemeral(true).queue();
            });
            thread.start();
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
        return;
    }

}
