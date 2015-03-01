package test;

class Test extends Thread{

	@Override
	public void run() {
//		while(true){
//			
//			System.out.println("begin---------->");
//		}
		for (int i = 0; i < 100; i++) {
			System.out.println("id---------->"+i);
		}
	}
	
}