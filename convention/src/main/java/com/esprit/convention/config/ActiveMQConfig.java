package com.esprit.convention.config;

import jakarta.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMQConfig {

    public static final String CONVENTION_QUEUE = "conventionQueue";
    public static final String REVIEW_QUEUE = "reviewQueue";

    @Bean
    public Queue conventionQueue() {
        return new ActiveMQQueue(CONVENTION_QUEUE);
    }

    @Bean
    public Queue reviewQueue() {
        return new ActiveMQQueue(REVIEW_QUEUE);
    }
}

