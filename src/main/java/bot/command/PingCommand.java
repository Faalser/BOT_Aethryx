package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;

public class PingCommand implements Command {
    
    @Override
    public String getName() {
        return "ping";
    }
    
    @Override
    public void execute(MessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Ping");
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.addField("Pong", "Pong !", false);
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}