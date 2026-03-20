package bot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;

public interface command 
{

    String getName();
    String getDescription();
    String getUsage();
    default List<OptionData> getOptions() {
        return List.of();
    }

    void execute(SlashCommandInteractionEvent event);

}
