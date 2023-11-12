package com.lihuel.discordbot.discord.commands.events;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.context.ApplicationEvent;

@Getter
public class SimpleError extends ApplicationEvent {
    private final String message;
    private final SlashCommandInteractionEvent event;
    public SimpleError(SlashCommandInteractionEvent event, String message) {
        super(event);
        this.message = message;
        this.event = event;
    }
}
