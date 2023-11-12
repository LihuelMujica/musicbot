package com.lihuel.discordbot.discord.lavaplayer.events;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired when a track throws an exception
 */
@Getter
public class TrackException extends ApplicationEvent {
    private final FriendlyException exception;
    private final SlashCommandInteractionEvent event;

    public TrackException(SlashCommandInteractionEvent event, FriendlyException exception) {
        super(exception);
        this.exception = exception;
        this.event = event;
    }
}
