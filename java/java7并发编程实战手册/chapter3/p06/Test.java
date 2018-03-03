package chapter3.p06;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		
		Phaser phaser = new Phaser(3);
		Runnable s1 = new Search("C:\\Program Files",".log",phaser);
		Runnable s2 = new Search("C:\\PerfLogs",".log",phaser);
		Runnable s3 = new Search("C:\\ProgramData",".log",phaser);
		Thread t1 = new Thread(s1);
		Thread t2 = new Thread(s2);
		Thread t3 = new Thread(s3);
		t1.start();
		t2.start();
		t3.start();
	}
}

class Search implements Runnable {
	
	private File directory;
	private String end;
	private Phaser phaser;
	private List<String> result;
	
	/**
	 * @param path 检索路径
	 * @param postfix 检索文件后缀名
	 * @param phaser 线程同步相位器
	 */
	public Search(String path,String end,Phaser phaser) {
		
		this.directory = new File(path);
		if(!directory.isDirectory()) {
			throw new IllegalArgumentException("参数异常，path指向的路径不是文件夹,path:"+path);
		}
		this.end = end;
		this.phaser = phaser;
		result = new ArrayList<String>();
	}
	
	@Override
	public void run() {
		
		/* 1st phaser:search */
		search(directory);
		if(isEmpty()) {
			return ;
		}
		System.out.printf("$the 1st phaser of %s is over\n",Thread.currentThread().getName());
		/* 2st phaser:filter */
		filterResult();
		if(isEmpty()) {
			return ;
		}
		System.out.printf("$the 2st phaser of %s is over\n",Thread.currentThread().getName());
		/* 3st phaser:show */
		showInfo();
		phaser.arriveAndDeregister();
		System.out.printf("$the 3st phaser of %s is over\n",Thread.currentThread().getName());
		
		System.out.println(phaser.isTerminated()); //当Phaser没有任何参与者时，处于中止态
	}
	
	/**
	 * @return
	 * 判断result是否为空，如果为空则从phaser中注销掉
	 */
	private boolean isEmpty() {
		
		if(result.isEmpty()) {
			phaser.arriveAndDeregister();
			System.out.printf("deregister thread %s\n",Thread.currentThread().getName());
			return true;
		} else {
			phaser.arriveAndAwaitAdvance();
			return false;
		}
	}
	
	private void filterResult() {
		
		List<String> newResult = new ArrayList<String>();
		for(int i=0;i<result.size();i++) {
			String temp = result.get(i);
			if(temp.endsWith(end)) {
				File tempFile = new File(temp);
				if(((new Date().getTime())-(tempFile.lastModified()))<TimeUnit.MICROSECONDS.convert(1,TimeUnit.DAYS)) {
					newResult.add(temp);
				}
			}
		}
		this.result = newResult;
	}
	
	/**
	 * @param file
	 * 检索path指定的文件夹
	 */
	private void search(File file) {
		
		if(file.isDirectory()) {
			File[] fileArr = file.listFiles();
			if(fileArr==null) {
				return ;
			}
			for(int i=0;i<fileArr.length;i++) {
				search(fileArr[i]);
			}	
		} else {
			result.add(file.getAbsolutePath());
		}
	}
	
	private void showInfo() {
		
		System.out.printf("size of result of %s\n",Thread.currentThread().getName());
	}
}