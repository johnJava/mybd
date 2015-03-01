package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.CharBuffer;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.core.ThreadProxy;

@SuppressWarnings("deprecation")
public class MessageInbound extends StreamInbound {  
	  
    private String WS_NAME;  
    private final String FORMAT = "%s : %s";  
    private final String PREFIX = "ws_";  
    private String sessionId = "";  
  
    public MessageInbound(int id, String _sessionId) {  
        this.WS_NAME = PREFIX + id;  
        this.sessionId = _sessionId;  
    }  
  
    @Override  
    protected void onTextData(Reader reader) throws IOException {  
        char[] chArr = new char[1024];  
        int len = reader.read(chArr);  
        System.out.println("recivemsg:"+(String.copyValueOf(chArr, 0, len)));  
    }  
  
    @Override  
    protected void onClose(int status) {  
        System.out.println(String.format(FORMAT, WS_NAME, "closing ......"));  
        super.onClose(status);  
    }  
  
    @Override  
    protected void onOpen(WsOutbound outbound) {  
        super.onOpen(outbound);  
        	System.out.println("hello, my name is " + WS_NAME);  
        	System.out.println("session id = " + this.sessionId);  
         ThreadProxy tp=new ThreadProxy("http://s.5173.com/dnf-0-0-0-0-2zkpio-0-0-0-a-a-a-a-a-0-0-0-0.shtml?keyword=20141221"
				  ,"http://s.5173.com/dnf-0-0-0-0-2zkpio-0-0-0-a-a-a-a-a-1-0-0-0.shtml?keyword=20141221");
         tp.update();
    }  
  
    public void send(String message) throws IOException {  
        //message = String.format(FORMAT, WS_NAME, message);  
        System.out.println("pushmsg:"+message);  
        getWsOutbound().writeTextMessage(CharBuffer.wrap(message));  
    }  
  
    @Override  
    protected void onBinaryData(InputStream arg0) throws IOException {  
    }  
}  
