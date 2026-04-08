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
     * @return Description: "Display the help of all commands"
     */
    @Override
    public String getDescription() 
    {
        return "Display the help of all commands";
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
                // Set thread name for debugging
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                
                // Create the help embed
                EmbedBuilder embed = new EmbedBuilder();
                for (command cmd : Bot.COMMANDS) {
                    embed.addField(cmd.getName(), cmd.getDescription() + " \n" + cmd.getUsage(), false);
                }
                embed.setTitle("Commands of the bot");
                embed.addField("If you have any questions, please contact us on our Discord", "https://discord.gg/tkHVfSbMWp", false);
                embed.addField("For more information please consult our documentation on our GitHub", "https://github.com/Faalser/BOT_Aethryx", false);
                embed.addField("Or if you have access to the bot's source code, you can find the documentation in the docs folder", "https://github.com/Faalser/BOT_Aethryx/tree/main/doc/apidocs", false);
                
                // Send the help embed
                event.replyEmbeds(embed.build()).queue();
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();
    }    
}
