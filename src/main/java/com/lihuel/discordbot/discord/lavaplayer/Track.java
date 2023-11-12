package com.lihuel.discordbot.discord.lavaplayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.BlockingQueue;

@Data
@AllArgsConstructor
@ToString
public class Track {
    AudioTrackInfo info;
    Boolean loop;
    BlockingQueue<AudioTrack> queue;
    SlashCommandInteractionEvent event;

}
