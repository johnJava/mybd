package study.nio.achieve;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Redirector implements Runnable {
	private int port;
	private String newSite;

	public Redirector(int port, String newSite) {
		this.port = port;
		this.newSite = newSite;
	}

	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(this.port);
			System.out.println("Redirecting connections on port "+server.getLocalPort()+" to "+newSite);
			while(true){
				Socket s = server.accept();
				Thread t = new RedirectThread(s);
				t.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class RedirectThread extends Thread {
		private Socket conn;

		public RedirectThread(Socket conn) {
			this.conn = conn;
		}

		@Override
		public void run() {
			System.out.println("client:" + conn.getPort());
			try {
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
				String get = request.toString();
				int firstspace = get.indexOf(' ');
				int secondspace = get.indexOf(' ',1+firstspace);
				String theFile=get.substring(1+firstspace,secondspace);
				if(get.indexOf("HTTP")!=-1){
					StringBuffer content = new StringBuffer(2000);
					content.append("HTTP/1.0 302 FOUND\r\n");
					content.append("Date:"+new Date().toString()+"\r\n");
					content.append("Server: Redirector 1.0\r\n");
					content.append("Location: "+newSite+theFile+"\r\n");
					content.append("Content-type: text/html\r\n\r\n");
					out.write(content.toString().getBytes());
					out.flush();
				}
				out.write("moved...".getBytes());
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(conn!=null)
					try {
						conn.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	}
	public static void main(String[] args) {
		new Thread(new Redirector(80, "https://news.ycombinator.com/")).start();
	}
}
