package bot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;
import java.lang.Math;

public interface command 
{

    String getName();
    String getDescription();
    String getUsage();
    default List<OptionData> getOptions() {
        return List.of();
    }

    default int nombreDeThreads()
    {
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parentGroup;
        while ((parentGroup = rootGroup.getParent()) != null)
        {
            rootGroup = parentGroup;
        }
        int count = rootGroup.activeCount();
        return count;
    }

    default int nombreDeThreadsMax()
    {
        return (int) Math.pow(Runtime.getRuntime().availableProcessors(), 2) * 10;
    }

    default void execute(SlashCommandInteractionEvent event)
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                event.reply("Commande non définie pour l'instant.").queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
    }

}
