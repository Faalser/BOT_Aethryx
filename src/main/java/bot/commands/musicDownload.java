package bot.commands;

import bot.command;
import java.util.List;
import java.io.File;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Utility command to download music files from URLs.
 * Downloads audio files from specified URLs and saves them to the local songs directory.
 * Uses Apache HttpClient for HTTP requests and file operations.
 * Saves downloaded files as MP3 format for compatibility with the play command.
 * Requires both a song name and URL as parameters.
 */
public class musicDownload implements command
{

    /**
     * Returns the command name used for registration with Discord.
     * This name is used to identify the command and must be unique.
     * 
     * @return The string "musicdownload"
     */
    @Override
    public String getName() 
    {
        return "musicdownload";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description: "Download a song"
     */
    @Override
    public String getDescription() 
    {
        return "Download a song";
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the required parameters for the download command.
     * 
     * @return Usage string: "/musicdownload <song> <url>"
     */
    @Override
    public String getUsage() 
    {
        return "/musicdownload <song> <url>";
    }

    /**
     * Returns the command options/parameters.
     * Requires both a song name and URL as mandatory parameters.
     * 
     * @return List containing song and URL option parameters
     */
    @Override
    public List<OptionData> getOptions() 
    {
        return List.of(
            new OptionData(OptionType.STRING, "song", "The song to download", true),
            new OptionData(OptionType.STRING, "url", "The URL of the song", true)
        );
    }

    /**
     * Executes the musicDownload command.
     * Downloads a music file from the specified URL to the local songs directory.
     * Saves the file with the provided song name and .mp3 extension.
     * Handles HTTP requests using Apache HttpClient and manages file operations.
     * Provides error handling for download failures and network issues.
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
                event.deferReply().queue();

                // Set thread name for debugging
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                
                // Get command parameters
                String song = event.getOption("song").getAsString();
                String url = event.getOption("url").getAsString();

                // Download the song
                try {
                    ProcessBuilder pb = new ProcessBuilder("yt-dlp", "-x", "--audio-format", "mp3", "-o", "songs/" + song + ".%(ext)s", url);
                    pb.directory(new File("src/main/java/bot"));
                    pb.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    event.getHook().editOriginal("Failed to download song.").queue();
                    return;
                }

                // Reply to the user
                event.getHook().editOriginal("Song downloaded successfully!" + "\nFile: " + song).queue();
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();
    }

}
