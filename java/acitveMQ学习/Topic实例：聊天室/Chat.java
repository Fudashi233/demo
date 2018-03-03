package cn.edu.jxau.ch02;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/1/7
 * Time:下午5:35
 */
public class Chat {

    private String topic;
    private String username;

    private Connection connection;
    private Session producerSession;
    private Session consumerSession;
    private MessageProducer producer;
    private MessageConsumer consumer;

    public Chat(String topic, String username) throws NamingException, JMSException {

        // chat info //
        this.topic = topic;
        this.username = username;

        // connection，session，producer/consumer //
        InitialContext context = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("connectionFactory");
        connection = connectionFactory.createConnection();
        Destination destination = (Destination) context.lookup(topic);
        producerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = producerSession.createProducer(destination);
        consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = consumerSession.createConsumer(destination, null, true);

        // set message listener //
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {

                MapMessage mapMessage = (MapMessage) message;
                try {
                    System.out.println(mapMessage.getStringProperty("sender"));
                    System.out.println(mapMessage.getStringProperty("message"));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        connection.start();
    }

    public void sendMessage(String message) throws JMSException {

        System.out.println("\t\t\t\t\t\t" + username);
        System.out.println("\t\t\t\t\t\t" + message);
        MapMessage mapMessage = producerSession.createMapMessage();
        mapMessage.setStringProperty("sender", username);
        mapMessage.setStringProperty("message", message);
        producer.send(mapMessage);
    }

    public void close() {

        try {
            if (consumer != null) {
                consumer.close();
            }
        } catch (JMSException e) {
            throw new RuntimeException("consumer关闭失败", e);
        } finally {
            try {
                if (consumerSession != null) {
                    consumerSession.close();
                }
            } catch (JMSException e) {
                throw new RuntimeException("consumerSession关闭失败", e);
            } finally {
                try {
                    if (producer != null) {
                        producer.close();
                    }
                } catch (JMSException e) {
                    throw new RuntimeException("producer关闭失败", e);
                } finally {
                    try {
                        if (producerSession != null) {
                            producerSession.close();
                        }
                    } catch (JMSException e) {
                        throw new RuntimeException("producerSession关闭失败", e);
                    } finally {
                        try {
                            if (connection != null) {
                                connection.close();
                            }
                        } catch (JMSException e) {
                            throw new RuntimeException("connection关闭失败", e);
                        }
                    }
                }
            }
        }

    }
}
