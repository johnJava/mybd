package com.dao;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.biz.EquBiz;
import com.common.GenericUtil;
import com.common.LogUtil;
import com.common.UrlType;
import com.vo.EquItem;
import com.web.EquMessageService;

public class EquSnatcher {
	private EquItem item;
	private EquBiz equbiz;
	private String state = "";
	private int query;
	public Vector<String> hpHrefVec = new Vector<String>();
	public Vector<String> wpHrefVec = new Vector<String>();
	private Timer timer;

	public EquSnatcher(EquItem item) {
		this.item = item;
		equbiz = new EquBiz(this.item);
		// timer = new Timer(); // 建立Timer类对象
	}

	public boolean start() {
		// timer.schedule(new SnatchTask(UrlType.HPURL), 0, 1000); // 有赔付数据抓取任务
		// timer.schedule(new SnatchTask(UrlType.UPDATEPAGEINDEX), 0, 1000); //
		// 更新无赔付页码任务
		// timer.schedule(new SnatchTask(UrlType.WPURL), 5, 1000); // 无赔付数据抓取任务
		state = "running";
		new SnatchTh(UrlType.HPURL).start();
		new SnatchTh(UrlType.UPDATEPAGEINDEX).start();
		new SnatchTh(UrlType.WPURL).start();
		return true;
	}

	public boolean concel() {
		timer.cancel();
		state = "dead";
		return true;
	}

	class SnatchTask extends TimerTask {

		private UrlType urltype;

		public SnatchTask(UrlType urltype) {
			this.urltype = urltype;
		}

		public void run() {
			LogUtil.debugPrintf("调度-->" + urltype);
			try {
				switch (urltype) {
				case HPURL:
					extractHPHtml();
					break;
				case WPURL:
					extractWPHtml();
					break;
				case UPDATEPAGEINDEX:
					updateWPIndex();
					break;
				}
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class SnatchTh extends Thread {

		private UrlType urltype;

		public SnatchTh(UrlType urltype) {
			this.urltype = urltype;
		}

		public void run() {
			while (state.equalsIgnoreCase("running")) {
				LogUtil.debugPrintf("调度-->" + urltype);
				try {
					switch (urltype) {
					case HPURL:
						extractHPHtml();
						break;
					case WPURL:
						extractWPHtml();
						break;
					case UPDATEPAGEINDEX:
						updateWPIndex();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void extractWPHtml() {
		try {
			String url = this.item.getUrl(UrlType.WPURL);
			if (url.equalsIgnoreCase("")) {
				LogUtil.debugPrintf("wpurl为空。。。。");
				return;
			}
			Parser parser = getParser(url);
			long startTime = System.currentTimeMillis();
			LogUtil.debugPrintf("抽取[" + url + "]开始[" + GenericUtil.dateformat.format(startTime) + "]");
			int update = 0;
			try {
				NodeList hplist = parser.extractAllNodesThatMatch(new HasAttributeFilter("class", "sin_pdlbox"));
				for (int i = hplist.size() - 1; i >= 0; i--) {
					Node node = hplist.elementAt(i);
					if (node.toHtml().indexOf("ico_pei") != -1) {
						break;
					}
					NodeList list = node.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("href"), true);
					LinkTag tag = (LinkTag) list.elementAt(0);
					String href = tag.getAttribute("href");
					int hrefindex = this.wpHrefVec.indexOf(href);
					LogUtil.debugPrintf("href[" + hrefindex + "]===" + href);
					if (hrefindex == -1) {
						update++;
						updateVec(this.wpHrefVec, href);
						equbiz.addNode(node);
					}
				}

			} catch (ParserException e) {
				e.printStackTrace();
			}
			long endTime = System.currentTimeMillis();
			LogUtil.debugPrintf("抽取[" + url + "]结束[" + GenericUtil.dateformat.format(endTime) + "]耗时[" + (endTime - startTime) + "毫秒]");
			LogUtil.debugPrintf("共计更新[" + update + "]条记录");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void extractHPHtml() {
		try {
			String url = this.item.getUrl(UrlType.HPURL);
			Parser parser = getParser(url);
			long startTime = System.currentTimeMillis();
			LogUtil.debugPrintf("抽取[" + url + "]开始[" + GenericUtil.dateformat.format(startTime) + "]");
			int update = 0;
			try {
				NodeList hplist = parser.extractAllNodesThatMatch(new HasAttributeFilter("class", "sin_pdlbox"));
				for (int i = hplist.size() - 1; i >= 0; i--) {
					Node node = hplist.elementAt(i);
					NodeList list = node.getChildren();
					list = list.extractAllNodesThatMatch(new HasAttributeFilter("href"), true);
					LinkTag tag = (LinkTag) list.elementAt(0);
					String href = tag.getAttribute("href");
					int hrefindex = this.hpHrefVec.indexOf(href);
					LogUtil.debugPrintf("href[" + hrefindex + "]===" + href);
					if (hrefindex == -1) {
						update++;
						updateVec(this.hpHrefVec, href);
						equbiz.addNode(node);
					}
				}

			} catch (ParserException e) {
				e.printStackTrace();
			}
			long endTime = System.currentTimeMillis();
			LogUtil.debugPrintf("抽取[" + url + "]结束[" + GenericUtil.dateformat.format(endTime) + "]耗时[" + (endTime - startTime) + "毫秒]");
			LogUtil.debugPrintf("共计更新[" + update + "]条记录");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateVec(Vector<String> hrefVec, String href) {
		hrefVec.add(href);
		if (hrefVec.size() > 60) {
			hrefVec.remove(0);
		}
	}

	public void updateWPIndex() {
		try {
			String url = this.item.getUrl(UrlType.UPDATEPAGEINDEX);
			Parser parser = getParser(url);
			long startTime = System.currentTimeMillis();
			LogUtil.debugPrintf("抽取[" + url + "]开始[" + GenericUtil.dateformat.format(startTime) + "]");
			try {
				NodeList lblTotalCount = parser.extractAllNodesThatMatch(new HasAttributeFilter("id", "SiteMap1_lblTotalCount"));
				Span tag = (Span) lblTotalCount.elementAt(0);
				item.setPageIndex(Integer.parseInt(tag.getStringText()) / 40 + 1);
			} catch (ParserException e) {
				e.printStackTrace();
			}
			long endTime = System.currentTimeMillis();
			LogUtil.debugPrintf("抽取[" + url + "]结束[" + GenericUtil.dateformat.format(endTime) + "]耗时[" + (endTime - startTime) + "毫秒]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Parser getParser(String url) throws ParserException, IOException {
		this.query++;
		EquMessageService.inbound.send("grabber_query查询次数：" + this.query);
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
		long endTime = System.currentTimeMillis();
		LogUtil.infoPrintf("请求结束[" + GenericUtil.dateformat.format(endTime) + "]耗时[" + (endTime - startTime) + "毫秒]");
		return parser;
	}

	public String getState() {
		return state;
	}

}
