package cn.edu.jxau.jms;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/2/4
 * Time:下午9:57
 */
public class Retailer {

    private Sender sender;

    public Retailer() throws JMSException {
        sender = new Sender("DefaultQueue");
    }

    public void sendMessageForGold(String text) throws JMSException {

        TextMessage textMessage = sender.createTextMessage(text);
        textMessage.setStringProperty("custType", "GOLD"); //customer type
        sender.sendMessage(textMessage);
    }

    public void sendMessageForSilver(String text) throws JMSException {
        TextMessage textMessage = sender.createTextMessage(text);
        textMessage.setStringProperty("custType", "SILVER"); //customer type
        sender.sendMessage(textMessage);
    }

    public void close() {
        if (sender != null) {
            sender.close();
        }
    }
}