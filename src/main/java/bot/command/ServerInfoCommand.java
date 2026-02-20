package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;

public class ServerInfoCommand implements Command {
    
    @Override
    public String getName() {
        return "serverinfo";
    }
    
    @Override
    public void execute(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Server Information");
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.addField("Name", guild.getName(), false);
        embedBuilder.addField("Members", String.valueOf(guild.getMemberCount()), false);
        embedBuilder.addField("Owner", guild.getOwner().getUser().getName(), false);
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}