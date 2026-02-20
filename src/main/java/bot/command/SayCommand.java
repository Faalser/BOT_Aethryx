package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;

public class SayCommand implements Command {

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        String raw = event.getMessage().getContentRaw();

        if (raw.length() <= 5) {
            event.getChannel().sendMessage("Vous devez écrire quelque chose !").queue();
            return;
        }

        String message = raw.substring(5);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Message");
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.addField("Content", message, false);
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}