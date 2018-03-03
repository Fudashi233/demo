package cn.edu.jxau.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/2/5
 * Time:上午10:46
 */
public class WholesalerGold {

    private Receiver receiver;
    private List<Message> messageLog;

    public WholesalerGold() throws JMSException {

        receiver = new Receiver("DefaultQueue", "custType = 'GOLD'");
        messageLog = new ArrayList<>();
        receiver.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("WholesalerGold receive a message");
                messageLog.add(message);
            }
        });
    }

    public List<Message> getMessageLog() {
        return messageLog;
    }

    private void close() {
        receiver.close();
    }
}
