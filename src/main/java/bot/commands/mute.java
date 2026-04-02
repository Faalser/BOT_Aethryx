package bot.commands;

import java.util.List;

import bot.command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * Moderation command to mute a user for a specified duration.
 * Prevents the user from sending messages in the server for the specified time.
 * Maximum duration is limited to 28 days as per Discord API limitations.
 * Sends a private message to the muted user informing them of the action.
 * Requires appropriate permissions to execute.
 */
public class mute implements command 
{
    
    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "mute"
     */
    @Override
    public String getName() 
    {
        return "mute";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description in French: "Mute un utilisateur pour une durée donnée (max 28 jours)"
     */
    @Override
    public String getDescription() 
    {
        return "Mute un utilisateur pour une durée donnée (max 28 jours)";
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the required parameters for the mute command.
     * 
     * @return Usage string: "/mute [user] [duration] [reason]"
     */
    @Override
    public String getUsage() 
    {
        return "/mute [user] [duration] [reason]";
    }

    /**
     * Returns the command options/parameters.
     * Requires a target user, duration in seconds, and reason as mandatory parameters.
     * 
     * @return List containing user, duration, and reason option parameters
     */
    @Override 
    public List<OptionData> getOptions() 
    {
        return List.of(
            new OptionData(OptionType.USER, "user", "Utilisateur à mute", true),
            new OptionData(OptionType.INTEGER, "duration", "Durée du mute en secondes", true),
            new OptionData(OptionType.STRING, "reason", "Raison du mute", true)
        );
    }

    /**
     * Executes the mute command.
     * Mutes the specified user for the given duration with the provided reason.
     * Duration is automatically limited to maximum of 28 days (40320 minutes).
     * Sends a private message to the muted user informing them of the mute.
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
                Thread.currentThread().setName("Commande: " + this.getName() + "\nLancer par: " + event.getUser().getName());
                Member member = event.getOption("user").getAsMember();
                int duree = event.getOption("duration").getAsInt();
                String raison = event.getOption("reason") != null ? event.getOption("reason").getAsString() : "Aucune raison spécifiée";
                long dureeFinal = Math.min(duree, 28*24*60);
                member.timeoutFor(java.time.Duration.ofMinutes(dureeFinal)).reason(raison).queue();
                member.getUser().openPrivateChannel().flatMap(channel -> {
                    return channel.sendMessage("Vous avez été mute du serveur pour la raison : " + raison);
                }).queue();
                event.reply("L'utilisateur " + member.getUser() + " a été mute pour " + dureeFinal + " minutes.").setEphemeral(true).queue();
            });
            thread.start();
            return;
        }
        event.reply("Désolé, trop de demandes en cours.").queue();
    }
}
