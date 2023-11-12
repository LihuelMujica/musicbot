package com.lihuel.discordbot.discord.lavaplayer;

import com.lihuel.discordbot.discord.lavaplayer.events.*;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Holder for both the player and a track scheduler for one guild.
 * This is a wrapper around AudioPlayer
 * This class is used to manage the audio players for each guild.
 * It is also used to load tracks from URLs.
 * This class is a singleton.
 */
@Component
public class PlayerManager {
    private final ApplicationContext applicationContext;
    private static final Logger log = LogManager.getLogger(PlayerManager.class);
    private Map<Long, GuildMusicManager> guildMusicManagers = new HashMap<>();
    private AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();


    @Autowired
    private PlayerManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager, guild, applicationContext);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getAudioForwarder());
            return guildMusicManager;
        });
    }

    public void play(SlashCommandInteractionEvent event, String trackUrl) {
        Guild guild = event.getGuild();
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        log.info("Loading track: {}", trackUrl);
        audioPlayerManager.loadItemOrdered(guildMusicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(com.sedmelluq.discord.lavaplayer.track.AudioTrack audioTrack) {
                log.info("Track loaded: {}", audioTrack.getInfo().title);
                Track track = new Track(audioTrack.getInfo(), trackScheduler.getLoop(), trackScheduler.getQueue(), event);
                audioTrack.setUserData(track);
                guildMusicManager.getTrackScheduler().queue(audioTrack);
                applicationContext.publishEvent(new TrackLoaded(track));
            }

            @Override
            public void playlistLoaded(com.sedmelluq.discord.lavaplayer.track.AudioPlaylist audioPlaylist) {
                if(trackUrl.contains("ytsearch:")) {
                    log.info("Playlist loaded: {}", audioPlaylist.getTracks().get(0).getInfo().title);
                    Track track = new Track(audioPlaylist.getTracks().get(0).getInfo(), trackScheduler.getLoop(), trackScheduler.getQueue(), event);
                    audioPlaylist.getTracks().get(0).setUserData(track);
                    guildMusicManager.getTrackScheduler().queue(audioPlaylist.getTracks().get(0));
                    applicationContext.publishEvent(new TrackLoaded(track));
                } else {
                    log.info("Playlist loaded: {}", audioPlaylist.getTracks().get(0).getInfo().title);
                    audioPlaylist.getTracks().forEach(audioTrack -> audioTrack.setUserData(new Track(audioTrack.getInfo(), trackScheduler.getLoop(), trackScheduler.getQueue(), event)));
                    audioPlaylist.getTracks().forEach(audioTrack -> guildMusicManager.getTrackScheduler().queue(audioTrack));

                    applicationContext.publishEvent(
                            new PlaylistLoaded(
                                    audioPlaylist.getTracks().stream().map(audioTrack -> (Track) audioTrack.getUserData()).toList(
                            ), trackUrl
                    ));
                }
            }

            @Override
            public void noMatches() {
                log.info("No matches found for track: {}", trackUrl);
                applicationContext.publishEvent(new NoMatches(event, trackUrl));
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                log.error("Could not play: {}", exception.getMessage());
                applicationContext.publishEvent(new TrackException(event, exception));
            }
        });
    }

    public void skipTrack(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        TrackScheduler trackScheduler = getGuildMusicManager(guild).getTrackScheduler();
        if (trackScheduler.getCurrentTrack() == null) {
            applicationContext.publishEvent(new CustomError("No hay canciones en la cola", event));
            return;
        }
        trackScheduler.nextTrack();
        applicationContext.publishEvent(new SkipTrack(event));
    }
}
