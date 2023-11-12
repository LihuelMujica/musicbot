package com.lihuel.discordbot.discord.lavaplayer.events;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired when a track is skipped
 */
@Getter
public class SkipTrack extends ApplicationEvent {
    private final SlashCommandInteractionEvent event;
    public SkipTrack(SlashCommandInteractionEvent event) {
        super(event);
        this.event = event;
    }
}
