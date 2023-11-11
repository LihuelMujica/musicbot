package com.lihuel.discordbot.discord;

import com.lihuel.discordbot.discord.commands.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandManager extends ListenerAdapter {
    private static final Logger log = LogManager.getLogger(CommandManager.class);

    private final List<Command> commands;
    private final JDA jda;

    @Autowired
    public CommandManager(List<Command> commands, JDA jda) {
        this.commands = commands;
        this.jda = jda;
        log.info("Registering CommandManager as listener...");
        jda.addEventListener(this);
        registerCommands();
    }

    public void registerCommands() {
        log.info("Registering commands...");
        commands.forEach(this::registerCommand);
    }

    private void registerCommand(Command command) {
        log.info("Registering command: {}", command.getName());

        if (command.getOptions() != null) {
            jda.upsertCommand(command.getName(), command.getDescription())
                    .addOptions(command.getOptions())
                    .queue();
            return;
        }
        jda.upsertCommand(command.getName(), command.getDescription()).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        for (Command command : commands) {
            if (event.getName().equals(command.getName())) {
                log.info("Executing command: {}", command.getName());
                command.execute(event);
                return;
            }
        }
    }
}
