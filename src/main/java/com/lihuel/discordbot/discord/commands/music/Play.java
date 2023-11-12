package com.lihuel.discordbot.discord.commands.music;

import com.lihuel.discordbot.discord.commands.Command;
import com.lihuel.discordbot.discord.commands.events.SimpleError;
import com.lihuel.discordbot.discord.lavaplayer.PlayerManager;
import com.lihuel.discordbot.utils.SecurityUtils;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class Play implements Command {
    private final PlayerManager playerManager;
    private final ApplicationContext applicationContext;

    @Autowired
    public Play(PlayerManager playerManager, ApplicationContext applicationContext) {
        this.playerManager = playerManager;
        this.applicationContext = applicationContext;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Reproduce una canción";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "cancion", "Puedes poner texto para buscar una canción, link de youtube a una canción o una playlist de youtube", true)
        ); // Return a list of OptionData
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String name = event.getOption("cancion").getAsString();
        try {
            URI uri = new URI(name);
            if (uri.getScheme() == null && uri.getHost() == null) name = "ytsearch: " + name;
        } catch (URISyntaxException e) {
            name = "ytsearch: " + name;
        }

        try {
            if (!name.contains("ytsearch:") && !SecurityUtils.isUrlWhitelisted(name)) {
                applicationContext.publishEvent(new SimpleError(event, "No se pueden reproducir canciones de ese sitio"));
                return;
            }
        } catch (MalformedURLException e) {
            applicationContext.publishEvent(new SimpleError(event, "No se pueden reproducir canciones de ese sitio"));
            return;
        }


        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) {
            applicationContext.publishEvent(new SimpleError(event, "Tienes que estar en un canal de voz"));
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        } else {
            if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
                applicationContext.publishEvent(new SimpleError(event, "Tienes que estar en el mismo canal de voz que yo"));
                return;
            }
        }

        playerManager.play(event, name);
    }
}
