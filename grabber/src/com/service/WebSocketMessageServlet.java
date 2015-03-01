package com.service;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;




/**
 * Servlet implementation class WebSocketMessageServlet
 */
@SuppressWarnings("deprecation")
public class WebSocketMessageServlet extends  WebSocketServlet {
    private static final long serialVersionUID = 1L;  
    public static HelloMessageInbound inbound =null;
    
    private final AtomicInteger connectionIds = new AtomicInteger(0);  
	 @Override  
	    protected StreamInbound createWebSocketInbound(String arg0,  
	            HttpServletRequest request) {  
		if(inbound==null)inbound = new HelloMessageInbound(connectionIds.getAndIncrement(), request  
	                .getSession().getId());
	        return inbound;  
	    } 
       

}
