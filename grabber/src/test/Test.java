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
		String html="<html><head></head><body onload=\"document.FormPayfor.submit()\"><form name=\"FormPayfor\" method=\"Post\" action=\"https://mypay.5173.com/payorder/paydirectly.aspx?orderId=DB20150312579723330&key=F94BBB66C4E421E5A02CBDAD7D8406CF\" ></form></body><html>";
		html=html.substring(html.indexOf("action")+"action".length(),html.indexOf("></form>"));
		html=html.substring(html.indexOf("\"")+1, html.lastIndexOf("\""));
		System.out.println(html);
		//		File  f=new File((new Test()).getClass().getResource("/").getPath());
//		System.out.println(f.isDirectory());
		//test();
	}
	public static void test() throws Exception {
		new MyX509TrustManager();
	}
}