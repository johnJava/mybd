package study.pool.achievement;

import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPoolManager {
	private LinkedBlockingQueue<Task> taskquque;
	private Vector<WorkThread> works;
	private int nThreads;
	private final static int DEFALUT_NTHREADS = 10;

	public ThreadPoolManager() {
		this(DEFALUT_NTHREADS);
	}

	public ThreadPoolManager(int nThreads) {
		init(nThreads);
	}

	private void init(int nThreads) {
		this.nThreads = nThreads;
		taskquque = new LinkedBlockingQueue<Task>();
		works = new Vector<WorkThread>(this.nThreads);
		for (int i = 0; i < this.nThreads; i++) {
			WorkThread work = new WorkThread(taskquque);
			works.add(work);
		}
	}

	public void addTask(Task t) throws InterruptedException {
		synchronized (this.taskquque) {
			this.taskquque.put(t);
			this.taskquque.notifyAll();
		}
	}

	public void shutDown() {
		for (int i = 0; i < this.nThreads; i++) {
			WorkThread work = works.get(i);
			work.close();
		}
	}
}
