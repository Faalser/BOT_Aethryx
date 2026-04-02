package bot.commands;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Utility command that displays information about the Discord server.
 * Shows basic server details including name and member count.
 * Can only be used within a server context (not in direct messages).
 * Useful for users to get quick information about the server they are in.
 */
public class serverinfo implements command 
{
    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "serverinfo"
     */
    @Override
    public String getName() 
    {
        return "serverinfo";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description in French: "Infos sur le serveur (nom, membres, date de création)"
     */
    @Override
    public String getDescription() 
    {
        return "Infos sur le serveur (nom, membres, date de création)";
    }

    /**
     * Returns the usage instructions for this command.
     * Since this command takes no parameters, the usage is simple.
     * 
     * @return Usage string: "/serverinfo"
     */
    @Override 
    public String getUsage()
    {
        return "/serverinfo";
    }

    /**
     * Executes the serverinfo command.
     * Displays basic server information including name and member count.
     * Validates that the command is used in a server context.
     * Uses thread management to prevent excessive concurrent operations.
     * 
     * @param event The slash command interaction event containing server context
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                if (event.getGuild() == null)
                {
                    event.reply("Cette commande ne peut être utilisée que dans un serveur.").setEphemeral(true).queue();
                    return;
                }
                event.reply("Informations sur le serveur : \n \t Nom : " + event.getGuild().getName() + "\n \t Membres : " + event.getGuild().getMemberCount()).queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();        
    }
}
