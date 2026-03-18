package bot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.session.ReadyEvent;

public class SlashListener extends ListenerAdapter 
{

    @Override
    public void onReady(ReadyEvent event)
    {
        System.out.println("SlashListener prêt !");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) 
    {
        System.out.println("Commande reçue : /" + event.getName());
        Bot.COMMANDS.stream()
            .filter(cmd -> cmd.getName().equals(event.getName()))
            .findFirst()
            .ifPresent(cmd -> cmd.execute(event));
    }

}
