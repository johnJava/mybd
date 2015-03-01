package test;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpUrlConnectionExample {

  private List<String> cookies;
  private static HttpsURLConnection conn;

  //private final String USER_AGENT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:34.0) Gecko/20100101 Firefox/34.0";

  public static void main(String[] args) throws Exception {

    String url = "https://passport.5173.com/";
   // String url = "https://passport.csdn.net/";
    //String url = "https://a.5173.com/__tra.gif?referrer=&r=0.8926788993015564&url=https%3A%2F%2Fpassport.5173.com%2F&domain=passport.5173.com&title=%E7%99%BB%E5%BD%95%7C5173.com%7C%E8%A3%85%E5%A4%87%E4%BA%A4%E6%98%93%E6%B8%B8%E6%88%8F%E5%B8%81%E4%BA%A4%E6%98%93%E5%B8%90%E5%8F%B7%E4%BA%A4%E6%98%93%7C%E5%AF%84%E5%94%AE%E6%8B%85%E4%BF%9D%E4%BA%A4%E6%98%93%7C%E6%B8%B8%E6%88%8F%E7%82%B9%E5%8D%A1%7C%E4%BB%A3%E7%BB%83%E4%B8%AD%E4%BB%8B%7C%E5%B0%8F%E9%A2%9D%E4%BA%A4%E6%98%93&height=800&width=1280&colorDepth=32&language=&agent=Mozilla%2F4.0%20(compatible%3B%20MSIE%209.0%3B%20Windows%20NT%206.1%3B%20Trident%2F5.0%3B%20SLCC2%3B%20.NET%20CLR%202.0.50727%3B%20.NET%20CLR%203.5.30729%3B%20.NET%20CLR%203.0.30729%3B%20Media%20Center%20PC%206.0%3B%20.NET4.0C%3B%20.NET4.0E)";
    String gmail = "https://mail.google.com/mail/";

    HttpUrlConnectionExample http = new HttpUrlConnectionExample();

    // make sure cookies is turn on
    CookieHandler.setDefault(new CookieManager());

    // 1. Send a "GET" request, so that you can extract the form's data.
    //String page = http.GetPageContent(url);
//    String postParams = http.getFormParams(page, "username@gmail.com", "password");
    //String postParams = "userName=wlg7766&password=374e1a456f7fcd3a2935cfacfe180a05&passpod=&captcha=&category=&__validationToken__=3b6d01d352b0450a8c2ac52fdf0d2937&__validationDna__=";
   // String postParams = "userName=wlg7766&password=374e1a456f7fcd3a2935cfacfe180a05&passpod=&captcha=&category="+getTokenAndPwd()+"&__validationDna__=";
    String postParams = "userName=wlg7766"+getTokenAndPwd()+"&__validationDna__=";
    //String postParams ="userName=wlg7766&password=915a18a042c7176c25f80615fbe40e81&passpod=&captcha=b2xb&category=&__validationToken__=8e0b8ba5e9bd4fe4aef867ea19edf52e&__validationDna__=";
    //
//    // 2. Construct above post's content and then send a POST request for
//    // authentication
    http.sendPost(url, postParams);
//
//    // 3. success then go to gmail.
//    String result = http.GetPageContent(gmail);
//    System.out.println(result);
  }

  private void sendPost(String url, String postParams) throws Exception {

    URL obj = new URL(url);
    conn = (HttpsURLConnection) obj.openConnection();

    // Acts like a browser
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Host","passport.5173.com");
    conn.setRequestProperty("User-Agent", USER_AGENT);
    conn.setRequestProperty("Accept","*/*");
    conn.setRequestProperty("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
    conn.setRequestProperty("Accept-Encoding","gzip, deflate");
    conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
    conn.setRequestProperty("X-Requested-With","XMLHttpRequest");
    conn.setRequestProperty("Referer","https://passport.5173.com/");
    conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
    conn.setRequestProperty("Connection", "keep-alive");
    conn.setRequestProperty("Pragma","no-cache");
    conn.setRequestProperty("Cache-Control","no-cache");
    conn.setDoOutput(true);
    conn.setDoInput(true);
    if(null!=this.cookies){
    	for (String cookie : this.cookies) {
    		conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
    	}
    }

    // Send post request
    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
    wr.writeBytes(postParams);
    wr.flush();
    wr.close();

    int responseCode = conn.getResponseCode();
   
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + postParams);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in =new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();
    // System.out.println(response.toString());
    System.out.println("conn.getHeaderFields():"+conn.getHeaderFields());
  }

  private String GetPageContent(String url) throws Exception {
	// 创建SSLContext对象，并使用我们指定的信任管理器初始化
	TrustManager[] tm = { new MyX509TrustManager() };
	SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
	sslContext.init(null, tm, new java.security.SecureRandom());
	// 从上述SSLContext对象中得到SSLSocketFactory对象
	SSLSocketFactory ssf = sslContext.getSocketFactory();
	  
	  
    URL obj = new URL(url);
    conn = (HttpsURLConnection) obj.openConnection();
    
    conn.setSSLSocketFactory(ssf);

    // default is GET
    conn.setRequestMethod("GET");

    conn.setUseCaches(false);

    // act like a browser
    conn.setRequestProperty("User-Agent", USER_AGENT);
    conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
    if (cookies != null) {
        for (String cookie : this.cookies) {
            conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
        }
    }

    BufferedReader in =
            new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    int responseCode = conn.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();

    // Get the response cookies
    setCookies(conn.getHeaderFields().get("Set-Cookie"));
    System.out.println("conn.getHeaderFields():"+conn.getHeaderFields());
    return response.toString();

  }

  public String getFormParams(String html, String username, String password)
        throws UnsupportedEncodingException {

    System.out.println("Extracting form's data...");

    Document doc = Jsoup.parse(html);

    // Google form id
    Element loginform = doc.getElementById("gaia_loginform");
    Elements inputElements = loginform.getElementsByTag("input");
    List<String> paramList = new ArrayList<String>();
    for (Element inputElement : inputElements) {
        String key = inputElement.attr("name");
        String value = inputElement.attr("value");

        if (key.equals("Email"))
            value = username;
        else if (key.equals("Passwd"))
            value = password;
        paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
    }

    // build parameters list
    StringBuilder result = new StringBuilder();
    for (String param : paramList) {
        if (result.length() == 0) {
            result.append(param);
        } else {
            result.append("&" + param);
        }
    }
    return result.toString();
  }
  public static String getTokenAndPwd() throws Exception {
		String str = "";
		String url = "https://passport.5173.com";
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();
	    URL obj = new URL(url);
		//HttpsURLConnection conn = null;
		conn = (HttpsURLConnection) obj.openConnection();
		conn.setSSLSocketFactory(ssf);
		// default is GET
		conn.setRequestMethod("GET");
		// act like a browser
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		//conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		conn.setRequestProperty("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		conn.setRequestProperty("Accept-Encoding","gzip, deflate");
		conn.setRequestProperty("Connection","keep-alive");
		conn.setRequestProperty("Host","passport.5173.com");
		Parser parser = new Parser(conn);
		parser.setEncoding("UTF-8");
		NodeList list = parser.parse(new HasAttributeFilter("type", "text/javascript"));
		ScriptTag jstag = null;
		String pwd = "";
		String token = "";
		for (int i = 0; i < list.size(); i++) {
			jstag = (ScriptTag) list.elementAt(i);
			if (jstag == null)
				continue;
			String scrstr = jstag.toPlainTextString();
			if (scrstr.contains("$.fost.LoginParams")) {
				int beginindex = scrstr.indexOf("{");
				int endindex = scrstr.indexOf("};");
				scrstr = scrstr.substring(beginindex, endindex + 1);
				System.out.println("jstag==" + scrstr);
				JSONObject jsonobj = new JSONObject(scrstr);
				token = (String) jsonobj.get("SecurityToken");
				System.out.println("SecurityToken==" + jsonobj.get("SecurityToken"));
				pwd = (String) jsonobj.get("PasswordKey");
				System.out.println("PasswordKey==" + jsonobj.get("PasswordKey"));
			}
		}
		if (!pwd.equals("") && !token.equals("")) {
			pwd = hex_md5(pwd);
			str ="&password=" + pwd+"&passpod=&captcha=&category=&"+"__validationToken__=" + token;
		}
		return str;
	}
  public static String hex_md5(String pwdsrc) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		String jsFileName = "md5.js"; // 读取js文件
		FileReader reader = new FileReader(jsFileName); // 执行指定脚本
		engine.eval(reader);
		String PasswordKey = pwdsrc;
		String hexpwd = "";
		if (engine instanceof Invocable) {
			Invocable invoke = (Invocable) engine; // 调用merge方法，并传入两个参数
			String c = (String) invoke.invokeFunction("hex_md5", "54054110");
			hexpwd = (String) invoke.invokeFunction("hex_md5", c.substring(8, 24) + PasswordKey);
			System.out.println("hexpwd = " + hexpwd);
		}
		reader.close();
		return hexpwd;
	}
  public List<String> getCookies() {
    return cookies;
  }

  public void setCookies(List<String> cookies) {
    this.cookies = cookies;
    System.out.println("this.cookies =="+ this.cookies );
  }

}
