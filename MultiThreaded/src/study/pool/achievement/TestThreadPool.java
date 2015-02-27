package study.pool.achievement;

public class TestThreadPool {
	public TestThreadPool(int nThreads,int jops) throws InterruptedException {
		ThreadPoolManager pm = new ThreadPoolManager(nThreads);
		while(jops-->0){
			TestTask t = new TestTask();
			pm.addTask(t);
		}
		System.out.println("=======sleep(10000)==============");
		Thread.sleep(10000);
		System.out.println("=======begin==============\n\n\n\n\n");
		int count=1000;
		while(count++<1200){
			TestTask t = new TestTask();
			pm.addTask(t);
		}
	}
	private class TestTask implements Task{
		private boolean end=true;

		@Override
		public void setEnd(boolean end) {
			this.end=end;
		}

		@Override
		public boolean isEnd() {
			return this.end;
		}

		@Override
		public void startTask() {
			this.end=false;
			System.out.println("=============================");
			for (int i = 0; i < 5; i++) {
				System.out.println("task["+this.toString()+"]------>"+i);
			}
		}

		@Override
		public void endTask() {
			this.end=true;
		}
	}
	public static void main(String[] args) throws InterruptedException  {
		new TestThreadPool(10, 200);
	}
}
