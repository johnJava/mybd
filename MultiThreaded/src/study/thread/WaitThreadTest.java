package study.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

public class WaitThreadTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		test();
	    
//	    th.setCount(10);
//	    th.start();
//	    BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));  
//        System.out.print("请输入一个字符串：");  
//        String str = strin.readLine();  
//        System.out.println("第一个："+str); 
//        th.setCount(10);
//        th.notify();
//        System.out.println("请输入第二个字符串：");  
//        String str2 = strin.readLine();  
//        System.out.println("第2个："+str2);  
//        th.setCount(10);
//        th.notify();
	}

	public synchronized static void test(){
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		long startTime = System.currentTimeMillis();
		System.out.println("开始[" + format.format(startTime)+ "]");
		WaitThread th = new WaitThread();
		int count=1;
		th.setCount(count);
		th.start();
		int limit=100;
		while(limit-->0){
			try {
				System.out.println("sleep=========");
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			th.setCount(count);
			th.rego();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("结束[" + format.format(endTime)+ "]耗时[" + (endTime - startTime) + "毫秒]");
	}
}
