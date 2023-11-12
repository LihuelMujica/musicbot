package com.lihuel.discordbot.discord.lavaplayer.events;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired with the message the bot should send
 */
@Getter
public class CustomError extends ApplicationEvent {
    private final String error;
    private final SlashCommandInteractionEvent event;
    public CustomError(String error, SlashCommandInteractionEvent event) {
        super(error);
        this.error = error;
        this.event = event;
    }
}
