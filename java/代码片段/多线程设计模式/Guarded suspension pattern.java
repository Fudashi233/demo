package test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 付大石
 * Guarded Suspension：监视挂起
 * Client发送Request到RequestQueue中，Server从RequestQueue
 * 中获取Request。当RequestQueue装满了Request时，Client发送Request的操作被阻塞，
 * 即不满足监视条件而被挂起，下同。当RequestQueue已经没有了Request时，
 * Server接收Request的操作被阻塞
 * 
 * the point:
 * 1.guard condition
 * 2.wait/notify/notifyAll 机制
 * 3.线程真正等待的，是实例状态的变化
 * 
 */
public class Test {
	
    public static void main(String[] args) {
        
        RequestQueue queue = new RequestQueue();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new Client(queue));
        executor.execute(new Server(queue));
        executor.shutdown();
    }
    
    private static class Request {
        
        private final String name;
        
        public Request(String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        @Override
        public String toString() {
            return String.format("[Request:name=%s]",name);
        }
    }
    
    private static class RequestQueue {
        private static final int MAX = 10;
        private Queue<Request> queue;
        public RequestQueue() {
            this.queue = new LinkedList<>();
        }
        
        public synchronized Request getRequest() throws InterruptedException {
            
            while(queue.size()<=0) { //监视条件是大于0，当不满足监视条件时（小于等于0），线程被挂起
                System.out.println("get request has been blocked");
                super.wait();
            }
            Request r = queue.remove();
            super.notifyAll();
            return r;
        }
        
        public synchronized void putRequest(Request request) throws InterruptedException {
            
            while(queue.size()>=MAX) { //监视条件是小于 MAX，放不满足监视条件时（大于等于 MAX），线程被挂起
                System.out.println("put request has been blocked");
                super.wait();
            }
            queue.add(request);
            super.notifyAll();
        }
    }
    
    private static class Client implements Runnable {
        
        private RequestQueue queue;
        
        public Client(RequestQueue queue) {
            this.queue = queue;
        }
        
        @Override
        public void run() {
            
            try {
                for(int i=0;i<1000;i++) {
                    ThreadUtil.randomSleep(1000);
                    queue.putRequest(new Request(String.valueOf(i)));
                }
            } catch (InterruptedException e) {
               throw new RuntimeException("the "+ThreadUtil.curThreadName()+" has been interrupted",e);
            }
        }
    }
    
    private static class Server implements Runnable {
        
        private RequestQueue queue;
        
        public Server(RequestQueue queue) {
            this.queue = queue;
        }
        
        @Override
        public void run() {
            try {
                for(int i=0;i<1000;i++) {
                    ThreadUtil.randomSleep(2000);
                    System.out.println(queue.getRequest());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("the "+ThreadUtil.curThreadName()+" has been interrupted",e);
            }
        }
    }
    
}