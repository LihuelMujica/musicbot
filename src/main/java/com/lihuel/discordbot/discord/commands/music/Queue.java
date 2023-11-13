package com.lihuel.discordbot.discord.commands.music;

import com.lihuel.discordbot.discord.commands.Command;
import com.lihuel.discordbot.discord.commands.events.SimpleError;
import com.lihuel.discordbot.discord.embeds.EmbedRepository;
import com.lihuel.discordbot.discord.lavaplayer.PlayerManager;
import com.lihuel.discordbot.discord.lavaplayer.Track;
import com.lihuel.discordbot.discord.utils.embeds.MusicEmbeds;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Queue implements Command {
    private final EmbedRepository embedRepository;
    private final PlayerManager playerManager;
    private final ApplicationContext applicationContext;

    @Autowired
    public Queue(EmbedRepository embedRepository, PlayerManager playerManager, ApplicationContext applicationContext) {
        this.embedRepository = embedRepository;
        this.playerManager = playerManager;
        this.applicationContext = applicationContext;
    }

    @Override
    public String getName() {
        return "playlist";
    }

    @Override
    public String getDescription() {
        return "Muestra la lista de reproducción";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        if(!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
            applicationContext.publishEvent(new SimpleError(event, "No se está reproduciendo nada."));
            return;
        }

        List<Track> tracks = playerManager.getQueue(event.getGuild(), event);
        if (tracks.isEmpty()) {
            applicationContext.publishEvent(new SimpleError(event, "No se está reproduciendo nada."));
            return;
        }

        if (tracks.size() == 1) {
            applicationContext.publishEvent(new SimpleError(event, "No hay más canciones en la lista de reproducción."));
            return;
        }

        List<MessageEmbed> pages = MusicEmbeds.displayPlaylist(tracks);
        embedRepository.addPaginatedEmbed(pages, event);
    }
}
