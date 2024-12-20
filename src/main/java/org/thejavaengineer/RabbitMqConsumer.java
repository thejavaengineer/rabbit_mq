package org.thejavaengineer;

import com.rabbitmq.client.*;

import java.io.IOException;

import com.rabbitmq.client.*;

public class RabbitMqConsumer {
    private static final String QUEUE_NAME = "longMessageQueue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Connect to RabbitMQ running locally

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the queue (to ensure it exists)
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // Define the callback to handle received messages
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Received message: \n" + message);
            };

            // Start consuming messages from the queue
            System.out.println("Waiting for messages...");
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
                System.out.println("Consumer " + consumerTag + " canceled.");
            });

            // Keep the main thread alive to listen for messages
            synchronized (RabbitMqConsumer.class) {
                RabbitMqConsumer.class.wait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

