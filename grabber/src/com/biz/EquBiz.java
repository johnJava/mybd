package com.biz;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import org.htmlparser.Node;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;

import com.vo.EquItem;
import com.vo.VNode;
import com.web.EquMessageService;
@SuppressWarnings({ "unused" })
public class EquBiz {
	private EquItem item;
	private Queue<Node> nodequeue = new LinkedList<Node>();
	public Vector<String> orderNumVec = new Vector<String>();
	public Convert convert = new Convert();

	public EquBiz(EquItem item) {
		this.item = item;
	}
	public void addNode(Node node) {
		this.nodequeue.add(node);
		if (!convert.isAlive()) {
			convert = new Convert();
			convert.start();
		}
	}

	class Convert extends Thread {
		public void run() {
			while (true) {
				push();
			}
		}
	}

	public void push() {
		while (this.nodequeue.size() > 0) {
			Node node = this.nodequeue.poll();
			try {
				String nodehtml = toVoNode(node);
				if(!nodehtml.equalsIgnoreCase(""))
				EquMessageService.inbound.send("grabber_node" + nodehtml);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String toVoNode(Node node) {
		boolean flag=true;
		VNode vn = new VNode();
		NodeList all = node.getChildren();
		for (int j = 0; j < all.size(); j++) {
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
						//LogUtil.debugPrintf("title:" + tn.toHtml());
						LinkTag tag = (LinkTag) tn.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("href"), true).elementAt(0);
						String href = tag.getAttribute("href");
						String orderNum = href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf("."));
						vn.setOrderNum(orderNum);
						if(!addOrderNumVec(orderNum))return"";
						//LogUtil.debugPrintf("orderNum:" + orderNum);
					} else if (i == 3) {
						//LogUtil.debugPrintf("credit--->"+tn.toHtml());
						Span span=(Span) tn.getChildren().extractAllNodesThatMatch(new TagNameFilter("span"), true).elementAt(0);
						if(span!=null){
							if(!this.item.hasCredit(span.getAttribute("class"))){
								flag=false;
								return "";
							}
						}
						String credit = tn.toHtml().replaceAll("卖家信用：", "");
						vn.setCredit(credit);
						//LogUtil.debugPrintf("credit:" + credit);
					} else if (i == 7) {
						NodeList alist = tn.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("href"), true);
						String distinct = "";
						for (int k = 1; k < alist.size(); k++) {
							LinkTag tag = (LinkTag) alist.elementAt(j);
							distinct += tag.getStringText() + "/";
						}
						distinct = distinct.substring(0, distinct.length() - 2);
						vn.setDistinct(distinct);
						//LogUtil.debugPrintf("distinct:" + distinct);
					}
				}

			} else if ("pdlist_price".equalsIgnoreCase(classstr)) {
				String price = ul.getChildren().elementAt(1).toHtml();
				vn.setPrice(price);
				//LogUtil.debugPrintf("price:" + price);
			}
		}
		return vn.toString();
	}

	
	public boolean addOrderNumVec(String orderNum) {
		if(this.orderNumVec.contains(orderNum))return false;
		this.orderNumVec.add(orderNum);
		if (orderNumVec.size() > 80) {
			orderNumVec.remove(0);
		}
		return true;
	}
	
}
