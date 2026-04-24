package bot.commands;

import bot.command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import java.util.List;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import java.io.File;
import java.nio.ByteBuffer;

/**
 * Music command to play audio files in a voice channel.
 * Uses Lavaplayer library for audio playback management.
 * Connects to the user's voice channel and plays the specified song.
 * Supports local audio files and handles various loading scenarios.
 * Requires the user to be in a voice channel to execute.
 */
public class play implements command
{

    /**
     * Static audio player manager for handling audio playback.
     * Configured with local audio source manager for file-based playback.
     */
    private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    public static AudioPlayer player = playerManager.createPlayer();

    static {
        playerManager.registerSourceManager(new LocalAudioSourceManager());

        player.addListener(new AudioEventAdapter() {
            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                System.out.println("La piste s'est arrêtée. Raison : " + endReason);
            }

            @Override
            public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
                System.err.println("Erreur pendant la lecture : " + exception.getMessage());
            }
        });
    }

    /**
     * Audio send handler for streaming audio to Discord voice channels.
     * Handles the conversion of audio frames to the format required by Discord.
     * Implements the AudioSendHandler interface for voice communication.
     */
    static class AudioPlayerSendHandler implements AudioSendHandler {
        private final AudioPlayer player;
        private AudioFrame lastFrame;

        /**
         * Constructs a new audio send handler for the given player.
         * 
         * @param player The audio player to handle audio output from
         */
        AudioPlayerSendHandler(AudioPlayer player) 
        {
            this.player = player;
        }

        /**
         * Checks if audio data can be provided for streaming.
         * 
         * @return true if audio frame is available, false otherwise
         */
        public boolean canProvide() 
        { 
            lastFrame = player.provide();
            return lastFrame != null;
        }
        
        /**
         * Provides 20ms of audio data for streaming.
         * 
         * @return ByteBuffer containing the audio data
         */
        public ByteBuffer provide20MsAudio() 
        { 
            return ByteBuffer.wrap(lastFrame.getData());
        }
        
        /**
         * Indicates that the audio format is Opus (required by Discord).
         * 
         * @return true (always Opus format)
         */
        public boolean isOpus() 
        { 
            return false; 
        }
    }

    /**
     * Returns the command name used for registration with Discord.
     * 
     * @return The string "play"
     */
    @Override
    public String getName() {
        return "play";
    }

    /**
     * Returns the command description shown to users in Discord.
     * 
     * @return Description: "Play a song"
     */
    @Override
    public String getDescription() {
        return "Play a song";
    }

    /**
     * Returns the usage instructions for this command.
     * Shows the required parameter for the play command.
     * 
     * @return Usage string: "/play <song>"
     */
    @Override
    public String getUsage() {
        return "/play <song>";
    }

    /**
     * Returns the command options/parameters.
     * Requires a song name as a mandatory parameter.
     * 
     * @return List containing the song option parameter
     */
    @Override
    public List<OptionData> getOptions() {
        return List.of(
            new OptionData(OptionType.STRING, "song", "The song to play", true)
        );
    }

    /**
     * Executes the play command.
     * Plays the specified song in the user's voice channel.
     * Validates that the user is in a voice channel before proceeding.
     * Connects to the voice channel and sets up audio streaming.
     * Handles various loading scenarios (track, playlist, no matches, failure).
     * Uses thread management to prevent excessive concurrent operations.
     * 
     * @param event The slash command interaction event containing command parameters
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (this.nombreDeThreads() < this.nombreDeThreadsMax())
        {
            Thread thread = new Thread(() -> {
                event.deferReply().queue();

                // Set thread name for debugging
                Thread.currentThread().setName("Command: " + this.getName() + "\nStarted by: " + event.getUser().getName());
                
                // Get command parameters
                String song = event.getOption("song").getAsString();
                
                // Check if the user is in a voice channel
                if (!event.getMember().getVoiceState().inAudioChannel())
                {
                    event.getHook().sendMessage("You must be in a voice channel to use this command.").setEphemeral(true).queue();
                    return;
                }

                // Play the song
                playerManager.loadItem(new File("songs/" + song + ".mp3").getAbsolutePath(), new AudioLoadResultHandler() {
                    /**
                     * Called when a single audio track is successfully loaded.
                     * Starts playing the track and notifies the user.
                     * 
                     * @param track The loaded audio track
                     */
                    public void trackLoaded(AudioTrack track)
                    {
                        var audioManager = event.getGuild().getAudioManager();

                        // Set auto-reconnect to true to prevent issues
                        audioManager.setAutoReconnect(true);

                        // Set the audio player as the sending handler
                        audioManager.setSendingHandler(new AudioPlayerSendHandler(player));

                        // Play the track
                        player.setPaused(false);
                        player.setVolume(20);
                        player.playTrack(track);

                        // Get in the voice channel
                        audioManager.openAudioConnection(event.getMember().getVoiceState().getChannel());

                        // Notify the user
                        event.getHook().sendMessage("Now playing : " + track.getInfo().title).queue();
                    }
                    
                    /**
                     * Called when a playlist is successfully loaded.
                     * Notifies the user that the playlist was loaded.
                     * 
                     * @param playlist The loaded audio playlist
                     */
                    public void playlistLoaded(AudioPlaylist playlist) 
                    {
                        event.getHook().sendMessage("Playlist loaded: " + playlist.getName()).queue();
                    }
                    
                    /**
                     * Called when no matching tracks are found for the query.
                     * Notifies the user that no matches were found.
                     */
                    public void noMatches() 
                    {
                        event.getHook().sendMessage("No matches found for: " + song).queue();
                    }
                    
                    /**
                     * Called when loading fails due to an exception.
                     * Notifies the user that the track failed to load.
                     * 
                     * @param exception The exception that caused the failure
                     */
                    public void loadFailed(FriendlyException exception) 
                    {
                        event.getHook().sendMessage("Failed to load: " + song).queue();
                        exception.printStackTrace();
                    }
                });
            });
            thread.start();
            return;
        }
        event.reply("Sorry, too many requests in progress.").queue();
    }

}
