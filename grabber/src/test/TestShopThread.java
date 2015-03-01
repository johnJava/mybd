package test;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.junit.Test;

import com.dao.LoginSys;
import com.dao.ShopThread;

public class TestShopThread {

	public static void main(String[] args) throws Exception {
		JSONObject loginInfo = new JSONObject("{username:'wlg7766',password:'54054110'}");
		LoginSys login = new LoginSys(loginInfo);
		//login.doLogin();
		if(login.doLogin()){
			System.out.println("cookie:"+login.getCookies());
			JSONObject params= new JSONObject();
			params.put("price", "1000.00");
			params.put("txtGameRole", "2000");
			params.put("rolelevel", "100");
			params.put("txtBuyerTel", "13562567172");
			params.put("txtBuyerQQ", "179627981");
			params.put("customer", "Star1");
			ShopThread st =new ShopThread(login,params,"JS064-20150207-71198871",ShopThread.JSTYPE);
			new Thread(st).start();
		}
	}
	@Test
	public void testspliet() throws UnsupportedEncodingException, ParseException{
		//String value ="OBUu8{line}wTWq8{plus}l6Is3khDXY9{line}xm48pH7KIJ4DCEetlFfyWOWnvfsc7Q{equel}{equel}";
		long time = System.currentTimeMillis();
		String tim = new SimpleDateFormat("HH:mm:ss").format(time);
		Date da = new SimpleDateFormat("HH:mm:ss").parse(tim);
		time=da.getHours()*3600+da.getMinutes()*60+da.getSeconds();
		System.out.println(	time*10+":"+tim);
//		String value ="";
//		System.out.println(URLEncoder.encode(value, "utf-8"));
	}

}
