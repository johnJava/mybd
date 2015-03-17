package com.vo;

import com.common.GenericUtil;

public class VNode {
	private String title;
	private String price;
	private String credit;
	private String distinct;
	private String orderNum;
	//private String updateTime;
	@Override
	public String toString() {
		StringBuffer nodeHtml =new StringBuffer(2000);
		String price=this.price;
		price=price.substring(price.indexOf("<strong>")+"<strong>".length(),price.indexOf("</strong>"));
		nodeHtml.append("<div class=\"sin_pdlbox\"> ");
		nodeHtml.append("<ul class=\"pdlist_info\">");
		nodeHtml.append(this.title);
		nodeHtml.append("</ul> ");
		nodeHtml.append("<ul class=\"pdlist_price\">");
		String priceHtml=this.price;
		priceHtml=priceHtml.replace(price, price.substring(0, price.indexOf('.')));
		nodeHtml.append(priceHtml);
		nodeHtml.append("</ul> ");
		nodeHtml.append("<ul class=\"pdlist_ensure\">");
		String params="{price:'"+price+"',orderNum:'"+this.orderNum+"'}";
		nodeHtml.append("<li> <input type=\"button\" value=\"下单\"  class=\"gs_search_btn\" onclick=\"shop("+params+")\" /> </li> ");
		nodeHtml.append("</ul>");
		nodeHtml.append("<ul class=\"pdlist_num\">");
		nodeHtml.append(this.distinct);
		nodeHtml.append("</ul> ");
		nodeHtml.append("<ul class=\"pdlist_num\">");
		nodeHtml.append(this.credit);
		
		nodeHtml.append("</ul> ");
		nodeHtml.append("<ul class=\"pdlist_delivery\">");
		nodeHtml.append(this.orderNum);
		nodeHtml.append("</ul> ");
		nodeHtml.append("<ul class=\"pdlist_time\">");
		nodeHtml.append(GenericUtil.dateformat.format(System.currentTimeMillis()) );
		nodeHtml.append("</ul> ");
		nodeHtml.append("</div> ");
		return nodeHtml.toString();
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public void setDistinct(String distinct) {
		this.distinct = distinct;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
}
