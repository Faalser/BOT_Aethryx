package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Random;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;

public class CoinFlipCommand implements Command {
    
    @Override
    public String getName() {
        return "coinflip";
    }
    
    @Override
    public void execute(MessageReceivedEvent event) {
        boolean isHeads = new Random().nextBoolean();
        String result = isHeads ? "Heads!" : "Tails!";
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Coin Flip Result");
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.addField("Result", result, false);
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}