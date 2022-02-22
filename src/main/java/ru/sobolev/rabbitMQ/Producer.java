package ru.sobolev.rabbitMQ;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Producer {
    private static final String EXCHANGE_NAME = "exchanger";
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try (Connection connection = connectionFactory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            while (true){
                Scanner sc = new Scanner(System.in);
                String article = sc.nextLine();
                String topic = article.split("\\s+")[0];
                String content = article.substring(topic.length() + 1);
                channel.basicPublish(EXCHANGE_NAME, topic, null, content.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
