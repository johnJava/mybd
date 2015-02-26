package study.thread;

public class WaitThread extends Thread {
	private int count=0;
	
	public synchronized void rego(){
		this.notify();
	}
	
	private synchronized void go(){
		while (true) {
			if(count==0){
				try {
					System.out.println("wait==============");
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			count--;
			System.out.println("count=="+count);
		}
	}

	@Override
	public void run() {
		go();
	}

	public void setCount(int count) {
		this.count = count;
	}

}
