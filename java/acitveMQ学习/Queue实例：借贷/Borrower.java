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
 * Time:上午10:47
 */
public class Borrower {

    private Connection connection;
    private Session session;
    private Queue borrowerRequestQueue;
    private Queue borrowerResponseQueue;

    public Borrower(String jndiConnectionFactory, String jndiBorrowerRequestQueue, String jndiBorrowerResponseQueue) throws NamingException, JMSException {

        Context context = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(jndiConnectionFactory);
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        borrowerRequestQueue = (Queue) context.lookup(jndiBorrowerRequestQueue);
        borrowerResponseQueue = (Queue) context.lookup(jndiBorrowerResponseQueue);
        connection.start();
    }

    public static void main(String[] args) throws JMSException, NamingException {

        Borrower borrower = new Borrower("connectionFactory", "borrowerRequest", "borrowerResponse");
        borrower.request(1000, 1500);
        borrower.request(1000, 2000);
        borrower.request(1000, 2500);
        borrower.close();
    }

    /**
     * 请求贷款
     *
     * @param salary
     * @param loan
     * @return
     */
    public void request(long salary, long loan) throws JMSException {

        MapMessage request = session.createMapMessage();
        request.setLong("salary", salary);
        request.setLong("loan", loan);
        request.setJMSReplyTo(borrowerResponseQueue);
        MessageProducer producer = null;
        MessageConsumer consumer = null;
        try {
            producer = session.createProducer(borrowerRequestQueue);
            producer.send(request);
            consumer = session.createConsumer(borrowerResponseQueue, String.format("JMSCorrelationID = '%s'", request.getJMSMessageID()));
            Message message = consumer.receive(3000);
            System.out.println("receive message...");
            if (message == null) {
                return;
            }
            if (message instanceof TextMessage) {
                System.out.println(((TextMessage) message).getText());
            } else {
                throw new RuntimeException("暂不支持的消息类型：" + message.getClass());
            }
        } catch (JMSException e) {
            throw e;
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
            } catch (JMSException e) {
                throw new RuntimeException("producer未关闭", e);
            } finally {
                try {
                    if (consumer != null) {
                        consumer.close();
                    }
                } catch (JMSException e) {
                    throw new RuntimeException("consumer未关闭", e);
                }
            }
        }
    }

    public void close() {

        try {
            if (session != null) {
                session.close();
            }
        } catch (JMSException e) {
            throw new RuntimeException("未关闭session", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                throw new RuntimeException("未关闭connection", e);
            }
        }
    }
}
