package com.lihuel.discordbot.discord.embeds;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginatedEmbedListener extends ListenerAdapter {
    private final EmbedRepository embedRepository;
    private final JDA jda;

    @Autowired
    public PaginatedEmbedListener(EmbedRepository embedRepository, JDA jda) {
        this.embedRepository = embedRepository;
        this.jda = jda;
        jda.addEventListener(this);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        String messageId = buttonId.substring(buttonId.indexOf(":") + 1);
        PaginatedEmbed embed = embedRepository.getPaginatedEmbed(messageId);
        if (embed == null) {
            Message message = event.getMessage();
            message.editMessageEmbeds(message.getEmbeds().get(0)).setComponents().queue();
            return;
        }
        if (buttonId.startsWith("next")) {
            embed.next();
        } else if (buttonId.startsWith("prev")) {
            embed.previous();
        }
        event.deferEdit().queue();
    }

}
