package bot.commands;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ping implements command 
{
    @Override
    public String getName()
    {
        return "ping";
    }

    @Override 
    public String getDescription()
    {
        return "Latence du bot";
    }

    @Override
    public String getUsage()
    {
        return "/ping";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        long latency = event.getJDA().getGatewayPing();
        event.reply("Pong ! Latence : **" + latency + "ms**").queue();
    }
}
