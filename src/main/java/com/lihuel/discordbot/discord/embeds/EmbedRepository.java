package com.lihuel.discordbot.discord.embeds;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class EmbedRepository {

    /**
     * A HashMap of PaginatedEmbeds, with the key being the message ID of the embed.
     */
    private final HashMap<String, PaginatedEmbed> embeds = new HashMap<>();

    public void addPaginatedEmbed(MessageEmbed[] pages, TextChannel channel) {
        PaginatedEmbed embed = new PaginatedEmbed(pages, channel);
        embeds.put(embed.getMessage().getId(), embed);
    }

    public void addPaginatedEmbed(List<MessageEmbed> pages, TextChannel channel) {
        PaginatedEmbed embed = new PaginatedEmbed(pages.toArray(new MessageEmbed[0]), channel);
        embeds.put(embed.getMessage().getId(), embed);
    }

    public void addPaginatedEmbed(MessageEmbed[] pages, SlashCommandInteractionEvent event) {
        PaginatedEmbed embed = new PaginatedEmbed(pages, event);
        embeds.put(embed.getMessage().getId(), embed);
    }

    public void addPaginatedEmbed(List<MessageEmbed> pages, SlashCommandInteractionEvent event) {
        PaginatedEmbed embed = new PaginatedEmbed(pages.toArray(new MessageEmbed[0]), event);
        embeds.put(embed.getMessage().getId(), embed);
    }

    public PaginatedEmbed getPaginatedEmbed(String id) {
        return embeds.get(id);
    }

}
