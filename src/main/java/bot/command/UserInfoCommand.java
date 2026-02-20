package bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;

public class UserInfoCommand implements Command {
    
    @Override
    public String getName() {
        return "userinfo";
    }
    
    @Override
    public void execute(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Member member = event.getMember();
        Guild guild = event.getGuild();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("User Information");
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.addField("Name", user.getName(), false);
        embedBuilder.addField("ID", user.getId(), false);
        embedBuilder.addField("Is Bot", String.valueOf(user.isBot()), false);
        embedBuilder.addField("Joined Server", member != null ? member.getTimeJoined().toString() : "Unknown", false);
        embedBuilder.addField("Server Name", guild != null ? guild.getName() : "Unknown", false);

        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}