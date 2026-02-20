package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;

public class ShutdownCommand implements Command {
    private static final String OWNER_ID = "539483237790121985"; // ton ID Discord

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(OWNER_ID)) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Shutting Down");
            embedBuilder.setColor(Color.BLUE);
            embedBuilder.addField("Status", "Bot en train de s'arrêter...", false);
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            event.getJDA().shutdown();
        } else {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Permission Denied");
            embedBuilder.setColor(Color.RED);
            embedBuilder.addField("Error", "Tu n'as pas la permission !", false);
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}