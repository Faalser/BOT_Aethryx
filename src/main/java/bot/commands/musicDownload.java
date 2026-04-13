package bot.commands;

import bot.command;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class musicDownload implements command
{

    @Override
    public String getName() 
    {
        return "musicdownload";
    }

    @Override
    public String getDescription() 
    {
        return "Download a song";
    }

    @Override
    public String getUsage() 
    {
        return "/musicdownload <song> <url>";
    }

    @Override
    public List<OptionData> getOptions() 
    {
        return List.of(
            new OptionData(OptionType.STRING, "song", "The song to download", true),
            new OptionData(OptionType.STRING, "url", "The URL of the song", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) 
    {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                // Set thread name for debugging
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                
                // Get command parameters
                String song = event.getOption("song").getAsString();
                String url = event.getOption("url").getAsString();
                
                // Download the song
                try (CloseableHttpClient client = HttpClients.createDefault()) {
                    client.execute(new HttpGet(url), response -> {
                        Files.copy(response.getEntity().getContent(),
                                   Paths.get("src/main/java/bot/songs/" + song + ".mp3"),
                                   StandardCopyOption.REPLACE_EXISTING);
                        return null;
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    event.reply("Failed to download song.").queue();
                    return;
                }
                event.reply("Song downloaded successfully!" + "\nFile: " + song).queue();
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();
    }

}
