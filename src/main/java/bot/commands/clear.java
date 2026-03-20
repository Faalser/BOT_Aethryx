package bot.commands;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;
import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.Message;

public class clear implements command 
{
    
    @Override 
    public String getName()
    {
        return "clear";
    }

    @Override 
    public String getDescription()
    {
        return "Supprime les messages";
    }

    @Override
    public List<OptionData> getOptions()
    {
        return List.of(
            new OptionData(OptionType.INTEGER, "nombre", "Le nombre de messages à supprimer", true)
        );
    }

    @Override 
    public String getUsage()
    {
        return "/clear [nombre de messages]";
    }

    @Override 
    public void execute(SlashCommandInteractionEvent event)
    {
        int nombre = event.getOption("nombre").getAsInt();
        event.getChannel().getHistory().retrievePast(nombre).queue(messages -> {
            List<Message> recents = messages.stream()
            .filter(m -> m.getTimeCreated().isAfter(OffsetDateTime.now().minusDays(14)))
            .collect(Collectors.toList());
            List<Message> vieux = messages.stream()
            .filter(m -> m.getTimeCreated().isBefore(OffsetDateTime.now().minusDays(14)))
            .collect(Collectors.toList());
            if (!recents.isEmpty()) 
            {
                event.getChannel().asTextChannel().deleteMessages(recents).queue();
            }
            if (!vieux.isEmpty()) 
            {
                for (int i = 0; i < vieux.size(); i++) {
                    vieux.get(i).delete().queueAfter(i * 1100, TimeUnit.MILLISECONDS);
                }
            }
            event.reply("Clear effectué !").setEphemeral(true).queue();
        });
    }

}
