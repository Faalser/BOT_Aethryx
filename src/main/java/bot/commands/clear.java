package bot.commands;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;
import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.Message;
import java.lang.Thread;
import net.dv8tion.jda.api.Permission;

/**
 * Moderation command to clear messages from a channel.
 * Can delete both recent messages (within 14 days) and older messages.
 * Recent messages are deleted in bulk for efficiency, older messages individually.
 * Requires appropriate permissions to execute.
 */
public class clear implements command 
{
    
    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "clear"
     */
    @Override 
    public String getName()
    {
        return "clear";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description: "Supprime les messages"
     */
    @Override 
    public String getDescription()
    {
        return "Delete messages";
    }

    /**
     * Returns the command options/parameters.
     * Requires the number of messages to delete as a mandatory parameter.
     * 
     * @return List containing the number option parameter
     */
    @Override
    public List<OptionData> getOptions()
    {
        return List.of(
            new OptionData(OptionType.INTEGER, "number", "The number of messages to delete", true)
        );
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the required parameter for the clear command.
     * 
     * @return Usage string: "/clear [number of messages]"
     */
    @Override 
    public String getUsage()
    {
        return "/clear [number of messages]";
    }

    /**
     * Executes the clear command.
     * Deletes the specified number of messages from the channel.
     * Handles both recent messages (within 14 days) and older messages differently.
     * Recent messages are deleted in bulk, older messages individually with delays.
     * Uses thread management to prevent excessive concurrent operations.
     * 
     * @param event The slash command interaction event containing command parameters
     */
    @Override 
    public void execute(SlashCommandInteractionEvent event)
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                // Set thread name for debugging
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                
                // Check if the user that use the command have the permission to manage messages
                if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                    event.reply("You do not have the permission to manage messages.").setEphemeral(true).queue();
                    return;
                }

                // Get command parameters
                int number = event.getOption("number").getAsInt();
                
                // Get messages from history
                event.getChannel().getHistory().retrievePast(number).queue(messages -> {
                    List<Message> recents = messages.stream()
                    .filter(m -> m.getTimeCreated().isAfter(OffsetDateTime.now().minusDays(14)))
                    .collect(Collectors.toList());
                    List<Message> vieux = messages.stream()
                    .filter(m -> m.getTimeCreated().isBefore(OffsetDateTime.now().minusDays(14)))
                    .collect(Collectors.toList());
                    if (!recents.isEmpty()) 
                    {
                        event.getChannel().asTextChannel().deleteMessages(recents).queue();
                    }
                    if (!vieux.isEmpty()) 
                    {
                        for (int i = 0; i < vieux.size(); i++) {
                            vieux.get(i).delete().queueAfter(i * 1100, TimeUnit.MILLISECONDS);
                        }
                    }
                    
                    // Reply to the command user
                    event.reply("Clear done!").setEphemeral(true).queue();
                });
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();
    }

}
