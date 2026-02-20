package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;

public class AvatarCommand implements Command {

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        User targetUser;

        if (args.length > 1) {
            String userId = args[1].replaceAll("[<@!>]", "");
            targetUser = event.getJDA().getUserById(userId);
            if (targetUser == null) {
                event.getChannel().sendMessage("Utilisateur non trouvé.").queue();
                return;
            }
        } else {
            targetUser = event.getAuthor();
        }

        String avatarUrl = targetUser.getEffectiveAvatarUrl() + "?size=512";

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(targetUser.getName() + "'s Avatar");
        embed.setImage(avatarUrl);
        embed.setColor(Color.CYAN);

        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }

}