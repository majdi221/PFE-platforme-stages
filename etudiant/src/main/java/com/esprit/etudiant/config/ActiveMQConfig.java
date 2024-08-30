package com.esprit.etudiant.config;

import jakarta.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMQConfig {

    public static final String CONVENTION_QUEUE = "conventionQueue";

    @Bean
    public Queue queue() {
        return new ActiveMQQueue(CONVENTION_QUEUE);
    }
}

