package com.lihuel.discordbot.discord.embeds;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

@Getter
public class PaginatedEmbed {
    Message message;
    MessageEmbed[] pages;
    int currentPage = 0;

    public PaginatedEmbed(MessageEmbed[] pages, TextChannel channel) {
        message = channel.sendMessageEmbeds(pages[0]).complete();
        message.editMessageEmbeds(pages[0]).setActionRow(
                Button.secondary("prev:" + message.getId(), "Anterior"),
                Button.secondary("next:" + message.getId(), "Siguiente")
        ).queue();
        this.pages = pages;
    }

    public PaginatedEmbed(MessageEmbed[] pages, SlashCommandInteractionEvent event) {
        InteractionHook hook = event.replyEmbeds(pages[0]).complete();
        message = hook.retrieveOriginal().complete();
        message.editMessageEmbeds(pages[0]).setActionRow(
                Button.secondary("prev:" + message.getId(), "Anterior"),
                Button.secondary("next:" + message.getId(), "Siguiente")
        ).queue();
        this.pages = pages;
    }

    public void next() {
        if (currentPage == pages.length - 1) {
            currentPage = 0;
        } else {
            currentPage++;
        }
        message.editMessageEmbeds(pages[currentPage]).queue();
    }

    public void previous() {
        if (currentPage == 0) {
            currentPage = pages.length - 1;
        } else {
            currentPage--;
        }
        message.editMessageEmbeds(pages[currentPage]).queue();
    }


}
