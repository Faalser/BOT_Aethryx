package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
        event.getChannel().sendMessage(message).queue();
    }
}