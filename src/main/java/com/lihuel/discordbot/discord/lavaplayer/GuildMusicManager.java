package com.lihuel.discordbot.discord.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.context.ApplicationContext;

/**
 * Holder for both the player and a track scheduler for one guild.
 * This is a wrapper around AudioPlayer
 */
@Getter
public class GuildMusicManager {
    private final ApplicationContext applicationContext;
    private final TrackScheduler trackScheduler;
    private final AudioForwarder audioForwarder;

    public GuildMusicManager(AudioPlayerManager manager, Guild guild, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        AudioPlayer player = manager.createPlayer();
        trackScheduler = new TrackScheduler(player, applicationContext);
        player.addListener(trackScheduler);
        audioForwarder = new AudioForwarder(player, guild);
    }

}
