package test;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class Test<E> {
    
    private static final int _1MB =1024*1024;
    private static Person person = new Person();
    public static void main(String[] args) throws InterruptedException {
        
        byte[] a = new byte[_1MB*5];
        Person p = new Person();
        ReferenceQueue<Person> queue = new ReferenceQueue<>();
        PhantomReference<Person> ref = new PhantomReference<>(p,queue);
        Thread t = new Thread(new Task(queue));
        t.start();
        p = null;
        System.gc();
        Thread.sleep(1000);
        if(p==null) {
            
        }
        System.gc();
        Thread.sleep(1000);
    }
    
    private static class Task implements Runnable {
        
        private ReferenceQueue<Person> queue;
        
        public Task(ReferenceQueue<Person> queue) {
            this.queue = queue;
        }
        
        @Override
        public void run() {
            PhantomReference<Person> ref = null;
            try {
                while(true) {
                    ref=(PhantomReference) queue.remove();
                    if(ref!=null) {
                        System.out.println("真正清除Person");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static class Person {
        
        public Person() {
            System.out.println("构造Person");
        }
        @Override
        public void finalize() throws Throwable {
            super.finalize();
            System.out.println("Person不可达");
            Test.person = this; //对象复活
            System.out.println("Person复活");
        }
    }
}