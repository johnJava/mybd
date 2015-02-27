package study.pool.achievement;

import java.util.concurrent.LinkedBlockingQueue;

public class WorkThread extends Thread {
	private boolean running;
	private LinkedBlockingQueue<Task> taskquque;

	public WorkThread(LinkedBlockingQueue<Task> taskquque) {
		this.taskquque = taskquque;
		running = true;
		super.start();
	}

	@Override
	public void run() {
		while (running) {
			if (this.taskquque.isEmpty()) {
				 synchronized(this.taskquque){
					 try {
						 this.taskquque.wait();
					 } catch (InterruptedException e) {
						 e.printStackTrace();
					 }
				 }
			}
			Task task = this.taskquque.poll();
			task.setEnd(false);
			task.startTask();
			task.endTask();
			task.setEnd(true);
		}
		this.running=false;
	}
	public void close(){
		this.running=false;
	}
}
