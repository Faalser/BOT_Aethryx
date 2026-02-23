package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ShutdownCommand implements Command {
    private static final String OWNER_ID = "539483237790121985"; // ton ID Discord

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(OWNER_ID)) {
            event.getChannel().sendMessage("Shutting down the bot...").queue();
            event.getJDA().shutdown();
        } else {
            event.getChannel().sendMessage("Vous n'avez pas la permission d'exécuter cette commande !").queue();
        }
    }
}