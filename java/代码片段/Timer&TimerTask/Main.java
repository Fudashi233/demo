package cn.edu.jxau.lang;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

	public static void main(String[] args) throws SQLException {
		foo2();
	}

	/**
	 * 问题1。通过查看Timer的源码，我们大概得知Timer仅用一个线程来调度多个任务，这就
	 * 产生了一个问题：如果需要调度多个任务，且某个任务的执行时间大于各个任务的间隔时间，
	 * 那么它就会影响其他任务的执行
	 */
	private static void foo1() {

		long start = System.currentTimeMillis();
		TimerTask task1 = new TimerTask() {
			@Override
			public void run() {

				System.out.println((System.currentTimeMillis() - start) + ":task1 is executing...");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					throw new RuntimeException("线程中断", e);
				}
			}
		};
		TimerTask task2 = new TimerTask() {
			@Override
			public void run() {

				System.out.println((System.currentTimeMillis() - start) + ":task2 is executing...");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException("线程中断", e);
				}
			}
		};
		Timer timer = new Timer();
		timer.schedule(task1, 1000);
		timer.schedule(task2, 2000); // task2并不是在过后2000ms时运行
	}

	/**
	 * 问题2。Timer是仅用一个线程来驱动多个任务，一旦多可任务中某一任务发生异常，其他的任务都将
	 * 无法正常运行下去
	 */
	private static void foo2() {

		TimerTask task1 = new TimerTask() {
			@Override
			public void run() {
				System.out.println("task1 is executing...");
				throw new RuntimeException("终止task1");
			}
		};
		TimerTask task2 = new TimerTask() {
			@Override
			public void run() {
				System.out.println("task2 is executing...");
			}
		};
		Timer timer = new Timer();
		timer.schedule(task1, 1000);
		timer.schedule(task2, 2000); // task2无法运行
	}

	/**
	 * 如果想单纯的避免上述问题，那就保证一个Timer调度一个TimerTask。但是如今又更好的选择
	 * 那就是ScheduledThreadPool。
	 * 问题3。Timer执行周期任务时依赖系统时间，如果当前系统时间发生变化会出现一些执行上的变化。
	 */
	private static void foo3() {

	}
}