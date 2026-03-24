package bot.commands;

import bot.command;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;

public class avatar implements command
{
    @Override
    public String getName()
    {
        return "avatar";
    }

    @Override
    public String getDescription()
    {
        return "Affiche l'avatar d'un utilisateur";
    }

    @Override
    public List<OptionData> getOptions()
    {
        return List.of(
            new OptionData(OptionType.USER, "utilisateur", "L'utilisateur dont vous voulez l'avatar", false)
        );
    }

    @Override
    public String getUsage()
    {
        return "/avatar <utilisateur>";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event)
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                OptionMapping option = event.getOption("utilisateur");
                User user = (option != null) ? option.getAsUser() : event.getUser();
                String avatarurl = user.getEffectiveAvatarUrl() + "?size=512";
                event.reply("Avatar de **" + user.getName() + "** : " + avatarurl).queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
    }

}
