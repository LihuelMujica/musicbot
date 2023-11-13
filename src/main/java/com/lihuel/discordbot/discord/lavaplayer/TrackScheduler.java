package com.lihuel.discordbot.discord.lavaplayer;

import com.lihuel.discordbot.discord.lavaplayer.events.TrackEnd;
import com.lihuel.discordbot.discord.lavaplayer.events.TrackStart;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final Logger log = LogManager.getLogger(TrackScheduler.class);
    private final ApplicationContext applicationContext;
    private final AudioPlayer player;
    private AudioTrack currentTrack;
    @Getter
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private boolean loop = false;

    public TrackScheduler(AudioPlayer player, ApplicationContext applicationContext) {
        this.player = player;
        this.applicationContext = applicationContext;
    }

    /**
     * Called when a track ends
     * @param player Audio player
     * @param track Audio track that started
     */
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        log.info("Track started: {}", track.getInfo().title);
        currentTrack = track;
        applicationContext.publishEvent(new TrackStart((Track) track.getUserData()));
    }

    /**
     * Called when a track ends
     * @param player Audio player
     * @param track Audio track that ended
     * @param endReason The reason why the track stopped playing
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        log.info("Track ended: title {} | end reason {}", track.getInfo().title, endReason.toString());
        applicationContext.publishEvent(new TrackEnd((Track) track.getUserData(), endReason));

        if (endReason.mayStartNext) {
            if (loop) {
                player.playTrack(currentTrack.makeClone());
            } else {
                nextTrack();
            }
            return;
        }

        if (queue.isEmpty()) {
            currentTrack = null;
        }

    }



    /**
     * Start the loop or stop it.
     */
    public void toggleLoop() {
        loop = !loop;
    }

    public boolean getLoop() {
        return loop;
    }

    public AudioTrack getCurrentTrack() {
        return currentTrack;
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        log.info("Next track");
        if (queue.isEmpty()) {
            player.stopTrack();
        } else {
            player.playTrack(queue.poll());
        }
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void stop() {
        queue.clear();
        player.stopTrack();
    }







}