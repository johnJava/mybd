package study.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainThead extends Thread {
	@Override
	public void run() {
		ExecutorService es =Executors.newCachedThreadPool();
		es.execute(new SubThread());
		es.execute(new SubThread());
		es.execute(new SubThread());
		es.execute(new SubThread());
		es.shutdown();
		try {
			boolean compelte = es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("compelte----------------->");
	}
}
