package com.lihuel.discordbot.discord.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.lang.Nullable;

import java.nio.ByteBuffer;

/**
 * This class is used to forward audio from a player to a voice channel.
 */
public class AudioForwarder implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final Guild guild;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame = new MutableAudioFrame();
    private int time;

    public AudioForwarder(AudioPlayer audioPlayer, Guild guild) {
        this.audioPlayer = audioPlayer;
        this.guild = guild;
        this.buffer = ByteBuffer.allocate(1024);
        this.frame.setBuffer(buffer);
    }

    /**
     * Whether the audio source has another chunk of data to provide.
     *
     * @return True if there is another chunk to provide, false otherwise.
     */
    @Override
    public boolean canProvide() {
        boolean canProvide = audioPlayer.provide(frame);
        if(!canProvide) {
            time += 20;
            if(time >= 300000) {
                time = 0;
                guild.getAudioManager().closeAudioConnection();
            }
        }
        else {
            time = 0;
        }
        return canProvide;
    }

    /**
     * Provides the next chunk of data for audio processing.
     *
     * @return The next chunk of data.
     */
    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        return buffer.flip();
    }

    /**
     * Whether the audio source is providing audio asynchronously.
     *
     * @return True if the source is providing audio asynchronously, false otherwise.
     */
    @Override
    public boolean isOpus() {
        return true;
    }
}
