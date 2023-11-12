package com.lihuel.discordbot.discord.lavaplayer.events;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired when no matches are found for a track
 */
@Getter
public class NoMatches extends ApplicationEvent {
    private final String identifier;
    private final SlashCommandInteractionEvent event;
    public NoMatches(SlashCommandInteractionEvent event, String identifier) {
        super(identifier);
        this.identifier = identifier;
        this.event = event;
    }
}
