package study.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThread {
	public static void main(String[] args) {
		ThreadPoolExecutor threadsPool = new ThreadPoolExecutor(18, 100, 4, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	}
}
