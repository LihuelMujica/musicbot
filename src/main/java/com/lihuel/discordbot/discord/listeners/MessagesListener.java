package com.lihuel.discordbot.discord.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class MessagesListener extends ListenerAdapter {
    private final JDA jda;

    public MessagesListener(JDA jda) {
        this.jda = jda;
        jda.addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentRaw();
        if (message.toLowerCase().contains("hello there")) {
            event.getMessage().reply("https://media1.tenor.com/images/d818a41835d7cdf13107ea8928d5cbd2/tenor.gif?itemid=13723705").queue();
        }
    }
}
