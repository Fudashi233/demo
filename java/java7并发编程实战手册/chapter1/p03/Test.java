package chapter1.p3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.State;

public class Test {
	
	public static void main(String[] args) {
		
		/*打印线程的三种主要优先级*/
		System.out.println(Thread.MAX_PRIORITY); //10
		System.out.println(Thread.NORM_PRIORITY); //5,java线程的默认优先级
		System.out.println(Thread.MIN_PRIORITY); //1
		
		/*创建十个线程，5 with the max priority,5 with the min priority*/
		Thread[] threadArr = new Thread[10];
		for(int i=0;i<threadArr.length;i++) {
			threadArr[i] = new Thread(new Calculator(i));
			if(i%2==0) {
				threadArr[i].setPriority(Thread.MIN_PRIORITY);
			} else {
				threadArr[i].setPriority(Thread.MAX_PRIORITY);
			}
			threadArr[i].setName("thread"+i);
		}
		
		/*创建状态数组*/
		State[] stateArr= new State[threadArr.length];
		
		/*打印状态变化*/
		try(PrintWriter pw = new PrintWriter(new FileWriter("log.txt"));) {
			for(int i=0;i<threadArr.length;i++) {
				pw.printf("state of %s:%s\r\n",threadArr[i].getName(),threadArr[i].getState());
				stateArr[i] = threadArr[i].getState();
			}
			
			/* 开启线程 */
			for(int i=0;i<threadArr.length;i++) {
				threadArr[i].start();
			}
			boolean finish = false;
			while(!finish) {
				for(int i=0;i<10;i++) {
					if(threadArr[i].getState()!=stateArr[i]) { //线程状态发生了变法
						printThreadInfo(pw,threadArr[i],stateArr[i]);
						stateArr[i] = threadArr[i].getState();
					}
				}
				finish = true;
				for(int i=0;i<threadArr.length;i++) { 
					finish = finish && threadArr[i].getState()==State.TERMINATED;
				}
			}
			
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	private static void printThreadInfo(PrintWriter pw,Thread thread,State state) {
		
		pw.printf("ID:%d-name:%s\r\n",thread.getId(),thread.getName());
		pw.printf("priority:%d\r\n",thread.getPriority());
		pw.printf("old state:%s\r\n",state);
		pw.printf("new state:%s\r\n",thread.getState());
		pw.printf("-----------------------\n");
	}
}
