package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.core.ThreadProxy;

@SuppressWarnings({ "unused", "deprecation" })
public class HelloMessageInbound extends StreamInbound {  
	  
    private String WS_NAME;  
    private final String FORMAT = "%s : %s";  
	private final String PREFIX = "ws_";  
	private String sessionId = "";  
  
    public HelloMessageInbound(int id, String _sessionId) {  
        //this.WS_NAME = PREFIX + id; 
        this.WS_NAME ="新单";    
        this.sessionId = _sessionId;  
    }  
  
    @Override  
    protected void onTextData(Reader reader) throws IOException {  
        char[] chArr = new char[1024];  
        int len = reader.read(chArr);  
        send(String.copyValueOf(chArr, 0, len));  
    }  
  
    @Override  
    protected void onClose(int status) {  
        System.out.println(String.format(FORMAT, WS_NAME, "closing ......"));  
        super.onClose(status);  
    }  
  
    @Override  
    protected void onOpen(WsOutbound outbound) {  
        super.onOpen(outbound);  
           // send("hello, my name is " + WS_NAME);  
            //send("session id = " + this.sessionId);
            String keyword=new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
            ThreadProxy tp=new ThreadProxy("http://s.5173.com/dnf-0-0-0-0-2zkpio-0-0-0-a-a-a-a-a-0-addnewdate_desc-0-0.shtml"
					  ,"http://s.5173.com/dnf-0-0-0-0-2zkpio-0-0-0-a-a-a-a-a-1-addnewdate_desc-0-0.shtml");//?keyword="+keyword
            tp.update();
    }  
  
    public void send(String message) throws IOException {  
    	if(message.length()>20){
    		message = String.format(FORMAT, WS_NAME, message);  
    	}
        System.out.println("message======"+message);  
        getWsOutbound().writeTextMessage(CharBuffer.wrap(message));  
    }  
  
    @Override  
    protected void onBinaryData(InputStream arg0) throws IOException {  
    }  
}  
