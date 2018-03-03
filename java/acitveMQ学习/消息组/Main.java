package cn.edu.jxau.jms;

import javax.jms.*;
import javax.jms.IllegalStateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/2/5
 * Time:上午10:55
 */
public class Main {

    public static void main(String[] args) throws JMSException, IOException {

        //sendMessageGroup("groupId-01");
        receiveMessageGroup("groupId-01");
    }


    private static void receiveMessageGroup(String groupId) throws JMSException {

        Receiver receiver = new Receiver("DefaultQueue", null);
        receiver.setMessageListener(new MessageListener() {

            private List<Message> messageList = new ArrayList<>();
            @Override
            public void onMessage(Message message) {
                try {
                    handleMessage(message);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            private void handleMessage(Message message) throws JMSException {
                if(message.propertyExists("SequenceMarker")) {
                    String sequenceMarker = message.getStringProperty("SequenceMarker");
                    if(sequenceMarker.equals("_START_")) {
                        messageList.clear();
                    } else if(sequenceMarker.equals("_END_")) {
                        handleMessageGroup();
                        message.acknowledge();
                    } else {
                        throw new IllegalArgumentException("不支持的sequenceMarker,sequenceMarker="+sequenceMarker);
                    }
                } else {
                    messageList.add(message);
                }
            }

            private void handleMessageGroup() throws JMSException {
                System.out.println("handle message group");
                for(Message message : messageList) {
                    if(message instanceof TextMessage) {
                        System.out.println(((TextMessage) message).getText());
                    }
                }
            }
        });
    }

    /**
     * 发送消息组
     *
     * @throws JMSException
     */
    private static void sendMessageGroup(String groupId) throws JMSException {

        Sender sender = new Sender("DefaultQueue");
        sendSequenceMarker(sender, "_START_", groupId);
        sendPayLoad(sender, "first message", groupId);
        sendPayLoad(sender, "second message", groupId);
        sendPayLoad(sender, "third message", groupId);
        sendSequenceMarker(sender, "_END_", groupId);
        sender.close();
    }

    private static void sendPayLoad(Sender sender, String text, String groupId) throws JMSException {

        TextMessage textMessage = sender.createTextMessage(text);
        textMessage.setStringProperty("JMSXGroupId", groupId);
        sender.sendMessage(textMessage);
    }

    private static void sendSequenceMarker(Sender sender, String marker, String groupId) throws JMSException {

        BytesMessage byteMessage = sender.createByteMessage();
        byteMessage.setStringProperty("SequenceMarker", marker);
        byteMessage.setStringProperty("JMSXGroupId", groupId);
        sender.sendMessage(byteMessage);
    }
}