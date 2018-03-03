package cn.edu.jxau.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    
    private static CompactDisc CD;
    
    public static void main(String[] args) {
        
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        CDPlayer player = context.getBean(CDPlayer.class);
        player = context.getBean(CDPlayer.class);
        player = context.getBean(CDPlayer.class);
        player.play();
    }
}