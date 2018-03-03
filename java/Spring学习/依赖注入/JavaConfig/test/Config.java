package cn.edu.jxau.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public CompactDisc sgtPepper() {
        
        System.out.println("sgtPepper()");
        return new SgtPepper();
    }
    
    @Bean
    public CDPlayer CDPlayer(CompactDisc compactDisc) {
        
        System.out.println("CDPlayer(CompactDisc CD)");
        CDPlayer player = new CDPlayer();
        player.setCompactDisc(compactDisc);
        return player;
    }
}
