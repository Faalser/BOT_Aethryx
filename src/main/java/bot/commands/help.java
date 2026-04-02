package bot.commands;

import bot.command;
import bot.Bot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.EmbedBuilder;

/**
 * Utility command that displays help information for all available bot commands.
 * Creates an embedded message showing command names, descriptions, and usage syntax.
 * Provides a link to the bot's documentation for more detailed information.
 * Useful for users to discover and learn how to use bot commands.
 */
public class help implements command
{
    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "help"
     */
    @Override
    public String getName() 
    {
        return "help";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description in French: "Affiche l'aide des commandes disponibles"
     */
    @Override
    public String getDescription() 
    {
        return "Affiche l'aide des commandes disponibles";
    }

    /**
     * Returns the usage instructions for this command.
     * Since this command takes no parameters, the usage is simple.
     * 
     * @return Usage string: "/help"
     */
    @Override
    public String getUsage() 
    {
        return "/help";
    }

    /**
     * Executes the help command.
     * Creates an embedded message containing all available commands with their descriptions and usage.
     * Iterates through all registered commands in Bot.COMMANDS list.
     * Includes a link to the bot's GitHub documentation.
     * Uses thread management to prevent excessive concurrent operations.
     * 
     * @param event The slash command interaction event containing user information
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                EmbedBuilder embed = new EmbedBuilder();
                for (command cmd : Bot.COMMANDS) {
                    embed.addField(cmd.getName(), cmd.getDescription() + " \n" + cmd.getUsage(), false);
                }
                embed.setTitle("Commandes du bot");
                embed.addField("Pour plus d'informations veuillez consulter notre documentation", "https://github.com/Faalser/BOT_Aethryx", false);
                event.replyEmbeds(embed.build()).queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
    }    
}
