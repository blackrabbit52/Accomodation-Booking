package com.jaynie.accombooking.rabbitMq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotifConsumer {
    private final SimpMessagingTemplate messagingTemplate;

    public NotifConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println(" Received notification from RabbitMQ: " + message);

        // Broadcast message to WebSockets (Frontend)
        messagingTemplate.convertAndSend("/topic/notifications", message);
        System.out.println(" Sent Notification to WebSocket: " + message);
    }
}
