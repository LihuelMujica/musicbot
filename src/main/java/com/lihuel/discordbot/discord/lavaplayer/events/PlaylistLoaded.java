package com.lihuel.discordbot.discord.lavaplayer.events;

import com.lihuel.discordbot.discord.lavaplayer.Track;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * Event that is fired when a playlist is loaded
 */
@Getter
public class PlaylistLoaded extends ApplicationEvent {
    private final List<Track> playlist;
    private final String playlistUrl;

    public PlaylistLoaded(List<Track> playlist, String playlistUrl) {
        super(playlist);
        this.playlist = playlist;
        this.playlistUrl = playlistUrl;
    }
}
