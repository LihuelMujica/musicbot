package com.lihuel.discordbot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Discord {

    private static final Logger logger = LogManager.getLogger(Discord.class);

    @Value("${discord.token}")
    private String token;

    @Bean
    public JDA jda() {
        try {
            JDA jda = JDABuilder.createDefault(token).build();
            logger.info("JDA successfully created");
            return jda;
        } catch (Exception e) {
            logger.error("Error creating JDA: {}", e.getMessage());
            throw new RuntimeException("Error creating JDA");
        }    }

}
