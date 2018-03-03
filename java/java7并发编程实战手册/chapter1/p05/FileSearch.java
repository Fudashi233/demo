package chapter1.p5;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author 付大石
 * 利用异常机制来中断线程运行
 * 
 * 相比于chapter1.p4,这个类的线程由于使用了较复杂的递归及函数调用，
 * 根本无法用isInterrupted()来实现线程中断
 */
public class FileSearch implements Runnable {

	private String initFile;
	private String fileName;
	public FileSearch(String initFile,String fileName) {
		this.initFile = initFile;
		this.fileName = fileName;
	}
	
	@Override
	public void run() {
		File file = new File(initFile);
		if(file.isDirectory()) {
			try {
				searchFile(file);
			} catch (InterruptedException e) {
				System.out.println("The thread has been interrupted");
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("参数initFile指向的路径不是文件夹");
		}
	}
	
	private void searchFile(File file) throws InterruptedException {
		
		File[] fileArr = file.listFiles();
		if(fileArr==null) {
			return ;
		}
		for(int i=0;i<fileArr.length;i++) {
			if(fileArr[i].isDirectory()) { //如果是文件夹则递归调用searchFile
				searchFile(fileArr[i]);
			} else {
				fileProcess(fileArr[i]);
			}
		}
		if(Thread.interrupted()) {
			System.out.println("30---");
			throw new InterruptedException();
		}
	}
	
	private void fileProcess(File file) throws InterruptedException {
		
		if(file.getName().equals(fileName)) {
			System.out.println("find a file:"+file.getAbsolutePath());
		}
		if(Thread.interrupted()) {
			System.out.println("28---");
			throw new InterruptedException();
		}
	}
}