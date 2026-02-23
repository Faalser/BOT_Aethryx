package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Random;

public class CoinFlipCommand implements Command {
    
    @Override
    public String getName() {
        return "coinflip";
    }
    
    @Override
    public void execute(MessageReceivedEvent event) {
        boolean isHeads = new Random().nextBoolean();
        String result = isHeads ? "Heads!" : "Tails!";
        event.getChannel().sendMessage(result).queue();
    }
}