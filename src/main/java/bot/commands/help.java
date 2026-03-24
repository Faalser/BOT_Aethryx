package bot.commands;

import bot.command;
import bot.Bot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class help implements command
{
    @Override
    public String getName() 
    {
        return "help";
    }

    @Override
    public String getDescription() 
    {
        return "Affiche l'aide des commandes disponibles";
    }

    @Override
    public String getUsage() 
    {
        return "/help";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                EmbedBuilder embed = new EmbedBuilder();
                for (command cmd : Bot.COMMANDS) {
                    embed.addField(cmd.getName(), cmd.getDescription() + " \n" + cmd.getUsage(), false);
                }
                embed.setTitle("Commandes du bot");
                embed.addField("Pour plus d'informations veuillez consulter notre documentation", "https://github.com/Faalser/BOT_Aethryx", false);
                event.replyEmbeds(embed.build()).queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
    }    
}
