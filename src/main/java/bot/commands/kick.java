package bot.commands;

import java.util.List;
import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.entities.Member;
import java.util.concurrent.TimeUnit;

public class kick implements command
{

    @Override
    public String getName() 
    {
        return "kick";
    }

    @Override
    public String getDescription() 
    {
        return "Vire un membre de manière non permanente";
    }

    @Override
    public List<OptionData> getOptions()
    {
        return List.of(
            new OptionData(OptionType.USER, "utilisateur", "L'utilisateur à kicker", true),
            new OptionData(OptionType.STRING, "raison", "La raison du kick", true)
        );
    }

    @Override
    public String getUsage() 
    {
        return "/kick <utilisateur> <raison>";
    }

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
                    return channel.sendMessage("Vous avez été kické du serveur pour la raison : " + reasonOption.getAsString());
                }).queue(); 
                Member member = option.getAsMember();
                event.getGuild().kick(member).completeAfter(200, TimeUnit.MILLISECONDS);
                event.reply("Kick effectué !").setEphemeral(true).queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();      
    }

}
