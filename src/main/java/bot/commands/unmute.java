package bot.commands;

import bot.command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;

public class unmute implements command
{
    
    @Override
    public String getName() 
    {
        return "unmute";
    }

    @Override
    public String getDescription() 
    {
        return "Unmute un utilisateur";
    }

    @Override
    public String getUsage() 
    {
        return "/unmute <user>";
    }

    @Override
    public List<OptionData> getOptions() 
    {
        return List.of(
            new OptionData(OptionType.USER, "user", "Utilisateur à unmute", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                Member member = event.getOption("user").getAsMember();
                member.removeTimeout().queue();
                event.reply("L'utilisateur " + member.getUser() + " a été unmute.").setEphemeral(true).queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
    }

}
