package test;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.common.LogUtil;
import com.vo.VNode;

public class TestProxyIp {
	@org.junit.Test
	public void getNodeName() throws ParserException {
		String url = "http://s.5173.com/dnf-0-0-0-0-2zkpio-0-0-0-a-a-a-a-a-0-0-0-0.shtml?keyword=20141221";
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
		try {
			NodeList result = parser.extractAllNodesThatMatch(new HasAttributeFilter("class", "sin_pdlbox"));
			// NodeList all= result.elementAt(0).getChildren();
			// for (int i = 1; i < all.size(); i+=2) {
			// Node tnode= all.elementAt(i);
			// if(!(tnode instanceof BulletList))continue;
			// //extracVnodeArr(tnode);
			// }
			System.out.println("to:" + toVoNode(result.elementAt(0)));
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	public String toVoNode(Node node) {
		VNode vn = new VNode();
		NodeList all = node.getChildren();
		for (int j = 1; j < all.size(); j += 2) {
			Node tnode = all.elementAt(j);
			if (!(tnode instanceof BulletList))
				continue;
			BulletList ul = (BulletList) tnode;
			String classstr = ul.getAttribute("class");
			if ("pdlist_info".equalsIgnoreCase(classstr)) {
				NodeList ls = ul.getChildren();
				for (int i = 1; i < ls.size(); i += 2) {
					Node tn = ls.elementAt(i);
					if (i == 1) {
						vn.setTitle(tn.toHtml());
						LogUtil.debugPrintf("title:" + tn.toHtml());
						LinkTag tag = (LinkTag) tn.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("href"), true).elementAt(0);
						String href = tag.getAttribute("href");
						String orderNum = href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf("."));
						vn.setOrderNum(orderNum);
						LogUtil.debugPrintf("orderNum:" + orderNum);
					} else if (i == 3) {
						String credit = tn.toHtml().replaceAll("卖家信用：<a", "<a");
						vn.setCredit(credit);
						LogUtil.debugPrintf("credit:" + credit);
					} else if (i == 7) {
						NodeList alist = tn.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("href"), true);
						String distinct = "";
						for (int k = 1; k < alist.size(); k++) {
							LinkTag tag = (LinkTag) alist.elementAt(j);
							distinct += tag.getStringText() + "/";
						}
						distinct = distinct.substring(0, distinct.length() - 2);
						vn.setDistinct(distinct);
						LogUtil.debugPrintf("distinct:" + distinct);
					}
				}

			} else if ("pdlist_price".equalsIgnoreCase(classstr)) {
				String price = ul.getChildren().elementAt(1).toHtml();
				vn.setPrice(price);
				LogUtil.debugPrintf("price:" + price);
			}
		}
		return vn.toString();
	}

	public void extracVnodeArr(Node tnode) {
		BulletList ul = (BulletList) tnode;
		String classstr = ul.getAttribute("class");
		if ("pdlist_info".equalsIgnoreCase(classstr)) {
			NodeList ls = ul.getChildren();
			for (int i = 1; i < ls.size(); i += 2) {
				Node tn = ls.elementAt(i);
				if (i == 1) {
					LogUtil.infoPrintf("title:" + tn.toHtml());
					LinkTag tag = (LinkTag) tn.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("href"), true).elementAt(0);
					String href = tag.getAttribute("href");
					href = href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf("."));
					LogUtil.infoPrintf("orderNum:" + href);
				} else if (i == 3) {
					LogUtil.infoPrintf("credit:" + tn.toHtml().replaceAll("卖家信用：<a", "<a"));
				} else if (i == 7) {
					NodeList alist = tn.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("href"), true);
					String distinct = "";
					for (int j = 1; j < alist.size(); j++) {
						LinkTag tag = (LinkTag) alist.elementAt(j);
						distinct += tag.getStringText() + "/";
					}
					LogUtil.infoPrintf("distinct:" + distinct.substring(0, distinct.length() - 2));
				}
			}

		} else if ("pdlist_price".equalsIgnoreCase(classstr)) {
			LogUtil.infoPrintf("price:" + ul.getChildren().elementAt(1).toHtml());
		}

	}

	public static void main(String args[]) {
		String price="<li class=\"pr\"><strong>375.00</strong></li>";
		price=price.substring(price.indexOf("<strong>")+"<strong>".length(),price.indexOf("</strong>"));
		System.out.println(price);
//		Test test = new Test();
//		// test.start();
//		int count = 10;
//		while (count > 0) {
//			// System.out.println("main scan----------");
//			if (!test.isAlive()) {
//				test = new Test();
//				test.start();
//				count--;
//			}
//
//		}

		// System.getProperties().setProperty("proxySet", "true");
		// //如果不设置，只要代理IP和代理端口正确,此项不设置也可以
		// System.getProperties().setProperty("http.proxyHost", "119.6.144.70");
		// System.getProperties().setProperty("http.proxyPort", "82");
		// System.out.println(getHtml("http://www.ip138.com/ip2city.asp"));
		// //判断代理是否设置成功
	}

	@SuppressWarnings("unused")
	private static String getHtml(String address) {
		StringBuffer html = new StringBuffer();
		String result = null;
		try {
			URL url = new URL(address);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
			BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
			try {
				String inputLine;
				byte[] buf = new byte[4096];
				int bytesRead = 0;
				while (bytesRead >= 0) {
					inputLine = new String(buf, 0, bytesRead, "ISO-8859-1");
					html.append(inputLine);
					bytesRead = in.read(buf);
					inputLine = null;
				}
				buf = null;
			} finally {
				in.close();
				conn = null;
				url = null;
			}
			result = new String(html.toString().trim().getBytes("ISO-8859-1"), "gb2312").toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		html = null;
		return result;
	}
}
