package cn.edu.jxau.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/2/4
 * Time:下午7:54
 */
public class Sender {

    private MessageProducer messageProducer;
    private Session session;
    private Connection connection;

    public Sender(String queueName) throws JMSException {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        messageProducer = session.createProducer(session.createQueue(queueName));
    }

    public void sendMessage(Message message) throws JMSException {
        messageProducer.send(message);
    }

    public TextMessage createTextMessage(String text) throws JMSException {
        return session.createTextMessage(text);
    }

    public BytesMessage createByteMessage() throws JMSException {
        return session.createBytesMessage();
    }

    public void close() {

        try {
            if (messageProducer != null) {
                messageProducer.close();
            }
        } catch (JMSException e) {
            throw new RuntimeException("MessageProducer 关闭失败", e);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (JMSException e) {
                throw new RuntimeException("Session 关闭失败", e);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (JMSException e) {
                    throw new RuntimeException("Connection 关闭失败", e);
                }
            }
        }
    }
}