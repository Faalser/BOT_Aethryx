package bot.commands;

import bot.command;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class userinfo implements command
{

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public String getDescription() {
        return "Infos sur un utilisateur (rôles, date de création, date d'arrivée)";
    }

    @Override
    public List<OptionData> getOptions()
    {
        return List.of(
            new OptionData(OptionType.USER, "utilisateur", "L'utilisateur dont vous voulez les infos", false)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        OptionMapping option = event.getOption("utilisateur");
        User user = (option != null) ? option.getAsUser() : event.getUser();
        Member member = (option != null) ? option.getAsMember() : event.getMember();
        List<Role> roles = member.getRoles();
        String rolesStr = roles.stream()
                .map(role -> role.getName())
                .collect(java.util.stream.Collectors.joining(", "));
        event.reply("User info : " + "\n \t Name : " + user.getName() + "\n \t Rôles : " + rolesStr + "\n \t Créé le : " + user.getTimeCreated().toString() + "\n \t Rejoint le : " + member.getTimeJoined().toString()).queue();
    }

}