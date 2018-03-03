package chapter1.p5;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		
		FileSearch searcher = new FileSearch("e://","target.txt"); //在initFile下查找名为fileName的文件
		Thread t = new Thread(searcher);
		t.start();
		try {
			TimeUnit.MILLISECONDS.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.interrupt();
	}
}
