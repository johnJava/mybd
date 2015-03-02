package test;


import java.io.File;

import com.common.MyX509TrustManager;

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
	public static void main(String[] args) throws Exception {
//		File  f=new File((new Test()).getClass().getResource("/").getPath());
//		System.out.println(f.isDirectory());
		test();
	}
	public static void test() throws Exception {
		new MyX509TrustManager();
	}
}