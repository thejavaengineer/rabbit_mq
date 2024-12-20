package org.thejavaengineer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.queueDeclare("testQueue", false, false, false, null);
            String message = "Hello RabbitMQ!";
            channel.basicPublish("", "testQueue", null, message.getBytes());
            System.out.println("Sent: " + message);

            GetResponse response = channel.basicGet("testQueue", true);
            if (response != null) {
                String receivedMessage = new String(response.getBody());
                System.out.println("Received: " + receivedMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

    }
}