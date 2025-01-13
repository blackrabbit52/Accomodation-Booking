package com.jaynie.accombooking.rabbitMq;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_NAME = "application_notifications";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }
}