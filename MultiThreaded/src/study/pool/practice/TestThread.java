package study.pool.practice;

public class TestThread extends Thread {

	@Override
	public void run() {
		int count=100;
		while(count-->0){
			System.out.println(Thread.currentThread().getName()+"["+count+"]");
		}
	}

}
