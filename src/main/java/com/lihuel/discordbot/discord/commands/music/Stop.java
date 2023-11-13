package com.lihuel.discordbot.discord.commands.music;

import com.lihuel.discordbot.discord.commands.Command;
import com.lihuel.discordbot.discord.lavaplayer.PlayerManager;
import com.lihuel.discordbot.discord.utils.embeds.AlertEmbed;
import com.lihuel.discordbot.discord.utils.embeds.MusicEmbeds;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Stop implements Command {
    private final ApplicationContext applicationContext;
    private final PlayerManager playerManager;


    @Autowired
    public Stop(ApplicationContext applicationContext, PlayerManager playerManager) {
        this.applicationContext = applicationContext;
        this.playerManager = playerManager;
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Para la reproducción de música. Limpia la playlist";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) {
            event.replyEmbeds(AlertEmbed.createError("Tienes que estar en un canal de voz para usar este comando")).queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inAudioChannel()) {
            event.replyEmbeds(AlertEmbed.createError("No hay canciones en la cola")).queue();
            return;
        } else {
            if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
                event.replyEmbeds(AlertEmbed.createError("Debes estar en el mismo canal de voz que yo")).queue();
                return;
            }
        }

        if(!playerManager.stop(event)) return;

        event.replyEmbeds(MusicEmbeds.stopMessage()).queue();
    }
}
