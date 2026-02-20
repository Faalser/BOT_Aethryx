package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Random;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;

public class RollCommand implements Command {
    
    @Override
    public String getName() {
        return "roll";
    }
    
    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Rolling the dice...").queue();
        int number = 6; 
        String raw = event.getMessage().getContentRaw();
        if (raw.length() > 5) {
            try {
                number = Integer.parseInt(raw.substring(5).trim());
            } catch (NumberFormatException ignored) {
                event.getChannel().sendMessage("Veuillez entrer un nombre valide !").queue();
                return;
            }
        }
        int result = new Random().nextInt(number) + 1; 
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Roll Result");
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.addField("Roll", "Tu as fait un " + result + "!", false);
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}