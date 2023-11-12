package com.lihuel.discordbot.discord.lavaplayer.events;

import com.lihuel.discordbot.discord.lavaplayer.Track;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired when a track ends
 */
@Getter
public class TrackEnd extends ApplicationEvent {
    private final Track track;
    private final AudioTrackEndReason endReason;
    public TrackEnd(Track track, AudioTrackEndReason endReason) {
        super(track);
        this.track = track;
        this.endReason = endReason;
    }
}
