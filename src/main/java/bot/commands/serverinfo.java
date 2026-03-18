package bot.commands;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class serverinfo implements command 
{
    @Override
    public String getName() 
    {
        return "serverinfo";
    }

    @Override
    public String getDescription() 
    {
        return "Infos sur le serveur (nom, membres, date de création)";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        if (event.getGuild() == null)
        {
            event.reply("Cette commande ne peut être utilisée que dans un serveur.").setEphemeral(true).queue();
            return;
        }
        event.reply("Informations sur le serveur : \n \t Nom : " + event.getGuild().getName() + "\n \t Membres : " + event.getGuild().getMemberCount()).queue();
    }
}
