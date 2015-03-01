package test;

import org.json.JSONObject;

import com.common.GenericUtil;
import com.dao.LoginSys;

public class TestLoginSys {

	public static void main(String[] args) throws Exception {
		JSONObject loginInfo = new JSONObject("{username:'wlg7766',password:'54054110'}");
		LoginSys login = new LoginSys(loginInfo);
		if(login.doLogin()){
			 System.out.println("cookie==="+GenericUtil.cookieFormat(login.getCookies()));
		}
	}

}
