package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Random;

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
        event.getChannel().sendMessage("Vous avez lancé un dé à " + number + " faces et obtenu : " + result).queue();
    }
}