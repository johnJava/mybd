package com.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.common.GenericUtil;
import com.common.LogUtil;
import com.common.MyX509TrustManager;
import com.web.EquMessageService;

public class ShopThread implements Runnable {
	private HashMap<String, String> cookies;
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:34.0) Gecko/20100101 Firefox/34.0";
	private JSONObject params;
	private String jsshopurl = "http://consignment.5173.com/PurchaseProcess/fillOrder.aspx?BizOfferId=&shopnum&&TradingServiceType=Consignment";
	private String ptshopurl = "http://danbao.5173.com/auction/buynew/PurchaseOrderNew.aspx?BizOfferId=&shopnum&";
	public static final int JSTYPE = 1;
	public static final int PTTYPE = 2;
	private int type;
	private String shopnum;

	public ShopThread(LoginSys login, JSONObject params, String shopnum,
			int type) {
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
		if (ShopThread.JSTYPE == type) {
			this.jsshopurl = this.jsshopurl.replaceAll("&shopnum&", shopnum);
			flag = doJSShop();
		} else {
			this.ptshopurl = this.ptshopurl.replaceAll("&shopnum&", shopnum);
			flag = doPTShop();
		}
		return flag;

	}

	private boolean doPTShop() throws Exception {
		LogUtil.infoPrintf("开始下单----------->");
		// loadCookie();
		boolean result = true;
		String postParams = addPTDynamicParams();
		HttpURLConnection loginConn = getHttpPostConn(this.ptshopurl);
		loginConn.setRequestProperty("Host", "danbao.5173.com");
		loginConn.setRequestProperty("Content-Length",
				Integer.toString(postParams.length()));
		LogUtil.infoPrintf("下单请求HEADER===" + loginConn.getRequestProperties());
		DataOutputStream wr = new DataOutputStream(loginConn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
		int responseCode = loginConn.getResponseCode();
		LogUtil.infoPrintf("\nSending 'POST' request to URL : "
				+ this.ptshopurl);
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
			LogUtil.singleDebugPrintf("Location :"+ loginConn.getURL().toString());
			setCookies(cookie);
			String payurl = getPayKey(loginConn,"db");
			if(payurl!=null&&!payurl.equals("")){
				LogUtil.debugPrintf("获取支付url:"+payurl);
				pay(payurl);
			}else{
				LogUtil.debugPrintf("获取支付url失败");
			}
		}
		LogUtil.infoPrintf("下单完成----------->");
		// System.out.println(list.toHtml());
		return result;
	}

	private boolean doJSShop() throws Exception {
		LogUtil.infoPrintf("开始下单----------->");
		// loadCookie();
		boolean result = true;
		String postParams = addJSDynamicParams();
		HttpURLConnection loginConn = getHttpPostConn(this.jsshopurl);
		loginConn.setRequestProperty("Content-Length",
				Integer.toString(postParams.length()));
		LogUtil.infoPrintf("下单请求HEADER===" + loginConn.getRequestProperties());
		DataOutputStream wr = new DataOutputStream(loginConn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
		int responseCode = loginConn.getResponseCode();
		LogUtil.infoPrintf("\nSending 'POST' request to URL : "
				+ this.jsshopurl);
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
			LogUtil.singleDebugPrintf("Location :"+ loginConn.getURL().toString());
			setCookies(cookie);
			String payurl = getPayKey(loginConn,"js");
			if(payurl!=null&&!payurl.equals("")){
				LogUtil.debugPrintf("获取支付url:"+payurl);
				pay(payurl);
			}else{
				LogUtil.debugPrintf("获取支付url失败");
			}
		}
		LogUtil.infoPrintf("下单完成----------->");
		// System.out.println(list.toHtml());
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
		NodeList a1ab = list.extractAllNodesThatMatch(new HasAttributeFilter(
				"id", "0915324f09d34f42a6b45b9d4235a1ab"), true);
		InputTag alabinput = (InputTag) a1ab.elementAt(0);
		String value = alabinput.getAttribute("value");
		LogUtil.infoPrintf("alabinput:" + value);
		dyparams.append("0915324f09d34f42a6b45b9d4235a1ab="
				+ URLEncoder.encode(value, "utf-8"));
		/************************************************************/
		dyparams.append("&__EVENTTARGET=&__EVENTARGUMENT=");
		/************************************************************/
		NodeList __VIEWSTATE = list.extractAllNodesThatMatch(
				new HasAttributeFilter("id", "__VIEWSTATE"), true);
		InputTag stateinput = (InputTag) __VIEWSTATE.elementAt(0);
		value = stateinput.getAttribute("value");
		LogUtil.infoPrintf("stateinput:" + value);
		dyparams.append("&__VIEWSTATE=" + URLEncoder.encode(value, "utf-8"));

		NodeList scrilist = list.extractAllNodesThatMatch(
				new HasAttributeFilter("type", "text/javascript"), true);
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
				JSONArray customers = (JSONArray) jsonobj.get(this.getParams()
						.getString("customer"));// 客服星级Star1或Star5
				hfVipKeFuId = (String) ((JSONObject) (customers.get(0)))
						.get("Id");
				LogUtil.infoPrintf("hfVipKeFuId==" + hfVipKeFuId);
			}
		}

		/************************************************************/
		dyparams.append("&hfVipKeFuId=" + hfVipKeFuId);
		dyparams.append("&header%24CommSearchBarNew5%24txtStuffCatagoryID=");
		dyparams.append("&header%24CommSearchBarNew5%24txtBizTypes=&header%24CommSearchBarNew5%24txtGameID=&header%24CommSearchBarNew5%24txtGameAreas=");
		dyparams.append("&header%24CommSearchBarNew5%24txtGameServers=");
		dyparams.append("&header%24CommSearchBarNew5%24txtGameBizType=");
		dyparams.append("&header%24CommSearchBarNew5%24txtSearchKeyWord=");
		dyparams.append("&header%24CommSearchBarNew5%24txtKeyword=%C7%EB%CA%E4%C8%EB%C8%CE%D2%E2%B9%D8%BC%FC%D7%D6");
		dyparams.append("&ddrQuantity=1");
		dyparams.append("&txtReOldRole=");
		dyparams.append("&txtReceivingRole="+ this.getParams().getString("txtGameRole"));
		dyparams.append("&txtSureReceivingRole="+ this.getParams().getString("txtGameRole"));
		dyparams.append("&txtReOldGameId=");
		dyparams.append("&txtRoleHierarchy="+ this.getParams().getString("rolelevel"));
		dyparams.append("&bzon=rdbtnOffPostSaleIndemnity");
		dyparams.append("&txtPhone="+ this.getParams().getString("txtBuyerTel"));
		dyparams.append("&txtQq=" + this.getParams().getString("txtBuyerQQ"));
		dyparams.append("&hiddenBtnSureOk=");
		dyparams.append("&HiddenField3=");
		dyparams.append("&hdfHistoryType=");
		dyparams.append("&hd_OriginalUnitQuantityMoney=");
		dyparams.append("&hd_OriginalUnitPirce="+ this.getParams().getString("price"));
		dyparams.append("&hd_gameRoleValue="+ this.getParams().getString("txtGameRole"));
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
		NodeList a1ab = list.extractAllNodesThatMatch(new HasAttributeFilter("id", "0915324f09d34f42a6b45b9d4235a1ab"), true);
		InputTag alabinput = (InputTag) a1ab.elementAt(0);
		String value = alabinput.getAttribute("value");
		LogUtil.infoPrintf("alabinput:" + value);
		dyparams.append("0915324f09d34f42a6b45b9d4235a1ab="
				+ URLEncoder.encode(value, "utf-8"));
		/************************************************************/
		dyparams.append("&__EVENTTARGET=&__EVENTARGUMENT=");
		/************************************************************/
		NodeList __VIEWSTATE = list.extractAllNodesThatMatch(new HasAttributeFilter("id", "__VIEWSTATE"), true);
		InputTag stateinput = (InputTag) __VIEWSTATE.elementAt(0);
		value = stateinput.getAttribute("value");
		LogUtil.infoPrintf("stateinput:" + value);
		dyparams.append("&__VIEWSTATE=" + URLEncoder.encode(value, "utf-8"));
		NodeList scrilist = list.extractAllNodesThatMatch(new HasAttributeFilter("type", "text/javascript"), true);
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
				JSONArray customers = (JSONArray) jsonobj.get(this.getParams()
						.getString("customer"));// 客服星级Star1或Star5
				hide_VipKefuId = (String) ((JSONObject) (customers.get(0)))
						.get("Id");
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
		dyparams.append("&PurchaseOrderNew1%24hdPrice="+ this.getParams().getString("price"));
		dyparams.append("&PurchaseOrderNew1%24hdDefFee=0.00");
		dyparams.append("&PurchaseOrderNew1%24QuantityInfo1%24ddlQuantity=1");
		dyparams.append("&PurchaseOrderNew1%24BuyerGameRoleInfo1%24txtGameRole="+ this.getParams().getString("txtGameRole"));
		dyparams.append("&PurchaseOrderNew1%24BuyerGameRoleInfo1%24txtGameRoleValidate="+ this.getParams().getString("txtGameRole"));
		dyparams.append("&PurchaseOrderNew1%24txtRoleGrade="+ this.getParams().getString("rolelevel"));
		dyparams.append("&PurchaseOrderNew1%24hide_VipKefuId=" + hide_VipKefuId);
		dyparams.append("&PurchaseOrderNew1%24rdPostSaleGroup=rdNoPostSale");
		dyparams.append("&PurchaseOrderNew1%24txtBuyerTel="+ this.getParams().getString("txtBuyerTel"));
		dyparams.append("&PurchaseOrderNew1%24hdLastBuyerTel="+ this.getParams().getString("txtBuyerTel"));
		dyparams.append("&PurchaseOrderNew1%24txtBuyerQQ="+ this.getParams().getString("txtBuyerQQ"));
		dyparams.append("&PurchaseOrderNew1%24btnCreateOrder=%CC%E1%BD%BB%B2%E9%D1%AF");
		dyparams.append("&PurchaseOrderNew1%24cbAgreeLicense=on");
		/************************************************************/
		return dyparams.toString();
	}
	
	private void pay(String payurl){
		LogUtil.debugPrintf("开始支付----------->");
		try {
			String postParams = getPayDynamicParams(payurl);
			HttpsURLConnection loginConn = getHttpSConn(payurl,"POST");
			if (null != this.cookies) {
				loginConn.addRequestProperty("Cookie",
						GenericUtil.cookieFormat(this.cookies));
			}
			LogUtil.infoPrintf("支付请求HEADER===" + loginConn.getRequestProperties());
			DataOutputStream wr = new DataOutputStream(loginConn.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
			int responseCode = loginConn.getResponseCode();
			LogUtil.infoPrintf("\nSending 'POST' request to URL : " +payurl );
			LogUtil.infoPrintf("Post parameters : " + postParams);
			LogUtil.infoPrintf("Response Code : " + responseCode);
			Map<String, List<String>> header = loginConn.getHeaderFields();
			LogUtil.infoPrintf("conn.getHeaderFields():" + header);
			BufferedReader in = new BufferedReader(new InputStreamReader(loginConn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer(2000);
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			String payresponse = response.toString();
			LogUtil.infoPrintf("payresponse:"+payresponse);
			List<String> cookie = header.get("Set-Cookie");
			String loginInfo;
			if (cookie == null || cookie.size() == 0) {
				LogUtil.infoPrintf("支付失败----------->");
				loginInfo="支付失败";
			} else {
				LogUtil.infoPrintf("支付成功----------->");
				LogUtil.debugPrintf("cookie====" + cookie);
				setCookies(cookie);
				loginInfo="支付成功";
			}
			EquMessageService.inbound.send(loginInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private HttpsURLConnection getHttpSConn(String payurl,String method) throws Exception {
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		// Acts like a browser
		URL obj = new URL(payurl);
		HttpsURLConnection conn;
		conn = (HttpsURLConnection) obj.openConnection();
		conn.setSSLSocketFactory(ssf);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Host", "mypay.5173.com");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		conn.setRequestProperty("Referer", payurl);
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Pragma", "no-cache");
		conn.setRequestProperty("Cache-Control", "no-cache");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		return conn;
	}
	//private String token="e80e3786edcd40848a2d606d4c6f6cdce80e3786edcd40848a2d606d4c6f6cdc";
	private String getPayDynamicParams(String payurl) throws Exception {
		LogUtil.infoPrintf("获取支付参数...");
		StringBuffer dyparams = new StringBuffer(4000);
		HttpURLConnection conn = getHttpGetConn(payurl);
		String token;
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					synchronized (token) {
						token=getToken();
//						token.notifyAll();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
//		}).start();
		conn.setRequestProperty("Referer", payurl);
		Parser parser = new Parser(conn);
		parser.setEncoding("UTF-8");
		NodeList list = parser.parse(null);
		dyparams.append("0915324f09d34f42a6b45b9d4235a1ab="+ URLEncoder.encode(getValueById(list, "0915324f09d34f42a6b45b9d4235a1ab"), "utf-8"));
		dyparams.append("&__EVENTTARGET=");
		dyparams.append("&__EVENTARGUMENT=");
		dyparams.append("&__VIEWSTATE="+ URLEncoder.encode(getValueById(list, "__VIEWSTATE"), "utf-8"));
		dyparams.append("&__EVENTVALIDATION="+ URLEncoder.encode(getValueById(list, "__EVENTVALIDATION"), "utf-8"));
		dyparams.append("&PayDirectlyBalance1%24ddlCoupon=-1");
		dyparams.append("&PayDirectlyBalance1%24hdnSumPrice="+getValueById(list, "PayDirectlyBalance1_hdnSumPrice"));
		dyparams.append("&PayDirectlyAuthType1%24ddlSecurityAnswer=%C4%FA%B8%B8%C7%D7%B5%C4%C3%FB%D7%D6%CA%C7%A3%BF");
		dyparams.append("&PayDirectlyAuthType1%24txtSecurityAnswer=%CD%F5%B9%E3%B1%F3");
		dyparams.append("&btnAffirmPay=");
		dyparams.append("&rdoDebitCard="+getValueByName(list, "rdoDebitCard"));
		dyparams.append("&rdoCreditCard="+getValueByName(list, "rdoCreditCard"));
		dyparams.append("&rdoThirdPay="+getValueByName(list, "rdoThirdPay"));
		dyparams.append("&OverSea="+getValueByName(list, "OverSea"));
		dyparams.append("&PayDirectlyBank1%24ddlCurrency=5adaf85c9fec4cf3b05766358c36c71a");
		dyparams.append("&ScanCode="+getValueByName(list, "ScanCode"));
		dyparams.append("&PayDirectlyBank1%24hdnBankParam=");
//		synchronized (token) {
//			if("e80e3786edcd40848a2d606d4c6f6cdce80e3786edcd40848a2d606d4c6f6cdc".equalsIgnoreCase(token)){
//				LogUtil.debugPrintf("wait token...");
//				token.wait();
//			}
//		}
		dyparams.append("&__validationToken__="+token);
		dyparams.append("&__validationValue__=");
		dyparams.append("&__validationDna__=");
		LogUtil.infoPrintf("支付参数："+dyparams.toString());
		return dyparams.toString();
	}
	private String getToken() throws Exception{
		String payurl="https://security.5173.com/Security/ClientBroker/5173MyPayDeduction";
		HttpsURLConnection conn = getHttpSConn(payurl);
		conn.setRequestProperty("Referer", payurl);
		Parser parser = new Parser(conn);
		parser.setEncoding("UTF-8");
		NodeList list = parser.parse(null);
		String token=getValueById(list, "__validationToken__");
		return (null==token)?"":token;
	}

	private String getValueById(NodeList list,String id){
		LogUtil.infoPrintf("id=="+id);
		NodeList a1ab = list.extractAllNodesThatMatch(new HasAttributeFilter("id", id), true);
		InputTag alabinput = (InputTag) a1ab.elementAt(0);
		String value = alabinput.getAttribute("value");
		LogUtil.infoPrintf(id+":" + value);
		return value;
	}
	private String getValueByName(NodeList list,String name){
		LogUtil.infoPrintf("name=="+name);
		NodeList a1ab = list.extractAllNodesThatMatch(new HasAttributeFilter("name",name), true);
		InputTag alabinput = (InputTag) a1ab.elementAt(0);
		String value = alabinput.getAttribute("value");
		LogUtil.infoPrintf("value:" + value);
		return value;
	}
	
	private String getPayKey(HttpURLConnection conn,String type) throws Exception {
		LogUtil.infoPrintf("开始自动付款");
		String keyurl = conn.getURL().toString();
		String postParams = getKeyDynamicParams(keyurl,type);
		HttpURLConnection loginConn = getHttpPostConn(keyurl);
		loginConn.setRequestProperty("Accept-Encoding", "deflate");// 注意压缩方式
		loginConn.setRequestProperty("Referer", keyurl);
		loginConn.setRequestProperty("Content-Length",Integer.toString(postParams.length()));
		LogUtil.debugPrintf("请求获取key的HEADER===" + loginConn.getRequestProperties());
		LogUtil.debugPrintf("请求获取key的postParams===" + postParams);
		DataOutputStream wr = new DataOutputStream(loginConn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(loginConn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer(2000);
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		String payurl = response.toString();
		LogUtil.infoPrintf("keyHtml:"+payurl);
		payurl=payurl.substring(payurl.indexOf("action")+"action".length(),payurl.indexOf("></form>"));
		payurl=payurl.substring(payurl.indexOf("\"")+1, payurl.lastIndexOf("\""));
		Map<String, List<String>> header = loginConn.getHeaderFields();
		LogUtil.singleDebugPrintf("获取key的回复HEADER===" + header);
		List<String> cookie = header.get("Set-Cookie");
		if (cookie == null || cookie.size() == 0) {
			LogUtil.infoPrintf("获取key有cookie----------->");
		} else {
			LogUtil.infoPrintf("获取key没有cookie----------->");
			LogUtil.infoPrintf("cookie====" + cookie);
			setCookies(cookie);
		}
		LogUtil.infoPrintf("获取key完成----------->");
		return payurl;
	}

	private String getKeyDynamicParams(String keyurl,String type) throws Exception {
		LogUtil.infoPrintf("获取key的参数...");
		StringBuffer dyparams = new StringBuffer(4000);
		HttpURLConnection conn = getHttpGetConn(keyurl);
		conn.setRequestProperty("Referer", keyurl);
		Parser parser = new Parser(conn);
		parser.setEncoding("UTF-8");
		NodeList list = parser.parse(null);
		String value = getValueById(list, "0915324f09d34f42a6b45b9d4235a1ab");
		LogUtil.infoPrintf("alabinput:" + value);
		dyparams.append("0915324f09d34f42a6b45b9d4235a1ab="+ URLEncoder.encode(value, "utf-8"));
		value = getValueById(list, "__VIEWSTATE");
		LogUtil.infoPrintf("stateinput:" + value);
		dyparams.append("&__VIEWSTATE=" + URLEncoder.encode(value, "utf-8"));
		dyparams.append("&hdStatus=");
		if("js".equalsIgnoreCase(type)){
			value = getValueById(list, "__EVENTVALIDATION");
			LogUtil.infoPrintf("eventinput:" + value);
			dyparams.append("&__EVENTVALIDATION="+ URLEncoder.encode(value, "utf-8"));
			dyparams.append("&hiddenBtnGoPayfor=");
		}else{
			dyparams.append("&__EVENTTARGET=&__EVENTARGUMENT=");
			dyparams.append("&btnGoPayfor=%CC%E1%BD%BB%B2%E9%D1%AF");
		}
		LogUtil.infoPrintf("获取key的参数:"+dyparams.toString());
		return dyparams.toString();
	}

	private HttpURLConnection getHttpPostConn(String url) throws Exception {
		LogUtil.infoPrintf("getHttpPostConn url===" + url);
		URL obj = new URL(url);
		HttpURLConnection conn;
		conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestMethod("POST");
		if (null != this.cookies) {
			conn.addRequestProperty("Cookie",
					GenericUtil.cookieFormat(this.cookies));
		}
		conn.setRequestProperty("Host", "consignment.5173.com");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language",
				"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		// conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		conn.setRequestProperty("Referer", url);
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		return conn;
	}

	private HttpsURLConnection getHttpSConn(String httpsurl) throws Exception {
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		// Acts like a browser
		URL obj = new URL(httpsurl);
		HttpsURLConnection conn;
		conn = (HttpsURLConnection) obj.openConnection();
		conn.setSSLSocketFactory(ssf);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Host", "security.5173.com");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Pragma", "no-cache");
		conn.setRequestProperty("Cache-Control", "no-cache");
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
			conn.addRequestProperty("Cookie",
					GenericUtil.cookieFormat(this.cookies));
		}
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language",
				"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
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
					if(kv.length<2){
						this.cookies.put(param, "");
					}else{
						this.cookies.put(kv[0], kv[1]);
					}
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

	// private String getParams() {
	// StringBuffer parambuff = new StringBuffer(2000);
	// Iterator<String> it = this.params.keys();
	// while (it.hasNext()) {
	// String key = it.next();
	// parambuff.append(key + "=" + this.params.getString(key));
	// parambuff.append("&");
	// }
	// if (parambuff.indexOf("&") != -1)
	// parambuff.deleteCharAt(parambuff.lastIndexOf("&"));
	// return parambuff.toString();
	// }
}
