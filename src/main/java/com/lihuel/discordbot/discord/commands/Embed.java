package com.lihuel.discordbot.discord.commands;

import com.lihuel.discordbot.discord.embeds.EmbedRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Embed implements Command {
    private final EmbedRepository embedRepository;

    @Autowired
    public Embed(EmbedRepository embedRepository) {
        this.embedRepository = embedRepository;
    }

    @Override
    public String getName() {
        return "testembed";
    }

    @Override
    public String getDescription() {
        return "Comando de prueba para probar mi implementación de embeds paginados.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("Enviando embed...").queue();
        List<MessageEmbed> embeds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Página " + (i + 1));
            embedBuilder.setDescription("Esta es la página " + (i + 1) + " de 5.");
            embeds.add(embedBuilder.build());
        }
        embedRepository.addPaginatedEmbed(embeds, (TextChannel) event.getChannel());
    }
}
