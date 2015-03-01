package com.core;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.http.ConnectionManager;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.service.WebSocketMessageServlet;
@SuppressWarnings("unused" )
public class Common {
	private ConnectionManager manager = Page.getConnectionManager();
	public int count = 0;//41 * 40;
	public int list1UpdateEndIndex = 0;
	public int list2UpdateEndIndex = 0;
	public Vector<String> hreflist1 = new Vector<String>();
	public Vector<String> hreflist2 = new Vector<String>();
	private long avrtime;
	private int avrcount;
	private int query;
	private static Common instance = null;
	private Queue<String> queue = new LinkedList<String>();
	private Common() {
		UpdateAVRT avr = new UpdateAVRT();
		new  Thread(avr).start();
	}
	public static Common getInstance() {
		if (instance == null)
			instance = new Common();
		return instance;
	}

	public NodeList doSnatch(String url) throws ParserException {
		this.query++;
		try {
			WebSocketMessageServlet.inbound.send("query查询次数："+this.query);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		long startTime = System.currentTimeMillis();
		System.out.println("抓取[" + url + "]开始[" + format.format(startTime)
				+ "]");
		NodeList result = null;
		 URL urlPage;
		 HttpURLConnection conn =null;
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
        conn.setRequestProperty("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
        conn.setRequestProperty("If-Modified-Since","0");
		Parser parser = new Parser(conn);
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
		return result;
	}


	public void updateCount(String url) throws ParserException {
		this.query++;
		try {
			WebSocketMessageServlet.inbound.send("query查询次数："+this.query);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		long startTime = System.currentTimeMillis();
		System.out.println("抓取[" + url + "]开始[" + format.format(startTime)
				+ "]");
		 URL urlPage;
		 HttpURLConnection conn =null;
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
        conn.setRequestProperty("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
        conn.setRequestProperty("If-Modified-Since","0");
		Parser parser = new Parser(conn);
		parser.setEncoding("UTF-8");
		try {
			NodeList lblTotalCount = parser
					.extractAllNodesThatMatch(new HasAttributeFilter("id",
							"SiteMap1_lblTotalCount"));
			Span tag = (Span) lblTotalCount.elementAt(0);
			System.out.println("lblTotalCount==" + tag.getStringText());
			this.count = Integer.parseInt(tag.getStringText());
		} catch (ParserException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("抓取[" + url + "]结束[" + format.format(endTime)
				+ "]耗时[" + (endTime - startTime) + "毫秒]");
		System.out.println("this.count===" + this.count);
	}

	public void doList1Update(NodeList newList) {
		System.out.println("doList1Update---------------------->");
		int update = 0;
		for (int i = newList.size() - 1; i >= 0; i--) {
			Node node = newList.elementAt(i);
			NodeList list = node.getChildren();
			System.out.println("list.toHtml()"+list.toHtml());
			NodeList lilist = list.extractAllNodesThatMatch(new HasAttributeFilter("class", "credit"),true);
//			System.out.println("lilist.toHtml()=="+lilist.toHtml());
			Node linode = lilist.elementAt(0);
			System.out.println("linode.toHtml()=="+linode.toHtml());
			System.out.println("linode.getChildren().size()=="+linode.getChildren().size());
			list = list.extractAllNodesThatMatch(
					new HasAttributeFilter("href"), true);
			LinkTag tag = (LinkTag) list.elementAt(0);
			String href = tag.getAttribute("href");
			int hrefindex = this.hreflist1.indexOf(href);
			System.out.println("href[" + hrefindex + "]===" + href);
			if (hrefindex == -1) {
				update++;
				updateList(this.hreflist1, href);
			}
		}
		System.out.println("共计更新[" + update + "]条记录");
	}

	public String doList2Update(NodeList newList) {
		String msg = "";
		System.out.println("doList2Update---------------------->");
		int update = 0;
		for (int i = newList.size() - 1; i >= 0; i--) {
			Node node = newList.elementAt(i);
			NodeList list = node.getChildren();
			System.out.println("list.toHtml()"+list.toHtml());
			NodeList lilist = list.extractAllNodesThatMatch(new HasAttributeFilter("class", "credit"),true);
			Node linode = lilist.elementAt(0);
			System.out.println("linode.toHtml()=="+linode.toHtml());
			System.out.println("linode.getChildren().size()=="+linode.getChildren().size());
			list = list.extractAllNodesThatMatch(new HasAttributeFilter("href"), true);
			LinkTag tag = (LinkTag) list.elementAt(0);
			if (node.toHtml().indexOf("ico_pei") != -1) {
				break;
			}
			String href = tag.getAttribute("href");
			int hrefindex = this.hreflist2.indexOf(href);
			System.out.println("href[" + hrefindex + "]===" + href);
			if (hrefindex == -1) {
				update++;
				updateList(this.hreflist2, href);
			}
		}
		System.out.println("共计更新[" + update + "]条记录");

		if (list2UpdateEndIndex == newList.size() - 1) {
			msg = "next";
		}
		return msg;
	}


	public void updateList(List<String> hreflist, String href) {
		System.out.println("href==" + href);
		hreflist.add(href);
		try {
			long currentTime = System.currentTimeMillis();
			WebSocketMessageServlet.inbound.send(new SimpleDateFormat(
					"MM/dd/yyyy HH:mm:ss").format(currentTime)
					+ ":" + href);
			if(hreflist.size()>40)
			this.queue.add(currentTime+"@"+href);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (hreflist.size() > 80) {
			hreflist.remove(0);
		}
	}

	public void updateAvrtime(long currentTime,String href){
		try {
			long publishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getPublishTime(href)).getTime();
			int timecount = this.avrcount+1;
			long updatetimeinterv=currentTime-publishTime;
			if(updatetimeinterv>60000)return;
			System.out.println("this.avrtime="+this.avrtime+"*"+this.avrcount+"/("+timecount+")+("+updatetimeinterv+")/("+timecount+")");
			this.avrtime=this.avrtime*this.avrcount/timecount+updatetimeinterv/timecount;
			this.avrcount++;
			WebSocketMessageServlet.inbound.send("avrtime平均出单时间（秒）："+(double)this.avrtime/1000);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
	}
	public String getPublishTime(String href) throws ParserException{
		String ptime="";
		Parser parser=getParser(href);
		try {
			if(href.contains("http://consignment.5173.com")){
				NodeList timenodelist = parser
						.extractAllNodesThatMatch(new HasAttributeFilter("id","bizOfferInfo_cnt"));
				Node node = timenodelist.elementAt(0);
				NodeList list = node.getChildren();
				LabelTag tag = (LabelTag) (list.elementAt(1));
				String str = tag.getStringText();
				ptime=str.substring(str.indexOf('-')-4);
			}else{
				NodeList timenodelist = parser
					.extractAllNodesThatMatch(new HasAttributeFilter("id",
							"GoodsDescription1_spanEPublishTime"));
				Span tag = (Span) timenodelist.elementAt(0);
				ptime= tag.getStringText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("publishtime==" + ptime);
		return ptime;
	}
	
	public Parser getParser(String url) throws ParserException {
		this.query++;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		long startTime = System.currentTimeMillis();
		System.out.println("抓取[" + url + "]开始[" + format.format(startTime)
				+ "]");
		 URL urlPage;
		 HttpURLConnection conn =null;
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
        conn.setRequestProperty("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
        conn.setRequestProperty("If-Modified-Since","0");
		Parser parser = new Parser(conn);
		parser.setEncoding("UTF-8");
		long endTime = System.currentTimeMillis();
		System.out.println("抓取[" + url + "]结束[" + format.format(endTime)
				+ "]耗时[" + (endTime - startTime) + "毫秒]");
		return parser;
	}
	
	public void printList(NodeList updateList) {
		NodeList nodes = updateList.extractAllNodesThatMatch(
				new HasAttributeFilter("class", "tt"), true);
		if (nodes != null) {
			System.out.println("共计[" + nodes.size() + "]条记录");
			for (int i = 0; i < nodes.size(); i++) {
				Node textnode = nodes.elementAt(i);
				NodeList list = textnode.getChildren();
				list = list.extractAllNodesThatMatch(new HasAttributeFilter(
						"href"), true);
				LinkTag tag = (LinkTag) list.elementAt(0);
				System.out.println("href[" + (i + 1) + "]==="
						+ tag.getAttribute("href"));
			}
		}
	}

	public void printNode(Node textnode) {
		NodeList list = textnode.getChildren();
		list = list.extractAllNodesThatMatch(new HasAttributeFilter("href"),
				true);
		LinkTag tag = (LinkTag) list.elementAt(0);
		System.out.println("href===" + tag.getAttribute("href"));
	}
	class UpdateAVRT  implements Runnable {

		public void run() {
				while (true) {
					String str = queue.poll();
					if(str!=null){
						String[] para = str.split("@");
						Common.getInstance().updateAvrtime(Long.parseLong(para[0]), para[1]);
					}
				}
	   };
	}
}
