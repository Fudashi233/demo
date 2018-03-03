package cn.edu.jxau.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.util.StringUtils;

import javax.jms.*;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/2/4
 * Time:下午7:54
 */
public class Receiver {

    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;

    public Receiver(String queueName, String messageSelector) throws JMSException {

        ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                ActiveMQConnection.DEFAULT_BROKER_URL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        if (StringUtils.isEmpty(messageSelector)) {
            messageConsumer = session.createConsumer(session.createQueue(queueName));
        } else {
            messageConsumer = session.createConsumer(session.createQueue(queueName), messageSelector);
        }
    }

    public void setMessageListener(MessageListener messageListener) throws JMSException {
        messageConsumer.setMessageListener(messageListener);
    }

    public void close() {

        try {
            if (messageConsumer != null) {
                messageConsumer.close();
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