package test;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
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
  private static HttpURLConnection httpconn;

  //private final String USER_AGENT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:34.0) Gecko/20100101 Firefox/34.0";

  public static void main(String[] args) throws Exception {

   // String url = "https://passport.5173.com/";
   String url = "http://consignment.5173.com/PurchaseProcess/goPayFor.aspx?OrderId=JS20150308822136071";
    //String url = "https://a.5173.com/__tra.gif?referrer=&r=0.8926788993015564&url=https%3A%2F%2Fpassport.5173.com%2F&domain=passport.5173.com&title=%E7%99%BB%E5%BD%95%7C5173.com%7C%E8%A3%85%E5%A4%87%E4%BA%A4%E6%98%93%E6%B8%B8%E6%88%8F%E5%B8%81%E4%BA%A4%E6%98%93%E5%B8%90%E5%8F%B7%E4%BA%A4%E6%98%93%7C%E5%AF%84%E5%94%AE%E6%8B%85%E4%BF%9D%E4%BA%A4%E6%98%93%7C%E6%B8%B8%E6%88%8F%E7%82%B9%E5%8D%A1%7C%E4%BB%A3%E7%BB%83%E4%B8%AD%E4%BB%8B%7C%E5%B0%8F%E9%A2%9D%E4%BA%A4%E6%98%93&height=800&width=1280&colorDepth=32&language=&agent=Mozilla%2F4.0%20(compatible%3B%20MSIE%209.0%3B%20Windows%20NT%206.1%3B%20Trident%2F5.0%3B%20SLCC2%3B%20.NET%20CLR%202.0.50727%3B%20.NET%20CLR%203.5.30729%3B%20.NET%20CLR%203.0.30729%3B%20Media%20Center%20PC%206.0%3B%20.NET4.0C%3B%20.NET4.0E)";
    String gmail = "https://mail.google.com/mail/";

    HttpUrlConnectionExample http = new HttpUrlConnectionExample();

    // make sure cookies is turn on
    CookieHandler.setDefault(new CookieManager());

    // 1. Send a "GET" request, so that you can extract the form's data.
    //String page = http.GetPageContent(url);
    //String postParams = http.getFormParams(page, "username@gmail.com", "password");
    //String postParams = "userName=wlg7766&password=374e1a456f7fcd3a2935cfacfe180a05&passpod=&captcha=&category=&__validationToken__=3b6d01d352b0450a8c2ac52fdf0d2937&__validationDna__=";
   // String postParams = "userName=wlg7766&password=374e1a456f7fcd3a2935cfacfe180a05&passpod=&captcha=&category="+getTokenAndPwd()+"&__validationDna__=";
    //String postParams = "userName=wlg7766"+getTokenAndPwd()+"&__validationDna__=";
    String postParams = "0915324f09d34f42a6b45b9d4235a1ab=OBUu8%7Bline%7DwTWq8%7Bplus%7Dl6Is3khDXWMazP5XTx%7Bline%7DCLk2w2ZMs5w52pO1ZM7cJPA%7Bequel%7D%7Bequel%7D&__VIEWSTATE=%2FwEPDwUJODc3MjM0MzQzZGQfVbUBP4gXWRDd3JaTWD9b%2BtyB5g%3D%3D&__EVENTVALIDATION=%2FwEWAwKjt%2BuHBAL%2FqbilAQL%2ByLa2BqVfi8nTv51XgZCgTkczMLz5NPDP&hdStatus=&hiddenBtnGoPayfor=";
    //String postParams ="userName=wlg7766&password=915a18a042c7176c25f80615fbe40e81&passpod=&captcha=b2xb&category=&__validationToken__=8e0b8ba5e9bd4fe4aef867ea19edf52e&__validationDna__=";
    //
//    // 2. Construct above post's content and then send a POST request for
//    // authentication
//    http.sendPost(url, postParams);
    http.sendHTTPPost(url, postParams);
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
  private void sendHTTPPost(String url, String postParams) throws Exception {
	  
	  URL obj = new URL(url);
	  httpconn = (HttpURLConnection) obj.openConnection();
	  
	  // Acts like a browser
	  httpconn.setRequestMethod("POST");
	  //conn.setRequestProperty("Host","passport.5173.com");
	  httpconn.setRequestProperty("Host","consignment.5173.com");
	  httpconn.setRequestProperty("User-Agent", USER_AGENT);
	  httpconn.setRequestProperty("Accept","*/*");
	  httpconn.setRequestProperty("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
	  httpconn.setRequestProperty("Accept-Encoding","deflate");//注意压缩方式
	  httpconn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
	  httpconn.setRequestProperty("X-Requested-With","XMLHttpRequest");
//    conn.setRequestProperty("Referer","https://passport.5173.com/");
	  httpconn.setRequestProperty("Referer","http://consignment.5173.com/PurchaseProcess/goPayFor.aspx?OrderId=JS20150308822136071");
	  httpconn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
	  httpconn.setRequestProperty("Connection", "keep-alive");
	  httpconn.setRequestProperty("Pragma","no-cache");
	  httpconn.setRequestProperty("Cache-Control","no-cache");
	  httpconn.setRequestProperty("Cookie","__utma=12194411.640244570.1423404202.1425731847.1425825876.4; __utmz=12194411.1423404202.1.1.utmccn=(referral)|utmcsr=5173.com|utmcct=/|utmcmd=referral; Hm_lvt_6054b5813cf740e14dd8df8f5d0cb24b=1423404203,1425725617,1425825873; __utmv=12194411.wlg7766; _BFD=2370208; tma=260225609.24577952.1423404267879.1425725679270.1425825955342.3; tmd=39.260225609.24577952.1423404267879.; __vizrg=1; VH=44343b06076d4a7a95a0ef22aac481ae,2f4c229f6b074c9cae4ffdf9adcd7af6,consignment,JS064-20141225-80116573,%u6B8B%u5FCD%u7684%u6148%u60B2%20%u6697%u591C%u4F7F%u8005%2050%u7EA7%u7C89%u5315%u9996%20%u539F%20...,5.00|44343b06076d4a7a95a0ef22aac481ae,2f4c229f6b074c9cae4ffdf9adcd7af6,consignment,JS064-20141225-80189802,%u9ED1%u72FC%u56FE%u817E%20%u5723%u804C%u8005%2050%u7EA7%u7C89%u56FE%u817E%20%u539F%20%u88C5%20...,5.00|44343b06076d4a7a95a0ef22aac481ae,2f4c229f6b074c9cae4ffdf9adcd7af6,Escort,DB145-20140606-50858347,+7%u6697%u591C%u4E4B%u730E%u77B3%20%u795E%u67AA%u624B%20%20%u6781%u54C1%u5C5E%u6027%20%u4FBF%u5B9C...,5.00|44343b06076d4a7a95a0ef22aac481ae,2f4c229f6b074c9cae4ffdf9adcd7af6,consignment,JS166-20150208-80007662,%u5168%u5957+10%20%u5957%u88C5%2080cc%u677F%u7532%u5957%u3010%u5929%u9A6C%u5B88...,330.00; NTKF_T2D_CLIENTID=guest1D8E3970-27EE-A4C1-86CD-698E21DA91AC; nprdC=7; cokGames=44343b06076d4a7a95a0ef22aac481ae; trace_u=mypay.ebatong.com; guide_payTips=s; trace_5173=20150308224425691a5713309233311d; Hm_lpvt_6054b5813cf740e14dd8df8f5d0cb24b=1425826200; __utmb=12194411; __utmc=12194411; .5173auth=3iJgVlXrbdSam8Tp4n2jPpb0SBArGuWYLMVi4LI2WGlz5Lpi+Fi+4QItb+g8BwyeuO3qeM8bb68SMU5g3Tr5CnNR1oDquJuXI0HEWLJQLN5Kvd+h2gIIeHv+RHzfd6xmDaZEACp/u+zjsWabQCX0FJxWBmq2egga0T2q2X65BRIWNvO+ABGGWp6A5ji6zgRQLCdJ+6axvBecwclIu/EHg/9126CgIt/wRcPfGqlvKzuF9UyYL6aD0G0aaiOIH8XoJIVhhcvAnyjr7IUfedTFPAercGhRl0c+YBjbpgHJ1Ip19UPQNN5j6g; bfd_session_id=bfd_g=b56c782bcb75035d00003a1202add6fd54d76ceb&bfd_s=260225609.19402574.1425825955325; tmc=4.260225609.23107635.1425825955331.1425826008784.1425826165446; __vizcnt=2|1425912362944; ASP.NET_SessionId=n5glegzatkesketbio2dbwxz; NTKF_PAGE_MANAGE=%7B%22m%22%3A%5B%5D%2C%22t%22%3A%2222%3A49%3A59%22%7D; NTKF_CACHE_DATA=%7B%22uid%22%3A%22bq_1007_ISME9754_guest1D8E3970-27EE-A4%22%2C%22tid%22%3A%221425826165881788%22%2C%22fid%22%3A%221425826165619649%22%2C%22d%22%3A%7B%7D%7D");
	  httpconn.setDoOutput(true);
	  httpconn.setDoInput(true);
	  // Send post request
	  DataOutputStream wr = new DataOutputStream(httpconn.getOutputStream());
	  wr.writeBytes(postParams);
	  wr.flush();
	  wr.close();
	  
	  int responseCode = httpconn.getResponseCode();
	  
	  System.out.println("\nSending 'POST' request to URL : " + url);
	  System.out.println("Post parameters : " + postParams);
	  System.out.println("Response Code : " + responseCode);
	  
	  BufferedReader in =new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
	  String inputLine;
	  StringBuffer response = new StringBuffer();
	  System.out.println("===========");
	  while ((inputLine = in.readLine()) != null) {
		  response.append(inputLine);
	  }
	  in.close();
	  System.out.println(response.toString());
	  System.out.println("===========");
	  System.out.println("httpconn.getHeaderFields():"+httpconn.getHeaderFields());
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
