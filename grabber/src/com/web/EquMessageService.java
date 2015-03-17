package com.web;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

import com.common.GenericUtil;

@SuppressWarnings("deprecation")
public class EquMessageService extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
	public static EquMessageInbound inbound = null;

	private final AtomicInteger connectionIds = new AtomicInteger(0);
	private String sessionid="";

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request) {
		String datestr = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		if (!GenericUtil.isEndDate()) {
			if (inbound == null||!this.sessionid.equals(request.getSession().getId())){
				this.sessionid=request.getSession().getId();
				inbound = new EquMessageInbound(connectionIds.getAndIncrement(), this.sessionid);
			}
			return inbound;
		}else{
			System.out.println("软件已经过期，请联系供应商！");
			return null;
		}
	}

}
