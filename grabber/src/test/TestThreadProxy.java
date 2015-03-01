package test;

import org.junit.Test;

import com.core.ThreadProxy;
public class TestThreadProxy {
	@Test
	public void testUpdate() {
//		ThreadProxy tp=new ThreadProxy("http://s.5173.com/dnf-0-0-0-0-2zkpio-0-0-0-a-a-a-a-a-1-0-0-0.shtml");
//		tp.update();
	}
	public static void main(String[] args) {
		ThreadProxy tp=new ThreadProxy("http://s.5173.com/dnf-0-0-0-0-2zkpio-0-0-0-a-a-a-a-a-0-0-0-0.shtml"
									  ,"http://s.5173.com/dnf-0-0-0-0-2zkpio-0-0-0-a-a-a-a-a-1-0-0-0.shtml");
		tp.update();
//		JSONObject jsonobj = new JSONObject("{id:test,msg:text}");  
//		String content = (String) jsonobj.get("id");
//		System.out.println("content="+content);
//		System.out.println(jsonobj.toString());
	}
}
