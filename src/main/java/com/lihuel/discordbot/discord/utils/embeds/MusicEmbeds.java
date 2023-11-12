package com.lihuel.discordbot.discord.utils.embeds;

import com.lihuel.discordbot.discord.lavaplayer.Track;
import com.lihuel.discordbot.utils.SecurityUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MusicEmbeds {
    public static @NotNull String formatTrackLength(long trackLength) {
        long hours = TimeUnit.MILLISECONDS.toHours(trackLength);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(trackLength) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(trackLength));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(trackLength) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(trackLength));
        String time = "";
        if (hours > 0) time += hours + ":";
        if (minutes < 10 && hours > 0) time += "0";
        time += minutes + ":";
        if (seconds < 10) time += "0";
        time += seconds;
        return time;
    }

    public static MessageEmbed trackAdded(Track track) {
        return displayTrack(track, "Añadida a la cola");
    }

    public static MessageEmbed playingTrack(Track track) {
        return displayTrack(track, "Reproduciendo");
    }

    public static MessageEmbed displayTrack(Track track, String status) {
        String duration = formatTrackLength(track.getInfo().length);
        String repeat = track.getLoop() ? "Activado" : "Desactivado";
        String userMention = track.getEvent().getUser().getAsMention();
        String lihuelMention = "@lihuel2000";
        String lihuelIcon = "https://cdn.discordapp.com/avatars/353729891872669696/bd25303305cd4eb01376e4667fb0530b.webp?size=128";
        return new EmbedBuilder()
                .setTitle(status)
                .setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")")
                .addField("Duración", "`"+duration+"`", true)
                .addField("Tamaño de la cola", "`"+(track.getQueue().size())+"`", true)
                .addField("Pedida por", userMention, true)
                .addField("Link", "[`enlace`]("+track.getInfo().uri+")", true)
                .addField("Bucle: ", "`"+repeat+"`", true)
                .setColor(Color.blue)
                .setThumbnail(getThumbnail(track))
                .setFooter("by " + lihuelMention + " md para informar de bugs y suggerencias :)", lihuelIcon)
                .build();
    }

    /**
     * Display a playlist in a list of embeds
     * @param tracks
     * @return List of embeds. Each embed contains 10 tracks
     */
    public static MessageEmbed displayPlaylist(List<Track> tracks, String playlistUrl) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Playlist agregada");
        embedBuilder.setDescription(tracks.get(0).getEvent().getMember().getAsMention() + " ha añadido " + "[`" + tracks.size() + " canciones a la cola`]("+playlistUrl+")");
        embedBuilder.setColor(Color.blue);
        embedBuilder.setThumbnail(getThumbnail(tracks.get(0)));
        embedBuilder.setFooter("by @lihuel2000 md para informar de bugs y suggerencias :)", "https://cdn.discordapp.com/avatars/353729891872669696/bd25303305cd4eb01376e4667fb0530b.webp?size=128");
        return embedBuilder.build();
    }

    private static String getThumbnail(Track track) {
        String domain = SecurityUtils.getDomain(track.getInfo().uri);
        return String.format("https://img.youtube.com/vi/%s/0.jpg", track.getInfo().identifier);
    }
}
