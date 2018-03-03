package cn.edu.jxau.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CDPlayer {
    
    @Autowired
    private CompactDisc compactDisc;
    
    public CDPlayer() {
        
    }
    
    public void play() {
        compactDisc.play();
    }
}
