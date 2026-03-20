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
        Thread helpThread = new Thread(() -> {
            EmbedBuilder embed = new EmbedBuilder();
            for (command cmd : Bot.COMMANDS) {
                embed.addField(cmd.getName(), cmd.getDescription() + " \n" + cmd.getUsage(), false);
            }
            embed.setTitle("Commandes du bot");
            event.replyEmbeds(embed.build()).queue();
        });
        helpThread.start();
    }
    
}
