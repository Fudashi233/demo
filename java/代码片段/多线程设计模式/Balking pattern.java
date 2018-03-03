package test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


  @author ����ʯ
  Balking patternֹ��ģʽ
  һ��Changer�����ϸı�Data�е����ݣ���һ��ʱ���ű��棬
  һ��Saver�����϶�Data�е����ݽ��б����������Data�е�����û���棬����б��棬����[ֹ��]��
  the point
  1.Balking pattern��Guarded suspension pattenr�Ĺ�ϵ������
 
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
        private boolean changed; pathָ����ļ��Ƿ�ı䣬�������Ϊtrue������Ϊfalse
        
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
          ֹ����ʽ�������֣�
          1.���ԣ���ʵ����ʾ
          2.�׳��쳣
          3.����booleanֵ
         
        public synchronized void save() throws IOException {
            
            if(!changed) { ���û�иı䣬��[ֹ��]
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
                    ThreadUtil.sleep(1000); ÿ��saveһ��
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