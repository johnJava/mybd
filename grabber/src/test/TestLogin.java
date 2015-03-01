package test;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.json.JSONObject;

import com.common.GenericUtil;
import com.common.LogUtil;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class TestLogin {
	public static void main(String[] args) throws MalformedURLException {
		getStream(new URL("https://passport.5173.com"), "", new URL("https://passport.5173.com"));
	}
	
	public static void test() throws ParserException {
		String url = "https://passport.5173.com";
		long startTime = System.currentTimeMillis();
		LogUtil.debugPrintf("请求[" + url + "]开始[" + GenericUtil.dateformat.format(startTime) + "]");
		URL urlPage;
		HttpURLConnection conn = null;
		try {
			urlPage = new URL(url);
			conn = (HttpURLConnection) urlPage.openConnection();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e1) {
			e1.printStackTrace();
		}
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		conn.setRequestProperty("If-Modified-Since", "0");
		Parser parser = new Parser(conn);
		parser.setEncoding("UTF-8");
		Map<String, List<String>> head = conn.getHeaderFields();
		// System.out.println("head===" + head.toString());
		long endTime = System.currentTimeMillis();
		LogUtil.infoPrintf("请求结束[" + GenericUtil.dateformat.format(endTime) + "]耗时[" + (endTime - startTime) + "毫秒]");
		NodeList list = parser.parse(new HasAttributeFilter("type", "text/javascript"));
		// System.out.println(list.toHtml());
		ScriptTag jstag = null;
		for (int i = 0; i < list.size(); i++) {
			jstag = (ScriptTag) list.elementAt(i);
			if (jstag == null)
				continue;
			String scrstr = jstag.toPlainTextString();
			if (scrstr.contains("$.fost.LoginParams")) {
				int beginindex = scrstr.indexOf("PasswordKey:\"");
				int endindex = scrstr.indexOf("ReturnUrl");
				System.out.println(beginindex + ":" + endindex);
				scrstr = scrstr.substring(beginindex, endindex);
				System.out.println("jstag==" + scrstr);
				System.out.println("jstag==" + scrstr.substring(scrstr.indexOf("\"") + 1, scrstr.lastIndexOf("\"")));
			}
		}
	}

	@org.junit.Test
	public void sendEms() throws Exception {
		String sessionId = "";
		String wen = "MS2201828";
		String btnSearch = "EMS快递查询";
		URL url = new URL("http://s.5173.com/");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");// 提交模式
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:34.0) Gecko/20100101 Firefox/34.0");
		//conn.setRequestProperty("If-Modified-Since", "0");
		// conn.setReadTimeout(2000);//读取超时 单位毫秒
		conn.setDoOutput(true);// 是否输入参数
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(false);
//		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		conn.setRequestProperty("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//		conn.setRequestProperty("Accept-Encoding","gzip, deflate");
//		conn.setRequestProperty("Connection","Keep-Alive");
//		InputStream in = conn.getInputStream();  
//		String key = "";
//		String msg="";
//		if (conn != null) {
//			for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
//				msg+=key+":"+conn.getHeaderField(key)+",";
//				if (key.equalsIgnoreCase("set-cookie")) {
//					sessionId = conn.getHeaderField(key);
//					sessionId = sessionId.substring(0, sessionId.indexOf(";"));
//				}
//			}
//		}
//		System.out.println("sessionId===" +sessionId);
//		System.out.println("msg===" +msg);
//		conn.setRequestMethod("POST");// 提交模式
//		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
//		conn.setRequestProperty("If-Modified-Since", "0");
//		// conn.setReadTimeout(2000);//读取超时 单位毫秒
//		conn.setDoOutput(true);// 是否输入参数
//		conn.setDoInput(true);
//		conn.setUseCaches(false);
//		conn.setInstanceFollowRedirects(false);
//		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		conn.connect();
//		// StringBuffer params = new StringBuffer();
//		// 表单参数与get形式一样
//		// params.append("wen").append("=").append(wen).append("&").append("btnSearch").append("=").append(btnSearch);
//		// byte[] bypes =
//		// "returnUrl=http%3a%2f%2fwww.5173.com&__validationDna__=&__validationToken__=c01b92f8a7f345cab8b75ccc487bd53b&captcha=NBDU&category=&passpod=&password=51b46ba4bd092f039e06895524376f3c&userName=wlg7766".getBytes();
//		String params = URLEncoder.encode("returnUrl=http%3a%2f%2fwww.5173.com&__validationDna__=&captcha=&category=&passpod=&" + getTokenAndPwd()
//				+ "&userName=wlg7766", "utf-8");
//		byte[] bypes = params.getBytes();
//		conn.getOutputStream().write(bypes);// 输入参数
		InputStream inStream = conn.getInputStream();
		System.out.println(new String(readStream(inStream), "utf-8"));
		Map<String, List<String>> head = conn.getHeaderFields();
		System.out.println("head===" + head.toString());

	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	public String getTokenAndPwd() throws Exception {
		String str = "";
		String url = "https://passport.5173.com";
		URL urlPage;
		HttpsURLConnection conn = null;
		try {
			urlPage = new URL(url);
			conn = (HttpsURLConnection) urlPage.openConnection();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e1) {
			e1.printStackTrace();
		}
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		conn.setRequestProperty("If-Modified-Since", "0");
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
			str = "__validationToken__=" + token + "&password=" + pwd;
		}
		return str;
	}

	public String hex_md5(String pwdsrc) throws Exception {
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
			hexpwd = (String) invoke.invokeFunction("hex_md5", c.substring(8, 16) + PasswordKey);
			System.out.println("hexpwd = " + hexpwd);
		}
		reader.close();
		return hexpwd;
	}
	 public static InputStream getStream(URL url,String post,URL cookieurl){  
	        HttpURLConnection connection;  
	        String cookieVal = null;  
	        String sessionId = "";  
	        String key=null;  
	        if(cookieurl!=null){              
	            try{  
	                connection = (HttpURLConnection)cookieurl.openConnection();  
	                for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++ ) {  
	                    if (key.equalsIgnoreCase("set-cookie")) {  
	                        cookieVal = connection.getHeaderField(i);  
	                        cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));  
	                        sessionId = sessionId+cookieVal+";";  
	                    }  
	                }  
	                InputStream in = connection.getInputStream();  
	                System.out.println(sessionId);  
	            }catch(MalformedURLException e){  
	                System.out.println("url can't connection");  
	                return null;  
	            }catch(IOException e){  
	                System.out.println(e.getMessage());  
	                return null;  
	            }  
	        }  
	  
	        try {  
	            connection = (HttpURLConnection)url.openConnection();  
	            //这个要写在Post前,否则会取不到值,原因我不知道  
	            if(cookieurl!=null){  
	                connection.setRequestProperty("Cookie", sessionId);  
	            }  
	            if(post!=""){  
	                connection.setDoOutput(true);  
	                connection.setRequestMethod("POST");  
	                connection.getOutputStream().write(post.getBytes());  
	                connection.getOutputStream().flush();  
	                connection.getOutputStream().close();  
	            }  
	            int responseCode = connection.getResponseCode();  
	            int contentLength = connection.getContentLength();  
	            // System.out.println("Content length: "+contentLength);  
	            if (responseCode != HttpURLConnection.HTTP_OK ) return(null);  
	            InputStream in = connection.getInputStream();  
	            return(in);  
	        }  
	        catch(Exception e) {  
	            // System.out.println(e);  
	            // e.printStackTrace();  
	            return(null);  
	        }  
	          
	    }  
}
