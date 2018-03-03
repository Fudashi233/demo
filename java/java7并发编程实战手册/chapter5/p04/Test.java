package chapter5.p04;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Test {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		FolderProcessor system=new FolderProcessor("C:\\Windows", "log");
		FolderProcessor apps=new FolderProcessor("C:\\Program Files","log");
		FolderProcessor documents=new FolderProcessor("C:\\Documents And Settings","log");
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(system);
		pool.execute(apps);
		pool.execute(documents);
		pool.shutdown();
		System.out.println(system.get().size());
		System.out.println(apps.join().size());
		System.out.println(documents.join().size());
	}
}

class FolderProcessor extends RecursiveTask<List<String>> {

	private File directory;
	private String suffix;
	public FolderProcessor(String path,String suffix) {
		
		directory = new File(path);
		if(!directory.isDirectory()) {
			throw new IllegalArgumentException("参数path指定的路径不是文件夹");
		}
		this.suffix = suffix;
		
	}
	
	@Override
	protected List<String> compute() {
		
		List<String> list = new ArrayList<String>();
		List<RecursiveTask<List<String>>> taskList = new ArrayList<RecursiveTask<List<String>>>();
		File[] fileArr = directory.listFiles();
		if(fileArr!=null) {
			for(int i=0;i<fileArr.length;i++) {
				if(fileArr[i].isDirectory()) {
					RecursiveTask<List<String>> task = new FolderProcessor(fileArr[i].getAbsolutePath(),suffix);
					task.fork();
					taskList.add(task);
				} else {
					if(checkFile(fileArr[i].getName())) {
						list.add(fileArr[i].getAbsolutePath());
					}
				}
			}
		}
		for(int i=0;i<taskList.size();i++) {
			try {
				list.addAll(taskList.get(i).get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	
	private boolean checkFile(String name) {
		return name.endsWith(suffix);
	}
}