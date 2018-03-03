package cn.edu.jxau.test;

public class SgtPepper implements CompactDisc {

    private String title = "sgt pepper";
    private String artist = "May day";
    
    @Override
    public void play() {
        System.out.printf("title:%s,artist:%s\n",title,artist);
    }
}
