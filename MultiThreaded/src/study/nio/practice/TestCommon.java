package study.nio.practice;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import org.junit.Test;

public class TestCommon {

	public static void main(String[] args) {
		 server();
	}

	public static void server() {
		try {
			ServerSocket server = new ServerSocket(8888);
			Socket conn = server.accept();
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(new Date().toString() + "\r\n");
			out.flush();
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void portScan() {
		String host = "localhost";
		for (int i = 1; i < 1024; i++) {
			try {
				Socket client = new Socket(host, i);
				System.out.println("There is a server on port " + i + " of " + host);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
				// e.printStackTrace();
				System.err.println(e.getMessage() + " " + i);
			}
		}
	}

	public void client() {
		String host = "localhost";
		try {
			Socket client = new Socket(host, 8888);
			InputStream ins = client.getInputStream();
			StringBuffer time = new StringBuffer();
			int readline;
			while((readline=ins.read())!=-1){
				time.append((char)readline);
			}
			String timestr=time.toString().trim();
			System.out.println("It is "+timestr+" at localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
