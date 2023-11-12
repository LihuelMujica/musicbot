package com.lihuel.discordbot.discord.notifications;

import com.lihuel.discordbot.discord.commands.events.SimpleError;
import com.lihuel.discordbot.discord.utils.embeds.AlertEmbed;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CommandsNotifications {
    @EventListener
    public void onSimpleError(SimpleError event) {
        event.getEvent().replyEmbeds(
                AlertEmbed.createError(event.getMessage())
        ).queue();
    }
}
