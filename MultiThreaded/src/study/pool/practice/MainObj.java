package study.pool.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainObj {
	ExecutorService es=null;
	public void start() {
		es =Executors.newCachedThreadPool();
		es.execute(new SubThread());
		es.execute(new SubThread());
		es.execute(new SubThread());
		es.execute(new SubThread());
		//es.shutdown();
	}

	public void compelet() {
		try {
			boolean compelte = es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName()+" compelte----------------->");
	}
}
