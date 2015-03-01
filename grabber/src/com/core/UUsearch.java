package com.core;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.http.ConnectionManager;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

@SuppressWarnings({ "unchecked", "rawtypes","unused" })
public class UUsearch {
	private Map<String, List<Node>> dataMap;
	private  UUsearch uusearch;

	
	public UUsearch() {
		
		this.dataMap = new HashMap();
	}

	public List<Node> searchFilter(String url) throws ParserException, MalformedURLException, IOException {
		System.out.println("parserUrl:" + url);
		List<Node> dataSet = new ArrayList();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		long startTime = System.currentTimeMillis();
		System.out.println("抓取开始：" + format.format(startTime));
		ConnectionManager manager = Page.getConnectionManager();
		doSearchFilter(manager, dataSet, url, 0);
		long endTime = System.currentTimeMillis();
		System.out.println("抓取结束：" + format.format(endTime));
		System.out.println("耗时：" + (endTime - startTime) + "毫秒");
		return dataSet;
	}

	public void doSearchFilter(ConnectionManager manager, List<Node> dataSet, String url, int end) throws ParserException, MalformedURLException, IOException {
		Parser parser = new Parser(manager.openConnection(url));
		parser.setEncoding("UTF-8");
		NodeFilter nfilter = new HasAttributeFilter("class", "stock");
		NodeFilter innerFilter = new HasAttributeFilter("img");
		NodeFilter sfilter = new HasChildFilter(innerFilter);
		NodeFilter filter = new AndFilter(nfilter, sfilter);
		NodeList nodeP = parser.extractAllNodesThatMatch(new HasAttributeFilter("class", "list-item"));
		NodeList nodes = nodeP.extractAllNodesThatMatch(filter, true);
		if ((nodes != null) && (nodes.size() > 0)) {
			for (int i = 0; i < nodes.size(); i++) {
				Node textnode = nodes.elementAt(i);
				System.out.println("添加第[" + (dataSet.size() + 1) + "]条记录");
				dataSet.add(textnode.getParent().getParent());
			}
		}
		int npSize = nodeP.size();
		if (npSize < 100) {
			end++;
		}
		if ((nodeP.size() > 0) && (end < 2)) {
			int pagePre = Integer.parseInt(String.valueOf(url.charAt(url.length() - 1)));
			url = url.substring(0, url.length() - 1) + ++pagePre;
			doSearchFilter(manager, dataSet, url, end);
		}
	}

	public List<Node> getDataMap(String token, String url) throws ParserException, MalformedURLException, IOException {
		List<Node> result = null;
		if (!url.contains("&p=")) {
			result = searchFilter(url);
			this.dataMap.put(token, result);
		} else {
			result = (List) this.dataMap.get(token);
		}
		return result;
	}

	private class Extract extends Thread {
		private int begin;
		private int step;
		private boolean flag = true;
		private int end = 0;
		private ConnectionManager manager;
		private List<Node> dataSet;
		private String url;

		public Extract(ConnectionManager manager, List<Node> dataSet, String url, int begin, int step) {
			this.begin = begin;
			this.step = step;
			this.manager = manager;
			this.dataSet = dataSet;
			this.url = url + "&p=" + begin;
		}

		@Override
		public void run() {
			while (flag) {
				int pagePre = Integer.parseInt(String.valueOf(url.charAt(url.length() - 1)));
				url = url.substring(0, url.length() - 1) + (pagePre + this.step);
				Parser parser = null;
				try {
					parser = new Parser(manager.openConnection(url));
					parser.setEncoding("UTF-8");
				} catch (ParserException e2) {
					e2.printStackTrace();
				}
				NodeFilter nfilter = new HasAttributeFilter("class", "stock");
				NodeFilter innerFilter = new HasAttributeFilter("img");
				NodeFilter sfilter = new HasChildFilter(innerFilter);
				NodeFilter filter = new AndFilter(nfilter, sfilter);
				NodeList nodeP = null;
				try {
					nodeP = parser.extractAllNodesThatMatch(new HasAttributeFilter("class", "sin_pdlbox"));
				} catch (ParserException e) {
					e.printStackTrace();
				}
				NodeList nodes = nodeP.extractAllNodesThatMatch(filter, true);
				if ((nodes != null) && (nodes.size() > 0)) {
					for (int i = 0; i < nodes.size(); i++) {
						Node textnode = nodes.elementAt(i);
						System.out.println("添加第[" + (dataSet.size() + 1) + "]条记录");
						dataSet.add(textnode.getParent().getParent());
					}
				}
				int npSize = nodeP.size();
				if (npSize < 100) {
					end++;
				}
				if ((nodeP.size() == 0) || end == 2) {
					this.flag = false;
				}

			}

		}

		public boolean isFlag() {
			return flag;
		}

	}
}
