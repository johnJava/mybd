package study.nio.achieve;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleFileHTTPServer extends Thread {
	private byte[] header;
	private byte[] content;
	private static int defaultport = 80;
	private int port;

	public SingleFileHTTPServer(byte[] data, String encoding, String MIMEType) throws UnsupportedEncodingException {
		this(data, encoding, MIMEType, defaultport);
	}

	public SingleFileHTTPServer(byte[] data, String encoding, String MIMEType, int port) throws UnsupportedEncodingException {
		this.content = data;
		this.port = port;
		String header = "HTTP/1.0 200 OK\r\n" + "Server: OneFile 1.0\r\n" + "Content-length: " + this.content.length + "\r\n\r\n" + "Content-type: " + MIMEType + "\r\n\r\n";
		this.header = header.getBytes("ASCII");
	}

	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("Accepting connections on port " + server.getLocalPort());
			System.out.println("Data to sent...");
			System.out.write(this.content);
			while (true) {
				Socket conn = null;
				try {
					conn = server.accept();
					System.out.println("client:"+conn.getPort());
					BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
					BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
					// 只读取第一行;这是所需的全部内容
					StringBuffer request = new StringBuffer(80);
					while (true) {
						int c = in.read();
						if (c == '\r' || c == '\n' || c == -1)
							break;
						request.append((char) c);
					}
					// 如果HTTP/1.0或以后版本，就发送一个MIME首部
					if (request.toString().indexOf("HTTP/") != -1) {
						out.write(this.header);
					}
					out.write(this.content);
					out.flush();
				} catch (IOException e) {

				} finally {
					if (conn != null)
						conn.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		args = new String[3];
		args[0]="F:\\HackerNews.htm";
		args[1]="80";
		args[2]="UTF-8";
		String contentType = "text/plain";
		if(args[0].endsWith(".html")||args[0].endsWith(".html")){
			contentType = "text/html";
		}
		FileInputStream in = new FileInputStream(args[0]);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int b;
		while((b=in.read())!=-1)out.write(b);
		byte[] data = out.toByteArray();
		//设置监听端口
		int port;
		try{
			port = Integer.parseInt(args[1]);
			if(port<1||port>65535) port=80;
		}catch(Exception e){
			port=80;
		}
		String encoding = "ASCII";
		if(args.length>2)encoding = args[2];
		Thread t = new SingleFileHTTPServer(data, encoding, contentType, port);
		t.start();
	}
}
