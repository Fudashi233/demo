package test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


  @author 付大石
  Balking pattern止步模式
  一个Changer，不断改变Data中的数据，过一段时间后才保存，
  一个Saver，不断对Data中的数据进行保存操作，当Data中的数据没保存，则进行保存，否则[止步]。
  the point
  1.Balking pattern与Guarded suspension pattenr的关系与折中
 
public class Test {
	
    public static void main(String[] args) {
        
        Data data = new Data(CUsersPC-CliveDesktoptest.txt,balking pattern);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new Changer(data));
        executor.execute(new Saver(data));
    }
    
    private static class Data {
        
        private final String path;
        private String content;
        private boolean changed; path指向的文件是否改变，如果是则为true，否则为false
        
        public Data(String path,String content) {
            
            this.path = path;
            this.content = content;
            this.changed = true;
        }
        
        public synchronized void change(String content) {
            
            System.out.println(the +ThreadUtil.curThreadName()+ change);
            this.content = content;
            this.changed = true;
        }
        
        
          @throws IOException
          止步方式大致三种：
          1.忽略，即实例所示
          2.抛出异常
          3.返回boolean值
         
        public synchronized void save() throws IOException {
            
            if(!changed) { 如果没有改变，则[止步]
                return ;
            }
            doSave();
            changed = false;
        }
        
        private void doSave() throws IOException {
            
            System.out.println(the +ThreadUtil.curThreadName()+ save);
            FileWriter writer = null;
            try {
                writer = new FileWriter(path);
                writer.write(content);
            } finally {
                writer.close();
            }
        }
    }
    
    public static class Saver implements Runnable {
        
        private Data data;

        public Saver(Data data) {
            this.data = data;
        }
        
        @Override
        public void run() {
            
            try {
                while(!ThreadUtil.curIsInterrupted()) {
                    ThreadUtil.sleep(1000); 每秒save一次
                    data.save();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static class Changer implements Runnable {
        
        private Data data;
        
        public Changer(Data data) {
            this.data = data;
        }
        
        @Override
        public void run() {
            
            try {
                for(int i=0;i100;i++) {
                    data.change(String.format(N.O.%d,1));
                    ThreadUtil.randomSleep(2000);
                    data.save();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}