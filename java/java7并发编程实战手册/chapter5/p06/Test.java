package chapter5.p06;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		int[] seed = {1,2,3,4,5,6,7,8,9,10};
		int[] arr = new int[100];
		for(int i=0;i<arr.length;i++) {
			arr[i] = seed[(int)(Math.random()*seed.length)];
		}
		System.out.println(Arrays.toString(arr));
		ForkJoinPool pool = new ForkJoinPool();
		Task task = new Task(arr,1);
		pool.execute(task);
		pool.shutdown();
		try {
			pool.awaitTermination(1,TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


class Task extends RecursiveTask<Integer>{

	private int first;
	private int last;
	private int[] arr;
	private int target;
	private TaskManager taskManager;
	private static final int THRESHOLD = 10;
	public static final int NOT_FOUND = -1;
	public Task(int[] arr,int target) {
		
		this.arr = arr;
		this.target = target;
		this.first = 0;
		this.last = arr.length-1;
		taskManager = new TaskManager();
		taskManager.addTask(this);
	}
	
	private Task(int[] arr,int target,int first,int last,TaskManager taskManager) {
		
		this.arr = arr;
		this.target = target;
		this.first = first;
		this.last = last;
		this.taskManager = taskManager;
		taskManager.addTask(this);
	}
	
	
	@Override
	protected Integer compute() {
		
		int found = Task.NOT_FOUND;
		if((last-first+1) <= Task.THRESHOLD) {
			return findTarget();
		} else {
			int mid = (first+last)/2;
			Task task1 = new Task(arr,target,first,mid,taskManager);
			Task task2 = new Task(arr,target,mid+1,last,taskManager);
			task1.fork();
			task2.fork();
			found = task1.join();
			if(found == Task.NOT_FOUND) { //判断task1是否找到target，找到即返回
				found = task2.join();
			}
			return found; //都没有找到
		}
	}
	
	private int findTarget() {
		
		for(int i=first;i<=last;i++) {
			if(arr[i]==target) {
				System.out.printf("the target found in %d\n",i);
				taskManager.cancel(this);
				return i;
			}
			try {
				ThreadUtil.randomSleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return Task.NOT_FOUND;
	}
	
	public void cancelMessage() {
		System.out.printf("cancel task from %d to %d\n",first,last);
	}
	
}

class TaskManager {
	
	private List<Task> list;
	
	public TaskManager() {
		list = new ArrayList<Task>();
	}
	
	public void addTask(Task task) {
		list.add(task);
	}
	
	public void cancel(Task task) {
		
		for(int i=0;i<list.size();i++) { //取消除task以外的Task
			Task tempTask = list.get(i);
			if(task!=tempTask) {
				task.cancel(true);
				tempTask.cancelMessage();
			}
		}
	}
}