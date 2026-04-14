package bot;

import bot.commands.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import java.util.List;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Main class for the Discord bot application.
 * This class handles bot initialization, command registration, and startup procedures.
 * It reads the bot token from environment file and configures the JDA instance.
 */
public class Bot
{

    /**
     * List of all available commands for the bot.
     * This list contains instances of all command implementations that will be registered with Discord.
     */
    public static final List<command> COMMANDS = List.of(
        new avatar(),
        new ban(),
        new clear(),
        new hello(),
        new help(),
        new kick(),
        new musicDownload(),
        new mute(),
        new ping(),
        new play(),
        new serverinfo(),
        new threads(),
        new unmute(),
        new userinfo()
    );

    /**
     * Main entry point for the Discord bot application.
     * Initializes the bot, loads configuration, registers commands, and starts the JDA instance.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args)
    {
        String token = null;
        try 
        {
            // Read Discord bot token from .env file
            token = Files.lines(Paths.get(System.getProperty("user.dir") + "/.env"))
                .filter(line -> line.startsWith("DISCORD_BOT_TOKEN="))
                .map(line -> line.split("=", 2)[1])
                .findFirst()
                .orElse(null);
        }
        catch (IOException e) 
        {
            System.err.println("Error while reading the file .env: " + e.getMessage());
            return;
        }
        
        // Create and configure JDA instance with required intents and event listeners
        JDA jda = JDABuilder
            .createDefault(token)
            .enableIntents(
                GatewayIntent.MESSAGE_CONTENT, 
                GatewayIntent.GUILD_MESSAGES, 
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_VOICE_STATES
            )
            .enableCache(CacheFlag.VOICE_STATE)
            .addEventListeners(new slashListener())
            .build();
            
        try 
        {
            // Wait for JDA to be fully loaded
            jda.awaitReady();
        } catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
        
        // Register all slash commands with Discord
        List <SlashCommandData> commands = new ArrayList<>();
        for (command cmd : COMMANDS)
        {
            SlashCommandData data = Commands.slash(cmd.getName(), cmd.getDescription());
            if (!cmd.getOptions().isEmpty())
            {
                data.addOptions(cmd.getOptions());
            }
            commands.add(data);
            System.out.println("Commands registered : /" + cmd.getName());
        }
        
        // Update commands on Discord servers
        jda.updateCommands().addCommands(commands).queue(
            success -> System.out.println("Commands updated successfully"),
            error -> System.err.println("Error while updating commands: " + error.getMessage())
        );
        
        System.out.println("Bot connected : " + jda.getSelfUser().getName());
    }

}
