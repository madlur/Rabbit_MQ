package ru.sobolev.rabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;

public class Consumer {
    private static final String EXCHANGE_NAME = "exchanger";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();

        new Thread(() -> {
            while (true) {
                try {
                    Scanner sc = new Scanner(System.in);
                    String command = sc.nextLine();
                    String topic = command.split(" ")[1];
                    if (command.startsWith("set_topic")) {
                        channel.queueBind(queueName, EXCHANGE_NAME, topic);
                        System.out.println("Вы подписаны на: " + topic);
                    }

                    if (command.startsWith("unsubscribe_topic")) {
                        channel.queueUnbind(queueName, EXCHANGE_NAME, topic);
                        System.out.println("Вы отписаны от: " + topic);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "UTF-8");
            System.out.println(msg);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

}
