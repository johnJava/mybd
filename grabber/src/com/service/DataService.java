package com.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.core.UUsearch;

//@WebServlet(name = "UuService", urlPatterns = { "/uuService" })
public class DataService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final String baseUrl = "http://www.uu898.com/Trade.aspx?";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doAction(request, response);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doAction(request, response);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParserException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		System.out.println("requestIp:" + request.getRemoteAddr());
		StringBuffer urlBuffer = new StringBuffer(500);
		urlBuffer.append("http://www.uu898.com/Trade.aspx?");
		Map<String, String[]> paras = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : paras.entrySet()) {
			urlBuffer.append(new String(((String) entry.getKey()).trim().getBytes("ISO-8859-1"), "GBK") + "=" + new String(((String[]) entry.getValue())[0].trim().getBytes("ISO-8859-1"), "GBK"));
			urlBuffer.append("&");
		}
		if (urlBuffer.indexOf("&") != -1) {
			urlBuffer.deleteCharAt(urlBuffer.lastIndexOf("&"));
		}
		String url = urlBuffer.toString();
		System.out.println("requestPara:" + new String(url.trim().getBytes("gbk"), "UTF-8"));
		if(paras.size()==0){
			System.out.println("重定向:index.html");
			request.getRequestDispatcher("index.html").forward(request, response);
		}else if(!paras.containsKey("file")) {
			doSearch(request, response, url);
		} else {
			getHtml(request, response, "http://www.uu898.com" + ((String[]) paras.get("file"))[0]);
		}
	}

	private void doSearch(HttpServletRequest request, HttpServletResponse response, String url) throws MalformedURLException, IOException {
		UUsearch uu = new UUsearch();
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head id=\"ctl00_Head1\">");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html\"; charset=\"gb2312\">");
		out.println("<link href=\"css/jiathis_share.css\" rel=\"stylesheet\" type=\"text/css\">");
		out.println("<link href=\"css/trade_20111029.css?20130220\" rel=\"stylesheet\" type=\"text/css\">");
		out.println("<link href=\"css/base.css\" rel=\"stylesheet\" type=\"text/css\">");
		out.println("<link href=\"css/top.css\" rel=\"stylesheet\" type=\"text/css\">");
		out.println("<link href=\"css/gotop.css\" rel=\"stylesheet\" type=\"text/css\">");
		out.println("<style type=\"text/css\">.qqlogin { background:url(http://www.uu898.com/files/api_ico.jpg) no-repeat 0 -90px; display:inline-block; height:16px; width:70px; text-decoration:none; text-indent:-999em;}</style>");
		out.println("<script src=\"js/jquery.js\" type=\"text/javascript\"></script>");
		out.println("<script src=\"js/bee.js\" type=\"text/javascript\"></script>");
		out.println("<script defer=\"defer\" src=\"js/listData.js?20121229\" type=\"text/javascript\"></script>");
		out.println("<script defer=\"defer\" src=\"js/top_select.js?20130907\" type=\"text/javascript\"></script>");
		out.println("<script defer=\"defer\" src=\"js/20110920.js\" type=\"text/javascript\"></script>");
		out.println("<script defer=\"defer\" src=\"http://www.uu898.com/static/inc/gameserver.js\" type=\"text/javascript\"></script>");
		out.println("</head>");
		out.println("<body >");
		out.write("<form id=\"aspnetForm\" action=\"uuService?\"  method=\"get\" name=\"aspnetForm\">");
		out.write("<div id=\"doc\" class=\"layout\">");
		out.write("<div id=\"listlayout\" class=\"content area960\">");
		out.write("<div class=\"navcon\"> <div class=\"nl fl\"></div> <div class=\"con fl\"> \t<div class=\"gm-search\"> \t\t<div class=\"gm-s-m gmarea\"> \t\t\t<div class=\"gm-m-top\"> \t\t\t\t<div class=\"_gm_left\"> \t\t\t\t\t<table> \t\t\t\t\t\t<tr> \t\t\t\t\t\t\t<td> \t\t\t\t\t\t\t\t<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"800\"> \t\t\t\t\t\t\t\t\t<tr> \t\t\t\t\t\t\t\t\t\t<td width=\"5\"></td> \t\t\t\t\t\t\t\t\t\t<td> \t\t\t\t\t\t\t\t\t\t\t<div class=\"gm-m\" onclick=\"openGameSelect();\"> \t\t\t\t\t\t\t\t\t\t\t\t<div id=\"selectedGame\" class=\"autoHidden tCenter vMiddle\" \t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"float: left; width: 99px; overflow: hidden; height: 20px;\"> \t\t\t\t\t\t\t\t\t\t\t\t\t 选择服务器</div> \t\t\t\t\t\t\t\t\t\t\t</div></td> \t\t\t\t\t\t\t\t\t\t<td width=\"5\"></td> \t\t\t\t\t\t\t\t\t\t<td> \t\t\t\t\t\t\t\t\t\t\t<div class=\"gm-m\" onclick=\"openAreaSelect()\"> \t\t\t\t\t\t\t\t\t\t\t\t<div id=\"selectedArea\" class=\"autoHidden tCenter vMiddle\" \t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"float: left; width: 99px; overflow: hidden; height: 20px;\"> \t\t\t\t\t\t\t\t\t\t\t\t\t请选择游戏区</div> \t\t\t\t\t\t\t\t\t\t\t</div></td> \t\t\t\t\t\t\t\t\t\t<td width=\"5\"></td> \t\t\t\t\t\t\t\t\t\t<td> \t\t\t\t\t\t\t\t\t\t\t<div class=\"gm-m\" onclick=\"openServerSelect();\"> \t\t\t\t\t\t\t\t\t\t\t\t<div id=\"selectedServer\" class=\"autoHidden tCenter vMiddle\" \t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"float: left; width: 99px; overflow: hidden; height: 20px;\"> \t\t\t\t\t\t\t\t\t\t\t\t\t请选择服务器</div> \t\t\t\t\t\t\t\t\t\t\t</div></td> \t\t\t\t\t\t\t\t\t\t<td width=\"5\"></td> \t\t\t\t\t\t\t\t\t\t<td> \t\t\t\t\t\t\t\t\t\t\t<div class=\"gm-m\" onclick=\"openCTypes()\"> \t\t\t\t\t\t\t\t\t\t\t\t<div id=\"selectedCType\" class=\"autoHidden tCenter vMiddle\" \t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"float: left; width: 99px; overflow: hidden; height: 20px;\"> \t物品类型</div> \t\t\t\t\t\t\t\t\t\t\t</div> \t\t\t\t\t\t\t\t\t\t\t<div id=\"cTypeDiv\" \t\t\t\t\t\t\t\t\t\t\t\tstyle=\"position: absolute; top: 40px; left: 7px; width: 100%; height: 2px; background: #fff;\"> \t\t\t\t\t\t\t\t\t\t\t</div></td> \t\t\t\t\t\t\t\t\t\t<td width=\"5\"></td> \t\t\t\t\t\t\t\t\t\t<td> \t\t\t\t\t\t\t\t\t\t\t<div class=\"gm-m\" onclick=\"openBTypes()\"> \t\t\t\t\t\t\t\t\t\t\t\t<div id=\"selectedBType\" class=\"autoHidden tCenter vMiddle\" \t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"float: left; width: 99px;\">出售</div> \t\t\t\t\t\t\t\t\t\t\t</div> \t\t\t\t\t\t\t\t\t\t\t<div id=\"bTypeDiv\" \t\t\t\t\t\t\t\t\t\t\t\tstyle=\"position: absolute; top: 40px; left: 7px; width: 100%; height: 1px; background: #fff;\"> \t\t\t\t\t\t\t\t\t\t\t</div></td> \t\t\t\t\t\t\t\t\t\t<td width=\"5\"></td> \t\t\t\t\t\t\t\t\t\t<td><input type=\"text\" class=\"text_hui\" name=\"txtKeys\" \t\t\t\t\t\t\t\t\t\t\tstyle=\"width: 160px; line-height: 21px;\" id=\"txtKeys\" \t\t\t\t\t\t\t\t\t\t\tonblur=\"keys_blur('请输入关键字 比如输入赤眼')\" \t\t\t\t\t\t\t\t\t\t\tonfocus=\"keys_focus('请输入关键字 比如输入赤眼')\" value=\"请输入关键字 比如输入赤眼\" /> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t\t<td width=\"20\">&nbsp;&nbsp;</td> \t\t\t\t\t\t\t\t\t\t<td><img \t\t\t\t\t\t\t\t\t\t\tsrc=\"http://www.uu898.com/files/text_submit_search.jpg\" \t\t\t\t\t\t\t\t\t\t\tstyle=\"cursor: pointer;\" onclick=\"sendSearch();\" /></td> \t\t\t\t\t\t\t\t\t</tr> \t\t\t\t\t\t\t\t</table></td> \t\t\t\t\t\t</tr> \t\t\t\t\t\t<tr> \t\t\t\t\t\t\t<td style=\"display: none; height: 1px; background: #fff;\" \t\t\t\t\t\t\t\tid=\"itemsTd\"> \t\t\t\t\t\t\t\t<div \t\t\t\t\t\t\t\t\tstyle=\"position: relative; width: 100%; height: 1px; background: #fff;\" \t\t\t\t\t\t\t\t\tid=\"theItemsDiv\"></div></td> \t\t\t\t\t\t</tr> \t\t\t\t\t</table> \t\t\t\t</div> \t\t\t</div> \t\t</div> \t</div> </div> <div class=\"nr fr\"></div> <div class=\"clear\"></div></div>");
		out.write("<div class=\"col-main\">");
		out.write("<div style=\"width:960px\">");
		out.write("<div id=\"auctions\" class=\"list list-optimized\">");
		out.write("<div id=\"filter\">");
		out.write("<ul class=\"toolbar\"> <li class=\"title\">信息标题/物品类型/游戏/区/服</li> <li class=\"soldtype\">诚信保障</li> <li id=\"ctl00_ContentPlaceHolder1_commodityListUC1_li_price\" class=\"price\"> <a id=\"ctl00_ContentPlaceHolder1_commodityListUC1_lbtn_price\" class=\"price\" href=\"?gm=115&area=&srv=&cmp=&b=&c=-2&p=1&ps=&o=2&sa=2&recommend=&key=&\">价格</a> </li> <li class=\"stock\">库存</li> <li id=\"ctl00_ContentPlaceHolder1_commodityListUC1_li_unitPrice\" class=\"price\"> <span style=\"display: none\"> <a id=\"ctl00_ContentPlaceHolder1_commodityListUC1_lbtn_unitPrice\" title=\"按比例由高到低排序\" href=\"?gm=115&area=&srv=&cmp=&b=&c=-2&p=1&ps=&o=5&sa=0&recommend=&key=&\">单价</a> <a id=\"ctl00_ContentPlaceHolder1_commodityListUC1_taopianyi\" class=\"taopianyi\" href=\"?gm=115&area=&srv=&cmp=&b=&c=-2&p=1&ps=&o=5&sa=0&recommend=&key=&\">点这里，更便宜</a> </span> </li> <li class=\"allselect\"> <input id=\"ChkIsBuy\" type=\"checkbox\" onclick=\"SelectAllChkbox(this.checked)\" name=\"SaveAll\"> 全选 </li> <li class=\"liveday\">剩余时间</li> </ul>");
		try {
			List<Node> ls = uu.getDataMap(url.substring(url.indexOf('?') + 1), url);
			if (ls.size() == 0) {
				out.write("无交易完成记录！");
			} else {
				StringBuffer content = new StringBuffer(5000);
				content.append("<div id=\"auction-list\" class=\"list-content\">");
				content.append("<ul class=\"list-view\">");
				for (int i = 0; i < ls.size(); i++) {
					content.append(((Node) ls.get(i)).toHtml());
				}
				content.append("</ul>");
				content.append("</div>");
				String contentHtml = content.toString();
				contentHtml = contentHtml.replaceAll("src='/", "src='http://www.uu898.com/");
				contentHtml = contentHtml.replaceAll("src='images/", "src='http://www.uu898.com/images/");
				contentHtml = contentHtml.replaceAll("href=\"/", "href=\"http://www.uu898.com/");
				out.write(contentHtml);
			}
			out.write("<input type=\"hidden\" name=\"ctl00$selectedTitleField\" id=\"ctl00_selectedTitleField\" /> <input type=\"hidden\" name=\"ctl00$gameIDField\" id=\"ctl00_gameIDField\" /> <input type=\"hidden\" name=\"ctl00$areaIDField\" id=\"ctl00_areaIDField\" /> <input type=\"hidden\" name=\"ctl00$serverIDField\" id=\"ctl00_serverIDField\" /> <input type=\"hidden\" name=\"ctl00$comTypeField\" id=\"ctl00_comTypeField\" />");
		} catch (ParserException e) {
			e.printStackTrace();
		}
		out.write("</div>");
		out.write("</div>");
		out.write("</div>");
		out.write("</div>");
		out.write("</form>");
		out.println("</body>");
		out.println("</html>");
		out.flush();
	}

	private void getHtml(HttpServletRequest request, HttpServletResponse response, String url) throws MalformedURLException, IOException, ParserException {
		System.out.println("url:" + url);
		Parser parser = new Parser((HttpURLConnection) new URL(url).openConnection());
		parser.setEncoding("UTF-8");
		NodeList ls = parser.parse(null);
		PrintWriter out = response.getWriter();
		out.write(ls.toHtml().substring(1));
		out.flush();
	}
}
