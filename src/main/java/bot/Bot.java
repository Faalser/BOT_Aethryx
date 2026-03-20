package bot;

import bot.commands.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import java.util.List;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Bot
{
    public static final List<command> COMMANDS = List.of(
        new avatar(),
        new ban(),
        new clear(),
        new hello(),
        new help(),
        new kick(),
        new ping(),
        new serverinfo(),
        new userinfo()
    );
    public static void main(String[] args)
    {
        String token = null;
        try 
        {
            token = Files.lines(Paths.get("/home/faalser/PROJECT/BOT_Aethryx/.env"))
                .filter(line -> line.startsWith("DISCORD_BOT_TOKEN="))
                .map(line -> line.split("=", 2)[1])
                .findFirst()
                .orElse(null);
        }
        catch (IOException e) 
        {
            System.err.println("Erreur lors de la lecture du fichier .env" + e.getMessage());
            return;
        }
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
