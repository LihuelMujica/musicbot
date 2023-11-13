package com.lihuel.discordbot.discord.notifications;

import com.lihuel.discordbot.discord.lavaplayer.events.*;
import com.lihuel.discordbot.discord.utils.embeds.AlertEmbed;
import com.lihuel.discordbot.discord.utils.embeds.MusicEmbeds;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MusicNotifications {
    private final JDA jda;

    /**
     * Map of current tracks being played
     * Key: Guild ID
     * Value: message ID of the embed
     */
    private final HashMap<String, Message> currentTracks = new HashMap<>();

    @Autowired
    public MusicNotifications(JDA jda) {
        this.jda = jda;
    }


    @EventListener
    public void onTrackLoaded(TrackLoaded event) {
        event.getTrack().getEvent().replyEmbeds(MusicEmbeds.trackAdded(event.getTrack())).queue();
    }

    @EventListener
    public void onTrackStart(TrackStart event) {
        SlashCommandInteractionEvent interactionEvent = event.getTrack().getEvent();
        interactionEvent.getChannel().sendMessageEmbeds(
                MusicEmbeds.playingTrack(event.getTrack())
        ).queue(message -> {
            currentTracks.put(interactionEvent.getGuild().getId(), message);
        });
    }

    @EventListener
    public void onTrackEnd(TrackEnd event) {
        System.out.println("Track end event");
        String guildId = event.getTrack().getEvent().getGuild().getId();
        currentTracks.get(guildId).delete().queue();
        currentTracks.remove(event.getTrack().getEvent().getGuild().getId());
    }

    @EventListener
    public void onPlaylistLoaded(PlaylistLoaded event) {
        SlashCommandInteractionEvent interactionEvent = event.getPlaylist().get(0).getEvent();
        interactionEvent.replyEmbeds(MusicEmbeds.playlistAdded(event.getPlaylist(), event.getPlaylistUrl())).queue();
    }

    @EventListener
    public void onNoMatches(NoMatches event) {
        event.getEvent().replyEmbeds(AlertEmbed.createError("No se encontraron canciones")).queue();
    }

    @EventListener
    public void onTrackException(TrackException event) {
        event.getEvent().replyEmbeds(AlertEmbed.createError("Error al reproducir la canción")).queue();
    }

    @EventListener
    public void onCustomError(CustomError event) {
        event.getEvent().replyEmbeds(AlertEmbed.createError(event.getError())).queue();
    }

    @EventListener
    public void onSkipTrack(SkipTrack event) {
            event.getEvent().replyEmbeds(AlertEmbed.createSuccess("La canción actual ha sido saltada")).queue();
    }

}
