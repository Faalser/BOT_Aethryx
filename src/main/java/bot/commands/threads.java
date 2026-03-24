package bot.commands;

import java.util.Map;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class threads implements command
{

    @Override
    public String getName()
    {
        return "threads";
    }

    @Override
    public String getDescription()
    {
        return "Liste les threads du serveur";
    }

    @Override
    public String getUsage()
    {
        return "/threads";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
                System.out.println("Nombre de threads : " + threads.size());
                threads.keySet().forEach(t -> 
                    System.out.println(t.getName() + " - " + t.getState())
                );
                event.reply("Threads en cours : " + this.nombreDeThreads() + "\nNombre de Threads possible : " + this.nombreDeThreadsMax()).queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
    }

}
