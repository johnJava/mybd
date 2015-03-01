package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.http.ConnectionManager;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.junit.Test;

import com.core.UUsearch;
@SuppressWarnings({ "unused" })
public class TestUUsearch {
	public static void testGetFile(){
//		  System.getProperties().setProperty("http.proxyHost", "119.6.144.70");
//	      System.getProperties().setProperty("http.proxyPort", "82");
		 List<String> iplist = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				/*add("119.6.144.73@80");
				add("119.6.144.70@82");
				//add("120.131.128.215@80");
				//add("120.132.48.68@80");
				//add("219.243.15.99@3128");
				add("220.231.32.195@3128");
				add("117.135.244.41@80");
				add("111.161.126.99@80");
				add("111.13.2.136@80");
				//add("222.88.236.236@80");
				add("111.161.126.98@80");
				//add("122.225.106.40@80");
				//add("60.206.239.195@80");
//				add("117.135.244.41@8118");
//				add("220.181.32.106@80");
//				add("183.221.190.77@8123");
//				add("122.226.172.18@3128");
//				add("36.250.69.4@80");
				//add("114.255.183.174@8080");
				//add("118.144.182.101@8118");
				//add("183.203.208.168@8118");*/
				add("221.238.140.164@8080");
				add("183.221.217.34@8123");
				add("180.153.100.242@80");
				add("183.221.170.144@8123");
				add("163.177.79.5@80");
				add("114.112.91.97@90");
				add("202.106.16.36@3128");
				add("175.1.135.12@80");
				add("119.6.144.78@80");
				add("218.7.132.1@8080");
				add("183.221.187.181@8123");
				add("203.100.80.81@8080");
				add("36.250.74.88@80");
				add("115.197.159.225@8118");
				add("111.199.230.209@8118");
				add("222.45.196.19@8118");
				add("124.161.78.202@808");
				add("114.255.183.164@8080");
				add("60.206.153.177@8118");
				add("202.108.15.130@9999");
				add("202.106.169.228@8080");
				add("113.214.13.1@8000");
				add("125.220.29.102@18186");
				add("119.188.94.145@80");
				add("183.221.217.78@8123");
				add("112.124.64.221@8000");
				add("123.125.122.58@80");
				add("182.254.214.64@8080");
				add("183.221.187.186@8123");
				add("119.40.36.79@18186");
				add("211.141.130.96@8118");
				add("58.213.19.134@2311");
				add("183.203.208.169@8118");
				add("183.203.208.167@8118");
				add("218.204.141.92@8118");
				add("111.13.2.130@80");
				add("183.221.217.187@8123");
				add("222.45.196.17@8118");
				add("183.203.208.168@8118");
				add("119.39.128.51@808");
				add("116.236.216.116@8080");
				add("211.141.128.81@8118");
				add("124.67.253.138@808");
				add("111.192.28.38@8118");
				add("183.207.232.119@8080");
				add("59.120.141.168@8888");
				add("61.234.123.64@8080");
				add("124.202.221.26@8118");
				add("219.145.93.110@8080");
				add("222.87.129.218@80");
				add("202.38.95.66@1080");
				add("119.97.164.48@8085");
				add("42.51.152.84@808");
				add("221.176.14.72@80");
				add("222.59.246.38@8118");
				add("182.254.129.68@80");
				add("114.255.183.163@8080");
				add("202.107.233.85@8080");
				add("115.236.59.194@3128");
				add("183.203.208.170@8118");
				add("27.221.43.37@8090");
				add("218.204.141.204@8118");
				add("183.230.53.242@8123");
				add("183.221.217.56@8123");
				add("125.39.66.66@80");
				add("111.166.196.144@8118");
				add("183.221.190.45@8123");
				add("183.221.164.182@8123");
				add("183.230.53.184@8123");
				add("221.130.178.78@8585");
				add("183.221.171.67@8123");
				add("222.45.194.122@8118");
				add("210.209.79.200@808");
				add("183.230.53.182@8123");
				add("183.203.208.171@8118");
				add("183.221.184.3@8123");
				add("221.0.182.5@808");
				add("60.165.157.189@18186");
				add("60.206.239.195@8118");
				add("123.119.160.255@8118");
				add("59.78.160.248@8080");
				add("183.203.208.162@8118");
				add("183.221.189.89@8123");
				add("118.144.182.101@8118");
				add("115.194.68.168@8118");
				add("114.255.183.174@8080");
				add("183.230.53.193@8123");
				add("113.9.13.222@8585");
				add("183.221.217.207@8123");
				add("115.45.57.2@8118");
				add("42.121.105.155@8888");
				add("61.184.192.42@80");
				add("221.10.102.199@82");
				add("27.159.212.210@8118");
				add("60.24.126.34@8118");
				add("222.95.11.193@8118");
				add("119.254.66.7@5060");
				add("121.31.5.101@80");
				add("111.192.146.140@8118");
				add("218.204.118.236@8123");
			}
		};
		for(int i=0;i<iplist.size();i++){
			List<String> iplists = new ArrayList<String>() ;
			System.out.println("ipproxy:"+iplist.get(i));
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			long startTime = System.currentTimeMillis();
			String[] ipstr = iplist.get(i).split("@");
			ConnectionManager manager = Page.getConnectionManager();
			manager.setProxyHost(ipstr[0]);
			manager.setProxyPort(Integer.parseInt(ipstr[1]));
			Parser parser;
			try {
				HttpURLConnection conn = (HttpURLConnection) manager.openConnection("http://city.ip138.com/ip2city.asp");
    			conn.setConnectTimeout(5000);
    			conn.setReadTimeout(5000);
//				 URL urlPage = new URL("http://city.ip138.com/ip2city.asp");
//				   HttpURLConnection conn = (HttpURLConnection) urlPage.openConnection();  
//				   conn.setConnectTimeout(5000);  
//				   conn.setReadTimeout(5000);   
				parser = new Parser(conn);
				//parser.wait(60000);
				// 设置Parser对象的字符编码,一般与网页的字符编码保持一致
				parser.setEncoding("UTF-8");
				NodeList ls = parser.parse(null);
				System.out.println(ls.toHtml());
			} catch (Exception e) {
				e.printStackTrace();
			} 
			long endTime = System.currentTimeMillis();
			System.out.println("抓取[" + iplist.get(i) + "]结束[" + format.format(endTime)
					+ "]耗时[" + (endTime - startTime) + "毫秒]");
		}
	}
	public static void sigRequest() throws ParserException{
		ConnectionManager manager = Page.getConnectionManager();
		manager.setProxyHost("119.6.144.70");
		manager.setProxyPort(82);
		Parser parser = new Parser(manager.openConnection("http://city.ip138.com/ip2city.asp"));
		// 设置Parser对象的字符编码,一般与网页的字符编码保持一致
		parser.setEncoding("UTF-8");
		NodeList ls = parser.parse(null);
		System.out.println(ls.toHtml());
	}
	public static void main(String[] args) throws ParserException, MalformedURLException, IOException {
		//testGetFile();
		doSnatch2("http://s.5173.com/dnf-0-0-0-0-2zkpio-0-0-0-a-a-a-a-a-0-0-0-116.shtml?keyword=20141230");
	}
	
	public static NodeList doSnatch(String url) throws ParserException {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		long startTime = System.currentTimeMillis();
		System.out.println("抓取[" + url + "]开始[" + format.format(startTime)
				+ "]");
		NodeList result = null;
		Parser parser = new Parser(Page.getConnectionManager().openConnection(url));
		parser.setEncoding("UTF-8");
		try {
			result = parser.extractAllNodesThatMatch(new HasAttributeFilter(
					"class", "sin_pdlbox"));
		} catch (ParserException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("抓取[" + url + "]结束[" + format.format(endTime)
				+ "]耗时[" + (endTime - startTime) + "毫秒]");
		// printList(result);
		return result;
	}
	public static NodeList doSnatch2(String url) throws ParserException {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		long startTime = System.currentTimeMillis();
		System.out.println("抓取[" + url + "]开始[" + format.format(startTime)
				+ "]");
		NodeList result = null;
		//intitMananger();
		//HttpURLConnection conn = (HttpURLConnection)manager.openConnection(url);
		 URL urlPage;
		 HttpURLConnection conn =null;
		  String cookieVal = null;  
	        String sessionId = "";  
	        String key=null;  
		try {
			urlPage = new URL(url);
			conn = (HttpURLConnection) urlPage.openConnection();  
			 for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++ ) {  
				 System.out.println("key==="+key);
                 if (key.equalsIgnoreCase("set-cookie")) {  
                     cookieVal = conn.getHeaderField(i);  
                     //cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));  
                     sessionId = sessionId+cookieVal+";";  
                 }  
             }  
			 InputStream in = conn.getInputStream();  
           System.out.println(sessionId);  
           System.out.println(cookieVal);  
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
//		HttpURLConnection.setFollowRedirects(true);
//        //conn.setUseCaches(false);
//        conn.setDoOutput(true);
//        conn.setDoInput(true);
//        try {
//			conn.setRequestMethod("POST");
//		} catch (ProtocolException e1) {
//			e1.printStackTrace();
//		}
////        conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727)");
////        conn.setRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
////        conn.setRequestProperty("Connection", "Keep-Alive");
//        conn.setRequestProperty("User-Agent","Mozilla/5.0 (X11; Linux x86_64; rv:24.0) Gecko/20100101 Firefox/24.0");
//        conn.setRequestProperty("Accept", "	text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        conn.setRequestProperty("Connection", "Keep-Alive");
//       // conn.setRequestProperty("Cookie","");
////		String cookie = conn.getHeaderField("Set-Cookie");
////		System.out.println("cookie==="+cookie);
//		Parser parser = new Parser(conn);
////		String cookie = conn.getHeaderField("Set-Cookie");// 取到所用的Cookie
////		System.out.println("cookie========================"+cookie);
//		System.out.println("getHeaderFields========================"+conn.getHeaderFields());
//		parser.setEncoding("UTF-8");
//		try {
//			result = parser.extractAllNodesThatMatch(new HasAttributeFilter(
//					"class", "sin_pdlbox"));
//		} catch (ParserException e) {
//			e.printStackTrace();
//		}
//		long endTime = System.currentTimeMillis();
//		System.out.println("抓取[" + url + "]结束[" + format.format(endTime)
//				+ "]耗时[" + (endTime - startTime) + "毫秒]");
		// printList(result);
		return result;
	}
	public void testgetDataMap() throws ParserException, MalformedURLException, IOException{
		UUsearch uu = new  UUsearch();
		String token = UUID.randomUUID().toString();
		List<Node> nodes = uu.getDataMap(token, "http://www.uu898.com/Trade.aspx?gm=41&ps=100");
		System.out.println("nodes.size():"+nodes.size());
		for (int i = 0; i < nodes.size(); i++) {
			System.out.println(nodes.get(i).toHtml());
		}
	}
	
	
	@Test
	public void testCommon() throws ParserException{
		//String url ="http://www.uu898.com/Trade.aspx?gm=115&area=1589&srv=9615&c=-2&ps=100&p=1";
		//url=url.substring(0,url.length()-1);
		//url=url.substring(url.indexOf('?')+1);
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		long startTime = System.currentTimeMillis();
		System.out.println("抓取开始：" + format.format(startTime));
		String url ="http://s.5173.com/dnf-0-0-0-0-2zkpio-0-0-0-a-a-a-a-a-1-0-0-0.shtml?keyword=20141214";
		ConnectionManager manager = Page.getConnectionManager();
		System.out.println(url);
		Parser parser = new Parser(manager.openConnection(url));
		parser.setEncoding("UTF-8");
		long endTime = System.currentTimeMillis();
		System.out.println("抓取1：" + format.format(System.currentTimeMillis()));
		System.out.println("耗时：" + (endTime - startTime) + "毫秒");
//		NodeList nodes = null;
//		try {
//			nodes = parser.extractAllNodesThatMatch(new HasAttributeFilter("class", "sin_pdlbox"));
//		} catch (ParserException e) {
//			e.printStackTrace();
//		}
		System.out.println("抓取2：" + format.format(System.currentTimeMillis()));
		System.out.println("耗时：" + (endTime - startTime) + "毫秒");
		NodeList lblTotalCount = parser.extractAllNodesThatMatch(new HasAttributeFilter("id", "SiteMap1_lblTotalCount"));
		Span tag = (Span) lblTotalCount.elementAt(0);
		System.out.println("lblTotalCount=="+tag.getStringText());
//		nodes = nodes.extractAllNodesThatMatch(new HasAttributeFilter("class", "tt"),true);
//		if(nodes!=null){
//			System.out.println("共计["+nodes.size()+"]条记录");
//			for (int i = 0; i < nodes.size(); i++) {
//				Node textnode = nodes.elementAt(i);
//				NodeList list = textnode.getChildren();
//				LinkTag tag1 = (LinkTag)list.elementAt(1);
//				System.out.println("添加第[" + (i + 1) + "]条记录");
//				//System.out.println("list.elementAt(1)==="+textnode.toHtml());
//				System.out.println("href==="+tag1.getAttribute("href")); 
//				//doRequest(manager, tag.getAttribute("href"));
//				//System.out.println(textnode.toHtml());
//			}
//		}
		endTime = System.currentTimeMillis();
		System.out.println("抓取结束：" + format.format(endTime));
		System.out.println("耗时：" + (endTime - startTime) + "毫秒");
	}
	public void doRequest(ConnectionManager manager,String url) throws ParserException{
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		long startTime = System.currentTimeMillis();
//		System.out.println("抓取开始：" + format.format(startTime));
		Parser parser = new Parser(manager.openConnection(url));
		parser.setEncoding("UTF-8");
		NodeList nodes = null;
		try {
			nodes = parser.extractAllNodesThatMatch(new HasAttributeFilter("id", "bizOfferInfo_cnt"));
		} catch (ParserException e) {
			e.printStackTrace();
		}
		if(nodes!=null){
//			System.out.println("共计["+nodes.size()+"]条记录");
				Node textnode = nodes.elementAt(0);
				NodeList cs = textnode.getChildren();
//				for (int i = 0; i < cs.size(); i++) {
//					System.out.println("==="+cs.elementAt(1).toHtml()); 
//				}
				//System.out.println("list.elementAt(1)==="+textnode.toHtml());
				System.out.println("textnode.getText()==="+cs.elementAt(1).toHtml()); 
				//System.out.println(textnode.toHtml());
		}
		long endTime = System.currentTimeMillis();
//		System.out.println("抓取结束：" + format.format(endTime));
    	System.out.println("耗时：" + (endTime - startTime) + "毫秒");
	}
}
