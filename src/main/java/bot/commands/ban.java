package bot.commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.entities.Member;

public class ban implements command
{

    @Override
    public String getName() 
    {
        return "ban";
    }

    @Override
    public String getDescription() 
    {
        return "Bannit un membre de manière permanente";
    }

    @Override
    public List<OptionData> getOptions()
    {
        return List.of(
            new OptionData(OptionType.USER, "utilisateur", "L'utilisateur à bannir", true),
            new OptionData(OptionType.STRING, "raison", "La raison du bannissement", true)
        );
    }

    @Override
    public String getUsage()
    {
        return "/ban [utilisateur] [raison]";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
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
    }

}
