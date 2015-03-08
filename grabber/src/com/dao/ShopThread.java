package com.dao;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import org.json.JSONArray;
import org.json.JSONObject;

import com.common.GenericUtil;
import com.common.LogUtil;

public class ShopThread implements Runnable {
	private HashMap<String, String> cookies;
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:34.0) Gecko/20100101 Firefox/34.0";
	private JSONObject params;
	private String jsshopurl = "http://consignment.5173.com/PurchaseProcess/fillOrder.aspx?BizOfferId=&shopnum&&TradingServiceType=Consignment";
	private String ptshopurl = "http://danbao.5173.com/auction/buynew/PurchaseOrderNew.aspx?BizOfferId=&shopnum&";
	public static final int JSTYPE=1;
	public static final int PTTYPE=2;
	private int type;
	private String shopnum;

	public ShopThread(LoginSys login, JSONObject params,String shopnum,int type) {
		this.params = params;
		this.shopnum = shopnum;
		this.type = type;
		initCookies(login);
	}

	private void initCookies(LoginSys login) {
		HashMap<String, String> cookieMap = login.getCookies();
		this.cookies = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
			this.cookies.put(entry.getKey(), entry.getValue());
		}
	}

	public void run() {
		try {
			doShop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean doShop() throws Exception {
		boolean flag;
		if(ShopThread.JSTYPE==type){
			this.jsshopurl=this.jsshopurl.replaceAll("&shopnum&", shopnum);
			flag=doJSShop();
		}else {
			this.ptshopurl=this.ptshopurl.replaceAll("&shopnum&", shopnum);
			flag=doPTShop();
		}
		return flag;
		
	}
	private boolean doPTShop() throws Exception {
		LogUtil.infoPrintf("开始下单----------->");
		//loadCookie();
		boolean result = true;
		String postParams = addPTDynamicParams();
		HttpURLConnection loginConn = getHttpPostConn(this.ptshopurl);
		loginConn.setRequestProperty("Host", "danbao.5173.com");
		loginConn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
		LogUtil.infoPrintf("下单请求HEADER==="+loginConn.getRequestProperties());
		DataOutputStream wr = new DataOutputStream(loginConn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
		int responseCode = loginConn.getResponseCode();
		LogUtil.infoPrintf("\nSending 'POST' request to URL : " + this.ptshopurl);
		LogUtil.infoPrintf("Post parameters : " + postParams);
		LogUtil.infoPrintf("Response Code : " + responseCode);
		Map<String, List<String>> header = loginConn.getHeaderFields();
		LogUtil.singleDebugPrintf("下单回复HEADER===" + header);
		List<String> cookie = header.get("Set-Cookie");
		if (cookie == null || cookie.size() == 0) {
			result = false;
			LogUtil.infoPrintf("下单失败----------->");
		} else {
			LogUtil.infoPrintf("下单成功----------->");
			LogUtil.infoPrintf("cookie====" + cookie);
			LogUtil.singleDebugPrintf("Location :"+header.get("Location"));
			setCookies(cookie);
		}
		LogUtil.infoPrintf("下单完成----------->");
		//System.out.println(list.toHtml());
		return result;
	}
	private boolean doJSShop() throws Exception {
		LogUtil.infoPrintf("开始下单----------->");
		//loadCookie();
		boolean result = true;
		String postParams = addJSDynamicParams();
		HttpURLConnection loginConn = getHttpPostConn(this.jsshopurl);
		loginConn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
		LogUtil.infoPrintf("下单请求HEADER==="+loginConn.getRequestProperties());
		DataOutputStream wr = new DataOutputStream(loginConn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
		int responseCode = loginConn.getResponseCode();
		LogUtil.infoPrintf("\nSending 'POST' request to URL : " + this.jsshopurl);
		LogUtil.infoPrintf("Post parameters : " + postParams);
		LogUtil.infoPrintf("Response Code : " + responseCode);
		Map<String, List<String>> header = loginConn.getHeaderFields();
		LogUtil.singleDebugPrintf("下单回复HEADER===" + header);
		List<String> cookie = header.get("Set-Cookie");
		if (cookie == null || cookie.size() == 0) {
			result = false;
			LogUtil.infoPrintf("下单失败----------->");
		} else {
			LogUtil.infoPrintf("下单成功----------->");
			LogUtil.infoPrintf("cookie====" + cookie);
			LogUtil.singleDebugPrintf("Location :"+header.get("Location"));
			setCookies(cookie);
		}
		LogUtil.infoPrintf("下单完成----------->");
		//System.out.println(list.toHtml());
		return result;
	}

	
	private String addJSDynamicParams() throws Exception {
		HttpURLConnection conn = getHttpGetConn(this.jsshopurl);
		Parser parser = new Parser(conn);
		parser.setEncoding("UTF-8");
		NodeList list = parser.parse(null);
		Map<String, List<String>> header = conn.getHeaderFields();
		LogUtil.debugPrintf("conn.getHeaderFields():" + header);
		List<String> cookie = header.get("Set-Cookie");
		if (cookie == null || cookie.size() == 0) {
			LogUtil.infoPrintf("获取失败----------->");
		} else {
			LogUtil.infoPrintf("获取成功----------->");
			LogUtil.infoPrintf("cookie====" + cookie);
			setCookies(cookie);
		}
		
		StringBuffer dyparams = new StringBuffer(4000);
		NodeList a1ab = list.extractAllNodesThatMatch(new HasAttributeFilter("id", "0915324f09d34f42a6b45b9d4235a1ab"),true);
		InputTag alabinput = (InputTag) a1ab.elementAt(0);
		String value = alabinput.getAttribute("value");
		LogUtil.infoPrintf("alabinput:" + value);
		dyparams.append("0915324f09d34f42a6b45b9d4235a1ab=" + URLEncoder.encode(value, "utf-8"));
		/************************************************************/
		dyparams.append("&__EVENTTARGET=&__EVENTARGUMENT=");
		/************************************************************/
		NodeList __VIEWSTATE = list.extractAllNodesThatMatch(new HasAttributeFilter("id", "__VIEWSTATE"),true);
		InputTag stateinput = (InputTag) __VIEWSTATE.elementAt(0);
		value = stateinput.getAttribute("value");
		LogUtil.infoPrintf("stateinput:" + value);
		dyparams.append("&__VIEWSTATE=" + URLEncoder.encode(value, "utf-8"));
		

		NodeList scrilist = list.extractAllNodesThatMatch(new HasAttributeFilter("type", "text/javascript"),true);
		ScriptTag jstag = null;
		String hfVipKeFuId = "";
		for (int i = 0; i < scrilist.size(); i++) {
			jstag = (ScriptTag) scrilist.elementAt(i);
			if (jstag == null)
				continue;
			String scrstr = jstag.toPlainTextString();
			if (scrstr.contains("vipKefuJsonDatas")) {
				int beginindex = scrstr.indexOf("{");
				int endindex = scrstr.indexOf("};");
				scrstr = scrstr.substring(beginindex, endindex + 1);
				LogUtil.infoPrintf("jstag==" + scrstr);
				JSONObject jsonobj = new JSONObject(scrstr);
				JSONArray customers= (JSONArray) jsonobj.get(this.getParams().getString("customer"));//客服星级Star1或Star5
				hfVipKeFuId=(String) ((JSONObject)(customers.get(0))).get("Id");
				LogUtil.infoPrintf("hfVipKeFuId==" + hfVipKeFuId);
			}
		}
		
		/************************************************************/
		dyparams.append("&hfVipKeFuId="+hfVipKeFuId);
		dyparams.append("&header%24CommSearchBarNew5%24txtStuffCatagoryID=");
		dyparams.append("&header%24CommSearchBarNew5%24txtBizTypes=&header%24CommSearchBarNew5%24txtGameID=&header%24CommSearchBarNew5%24txtGameAreas=");
		dyparams.append("&header%24CommSearchBarNew5%24txtGameServers=");
		dyparams.append("&header%24CommSearchBarNew5%24txtGameBizType=");
		dyparams.append("&header%24CommSearchBarNew5%24txtSearchKeyWord=");
		dyparams.append("&header%24CommSearchBarNew5%24txtKeyword=%C7%EB%CA%E4%C8%EB%C8%CE%D2%E2%B9%D8%BC%FC%D7%D6");
		dyparams.append("&ddrQuantity=1");
		dyparams.append("&txtReOldRole=");
		dyparams.append("&txtReceivingRole="+this.getParams().getString("txtGameRole"));
		dyparams.append("&txtSureReceivingRole="+this.getParams().getString("txtGameRole"));
		dyparams.append("&txtReOldGameId=");
		dyparams.append("&txtRoleHierarchy="+this.getParams().getString("rolelevel"));
		dyparams.append("&bzon=rdbtnOffPostSaleIndemnity");
		dyparams.append("&txtPhone="+this.getParams().getString("txtBuyerTel"));
		dyparams.append("&txtQq="+this.getParams().getString("txtBuyerQQ"));
		dyparams.append("&hiddenBtnSureOk=");
		dyparams.append("&HiddenField3=");
		dyparams.append("&hdfHistoryType=");
		dyparams.append("&hd_OriginalUnitQuantityMoney=");
		dyparams.append("&hd_OriginalUnitPirce="+this.getParams().getString("price"));
		dyparams.append("&hd_gameRoleValue="+this.getParams().getString("txtGameRole"));
		/************************************************************/
		return dyparams.toString();
	}
	private String addPTDynamicParams() throws Exception {
		HttpURLConnection conn = getHttpGetConn(this.ptshopurl);
		conn.setRequestProperty("Host", "danbao.5173.com");
		Parser parser = new Parser(conn);
		parser.setEncoding("UTF-8");
		NodeList list = parser.parse(null);
		Map<String, List<String>> header = conn.getHeaderFields();
		LogUtil.infoPrintf("conn.getHeaderFields():" + header);
		List<String> cookie = header.get("Set-Cookie");
		if (cookie == null || cookie.size() == 0) {
			LogUtil.infoPrintf("获取失败----------->");
		} else {
			LogUtil.infoPrintf("获取成功----------->");
			LogUtil.infoPrintf("cookie====" + cookie);
			setCookies(cookie);
		}

		StringBuffer dyparams = new StringBuffer(4000);
		NodeList a1ab = list.extractAllNodesThatMatch(new HasAttributeFilter("id", "0915324f09d34f42a6b45b9d4235a1ab"),true);
		InputTag alabinput = (InputTag) a1ab.elementAt(0);
		String value = alabinput.getAttribute("value");
		LogUtil.infoPrintf("alabinput:" + value);
		dyparams.append("0915324f09d34f42a6b45b9d4235a1ab=" + URLEncoder.encode(value, "utf-8"));
		/************************************************************/
		dyparams.append("&__EVENTTARGET=&__EVENTARGUMENT=");
		/************************************************************/
		NodeList __VIEWSTATE = list.extractAllNodesThatMatch(new HasAttributeFilter("id", "__VIEWSTATE"),true);
		InputTag stateinput = (InputTag) __VIEWSTATE.elementAt(0);
		value = stateinput.getAttribute("value");
		LogUtil.infoPrintf("stateinput:" + value);
		dyparams.append("&__VIEWSTATE=" + URLEncoder.encode(value, "utf-8"));
		
		NodeList scrilist = list.extractAllNodesThatMatch(new HasAttributeFilter("type", "text/javascript"),true);
		ScriptTag jstag = null;
		String hide_VipKefuId = "";
		for (int i = 0; i < scrilist.size(); i++) {
			jstag = (ScriptTag) scrilist.elementAt(i);
			if (jstag == null)
				continue;
			String scrstr = jstag.toPlainTextString();
			if (scrstr.contains("vipKefuJsonDatas")) {
				int beginindex = scrstr.indexOf("{");
				int endindex = scrstr.indexOf("};");
				scrstr = scrstr.substring(beginindex, endindex + 1);
				LogUtil.infoPrintf("jstag==" + scrstr);
				JSONObject jsonobj = new JSONObject(scrstr);
				JSONArray customers= (JSONArray) jsonobj.get(this.getParams().getString("customer"));//客服星级Star1或Star5
				hide_VipKefuId=(String) ((JSONObject)(customers.get(0))).get("Id");
				LogUtil.infoPrintf("hide_VipKefuId==" + hide_VipKefuId);
			}
		}
		/************************************************************/
		dyparams.append("&SearchHeadNew1%24CommSearchBarNew5%24txtStuffCatagoryID=");
		dyparams.append("&SearchHeadNew1%24CommSearchBarNew5%24txtBizTypes=");
		dyparams.append("&SearchHeadNew1%24CommSearchBarNew5%24txtGameID=");
		dyparams.append("&SearchHeadNew1%24CommSearchBarNew5%24txtGameAreas=");
		dyparams.append("&SearchHeadNew1%24CommSearchBarNew5%24txtGameServers=");
		dyparams.append("&SearchHeadNew1%24CommSearchBarNew5%24txtGameBizType=");
		dyparams.append("&SearchHeadNew1%24CommSearchBarNew5%24txtSearchKeyWord=");
		dyparams.append("&SearchHeadNew1%24CommSearchBarNew5%24txtKeyword=%C7%EB%CA%E4%C8%EB%C8%CE%D2%E2%B9%D8%BC%FC%D7%D6");
		dyparams.append("&PurchaseOrderNew1%24hdGameMoney=");
		dyparams.append("&PurchaseOrderNew1%24hdPrice="+this.getParams().getString("price"));
		dyparams.append("&PurchaseOrderNew1%24hdDefFee=0.00");
		dyparams.append("&PurchaseOrderNew1%24QuantityInfo1%24ddlQuantity=1");
		dyparams.append("&PurchaseOrderNew1%24BuyerGameRoleInfo1%24txtGameRole="+this.getParams().getString("txtGameRole"));
		dyparams.append("&PurchaseOrderNew1%24BuyerGameRoleInfo1%24txtGameRoleValidate="+this.getParams().getString("txtGameRole"));
		dyparams.append("&PurchaseOrderNew1%24txtRoleGrade="+this.getParams().getString("rolelevel"));
		dyparams.append("&PurchaseOrderNew1%24hide_VipKefuId="+hide_VipKefuId);
		dyparams.append("&PurchaseOrderNew1%24rdPostSaleGroup=rdNoPostSale");
		dyparams.append("&PurchaseOrderNew1%24txtBuyerTel="+this.getParams().getString("txtBuyerTel"));
		dyparams.append("&PurchaseOrderNew1%24hdLastBuyerTel="+this.getParams().getString("txtBuyerTel"));
		dyparams.append("&PurchaseOrderNew1%24txtBuyerQQ="+this.getParams().getString("txtBuyerQQ"));
		dyparams.append("&PurchaseOrderNew1%24btnCreateOrder=%CC%E1%BD%BB%B2%E9%D1%AF");
		dyparams.append("&PurchaseOrderNew1%24cbAgreeLicense=on");
		/************************************************************/
		return dyparams.toString();
	}

	private HttpURLConnection getHttpPostConn(String url) throws Exception {
		LogUtil.infoPrintf("getHttpPostConn url===" + url);
		URL obj = new URL(url);
		HttpURLConnection conn;
		conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestMethod("POST");
		if (null != this.cookies) {
			conn.addRequestProperty("Cookie", GenericUtil.cookieFormat(this.cookies));
		}
		conn.setRequestProperty("Host", "consignment.5173.com");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		//conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		conn.setRequestProperty("Referer",url);
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		return conn;
	}

	private HttpURLConnection getHttpGetConn(String url) throws Exception {
		LogUtil.infoPrintf("getHttpGetConn url===" + url);
		HttpURLConnection conn;
		// Acts like a browser
		URL obj = new URL(url);
		conn = (HttpURLConnection) obj.openConnection();
		if (null != this.cookies) {
			conn.addRequestProperty("Cookie", GenericUtil.cookieFormat(this.cookies));
		}
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Host", "consignment.5173.com");
		conn.setRequestProperty("Referer", url);
		System.out.println(conn.getRequestProperties());
		return conn;
	}

	private void addParam(String key, String value) {
		this.params.put(key, value);
	}


	private JSONObject getParams() {
		return params;
	}


	private void setCookies(List<String> cookies) {
		for (String cookie : cookies) {
			String[] params = cookie.split(";");
			for (int i = 0; i < params.length; i++) {
				String param = params[i];
				if (param.contains("=")) {
					String[] kv = param.split("=");
					this.cookies.put(kv[0], kv[1]);
				} else {
					this.cookies.put(param, "");
				}
				LogUtil.infoPrintf("add cookie==" + param);
			}
		}
		LogUtil.infoPrintf("this.cookies==" + this.cookies.toString());
	}

	public void clear() {
		this.cookies = null;
		this.params = null;
	}

//	private String getParams() {
//		StringBuffer parambuff = new StringBuffer(2000);
//		Iterator<String> it = this.params.keys();
//		while (it.hasNext()) {
//			String key = it.next();
//			parambuff.append(key + "=" + this.params.getString(key));
//			parambuff.append("&");
//		}
//		if (parambuff.indexOf("&") != -1)
//			parambuff.deleteCharAt(parambuff.lastIndexOf("&"));
//		return parambuff.toString();
//	}
}
