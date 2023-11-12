package com.lihuel.discordbot.discord.lavaplayer.events;

import com.lihuel.discordbot.discord.lavaplayer.Track;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired when a track starts playing
 */
@Getter
public class TrackStart extends ApplicationEvent {
    private final Track track;
    public TrackStart(Track track) {
        super(track);
        this.track = track;
    }
}
