package bot.commands;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class hello implements command
{
    @Override
    public String getName()
    {
        return "hello";
    }

    @Override
    public String getDescription()
    {
        return "Le bot te salue par ton nom";
    }

    @Override 
    public String getUsage()
    {
        return "/hello";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        event.reply("Hello " + event.getUser().getName() + " !").queue();
    }
}
