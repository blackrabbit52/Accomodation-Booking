package com.jaynie.accombooking.rabbitMq;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class NotificationPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate; // WebSocket Messaging


    public NotificationPublisher(RabbitTemplate rabbitTemplate, SimpMessagingTemplate messagingTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(String message) {
        String queueName = "application_notifications";
        // Sending notification to RabbitMQ
        rabbitTemplate.convertAndSend(queueName, message);
        System.out.println("📩 Sent Notification to Queue: " + message);

        // Sending notification to WebSockets (Frontend)
        messagingTemplate.convertAndSend("/topic/notifications", message);
        System.out.println("🌐 Sent Notification to WebSocket: " + message);
    }
}
