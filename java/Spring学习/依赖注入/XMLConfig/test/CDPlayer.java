package cn.edu.jxau.test;

public class CDPlayer {
    
    private CompactDisc compactDisc;
    
    public CDPlayer() {
        
    }
    
    public void setCompactDisc(CompactDisc compactDisc) {
        this.compactDisc = compactDisc;
    }
    
    public void play() {
        compactDisc.play();
    }
}
