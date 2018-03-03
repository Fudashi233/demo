package cn.edu.jxau.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/2/5
 * Time:上午10:55
 */
public class Main {

    public static void main(String[] args) throws JMSException {

        Retailer retailer = new Retailer();
        retailer.sendMessageForGold("a");
        retailer.sendMessageForGold("b");
        retailer.sendMessageForGold("c");
        retailer.sendMessageForSilver("1");
        retailer.sendMessageForSilver("2");
        retailer.sendMessageForSilver("3");
        WholesalerGold wholesalerGold = new WholesalerGold();
        WholesalerSilver wholesalerSilver = new WholesalerSilver();
    }
}
