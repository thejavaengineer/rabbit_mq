package org.thejavaengineer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqProducer {
    private static final String QUEUE_NAME = "longMessageQueue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Connect to RabbitMQ running locally

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare a queue
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // Create a long message
            StringBuilder messageBuilder = new StringBuilder();
            for (int i = 0; i < 5000; i++) { // Adjust length as needed
                messageBuilder.append("Line ").append(i).append(": This is a long message.\n");
            }
            String longMessage = messageBuilder.toString();

            // Publish the message to the queue
            channel.basicPublish("", QUEUE_NAME, null, longMessage.getBytes());
            System.out.println("Long message sent to queue: " + QUEUE_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
