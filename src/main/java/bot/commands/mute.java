package bot.commands;

import bot.command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;

public class mute implements command {
    
    @Override
    public String getName() 
    {
        return "mute";
    }

    @Override
    public String getDescription() 
    {
        return "Mute un utilisateur pour une durée donnée (max 28 jours), ce mute est un peu spécial (pas seulement le vocal)";
    }

    @Override
    public String getUsage() 
    {
        return "/mute <user>";
    }

    @Override 
    public List<OptionData> getOptions() 
    {
        return List.of(
            new OptionData(OptionType.USER, "user", "Utilisateur à mute", true),
            new OptionData(OptionType.INTEGER, "duration", "Durée du mute en secondes", true),
            new OptionData(OptionType.STRING, "reason", "Raison du mute", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        Member member = event.getOption("user").getAsMember();
        int duree = event.getOption("duration").getAsInt();
        String raison = event.getOption("reason") != null ? event.getOption("reason").getAsString() : "Aucune raison spécifiée";
        long dureeFinal = Math.min(duree, 28*24*60);
        member.timeoutFor(java.time.Duration.ofMinutes(dureeFinal)).reason(raison).queue();
        member.getUser().openPrivateChannel().flatMap(channel -> {
            return channel.sendMessage("Vous avez été mute du serveur pour la raison : " + raison);
        }).queue();
        event.reply("L'utilisateur " + member.getUser() + " a été mute pour " + dureeFinal + " minutes.").setEphemeral(true).queue();
    }
}
