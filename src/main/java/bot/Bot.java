package bot;

import bot.commands.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import java.util.List;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import java.util.ArrayList;

public class Bot
{
    public static final List<command> COMMANDS = List.of(
        new avatar(),
        new hello(),
        new ping(),
        new serverinfo(),
        new userinfo()
    );
    public static void main(String[] args)
    {
        String token = System.getenv("DISCORD_BOT_TOKEN");
        JDA jda = JDABuilder
            .createDefault(token)
            .enableIntents(
                GatewayIntent.MESSAGE_CONTENT, 
                GatewayIntent.GUILD_MESSAGES, 
                GatewayIntent.GUILD_MEMBERS
            )
            .addEventListeners(new SlashListener())
            .build();
        try 
        {
            jda.awaitReady();
        } catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
        List <SlashCommandData> commands = new ArrayList<>();
        for (command cmd : COMMANDS)
        {
            SlashCommandData data = Commands.slash(cmd.getName(), cmd.getDescription());
            if (!cmd.getOptions().isEmpty())
            {
                data.addOptions(cmd.getOptions());
            }
            commands.add(data);
            System.out.println("Commande ajoutée : /" + cmd.getName());
        }
        jda.updateCommands().addCommands(commands).queue(
            success -> System.out.println("Commandes mises à jour avec succès"),
            error -> System.err.println("Erreur lors de la mise à jour des commandes: " + error.getMessage())
        );
        System.out.println("Bot connecté : " + jda.getSelfUser().getName());
    }

}
