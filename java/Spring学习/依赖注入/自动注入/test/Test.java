package cn.edu.jxau.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    
    @Autowired
    private static CompactDisc CD;
    
    public static void main(String[] args) {
        
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        CDPlayer player = context.getBean(CDPlayer.class);
        player.play();
    }
}