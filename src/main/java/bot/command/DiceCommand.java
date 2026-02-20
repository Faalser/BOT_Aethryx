package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Random;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;

public class DiceCommand implements Command {
    
    @Override
    public String getName() {
        return "dice";
    }
    
    @Override
    public void execute(MessageReceivedEvent event) {
        int dice = 6; 
        String raw = event.getMessage().getContentRaw();
        if (raw.length() > 5) {
            try {
                dice = Integer.parseInt(raw.substring(5).trim());
            } catch (NumberFormatException ignored) {
                event.getChannel().sendMessage("Veuillez entrer un nombre valide !").queue();
                return;
            }
        }
        int roll = new Random().nextInt(dice) + 1;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Dice Roll Result");
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.addField("Roll", "Tu as fait un " + roll + "!", false);
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}