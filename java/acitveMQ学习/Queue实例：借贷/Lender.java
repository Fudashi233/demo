package cn.edu.jxau.ch04;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/1/14
 * Time:上午11:39
 */
public class Lender implements MessageListener {

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public Lender(String jndiConnectionFactory, String jndiBorrowerRequestQueue) throws NamingException, JMSException {

        Context context = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(jndiConnectionFactory);
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue borrowerRequestQueue = (Queue) context.lookup(jndiBorrowerRequestQueue);
        consumer = session.createConsumer(borrowerRequestQueue);
        consumer.setMessageListener(this);
        connection.start();
    }

    public static void main(String[] args) throws JMSException, NamingException {

        new Lender("connectionFactory", "borrowerRequest");
    }

    @Override
    public void onMessage(Message message) {

        MessageProducer producer = null;
        try {
            if (!(message instanceof MapMessage)) {
                throw new RuntimeException("暂不支持的消息类型：" + message.getClass());
            }
            MapMessage mapMessage = (MapMessage) message;
            long salary = mapMessage.getLong("salary");
            long loan = mapMessage.getLong("loan");
            System.out.printf("borrower request:salary=%d,loan=%d,accept=%b\n", salary, loan, loan <= salary * 1.5);
            TextMessage response = loan <= salary * 1.5 ? session.createTextMessage("accept") : session.createTextMessage("reject");
            response.setJMSCorrelationID(message.getJMSMessageID());
            producer = session.createProducer(message.getJMSReplyTo());
            producer.send(response);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
            } catch (JMSException e) {
                throw new RuntimeException("producer未关闭", e);
            }
        }
    }

    public void close() {

        try {
            if (consumer != null) {
                consumer.close();
            }
        } catch (JMSException e) {
            throw new RuntimeException("consumer未关闭", e);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (JMSException e) {
                throw new RuntimeException("session未关闭", e);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (JMSException e) {
                    throw new RuntimeException("connection未关闭", e);
                }
            }
        }
    }
}
