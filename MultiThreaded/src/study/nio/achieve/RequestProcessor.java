package study.nio.achieve;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class RequestProcessor implements Runnable {
	private static List pool = new LinkedList();
	private File documentRootDirectory;
	private String indexFileName = "index.htm";

	public RequestProcessor(File documentRootDirectory, String indexFileName) {
		if (documentRootDirectory.isFile())
			throw new IllegalArgumentException("documentRootDirectory must be a directory,not a file");
		try {
			this.documentRootDirectory = documentRootDirectory.getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null != indexFileName)
			this.indexFileName = indexFileName;
	}

	public static void processRequest(Socket request) {
		synchronized (pool) {
			pool.add(pool.size(), request);
			pool.notifyAll();
		}
	}

	@Override
	public void run() {
		String root = documentRootDirectory.getPath();
		// 安全检查
		while (true) {
			Socket conn;
			synchronized (pool) {
				if (pool.size()==0) {
					try {
						pool.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(pool.size()==0) continue;
				conn = (Socket) pool.remove(0);
				System.out.println("client:" + conn.getPort());
				try {
					String filename;
					String contentType;
					BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
					BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
					// 只读取第一行;这是所需的全部内容
					StringBuffer requestbf = new StringBuffer();
					while (true) {
						int c = in.read();
						if (c == '\r' || c == '\n' || c == -1)
							break;
						requestbf.append((char) c);
					}
					String get  = requestbf.toString();
					//记录请求日志
					System.out.println(get);
					StringTokenizer st = new StringTokenizer(get);
					String method = st.nextToken();
					String version ="";
					if(method.equalsIgnoreCase("get")){
						filename = st.nextToken();
						if(filename.equals("/")) filename=indexFileName;
						contentType = guessContentTypeFromName(filename.substring(1,filename.length()));
						if(st.hasMoreTokens()){
							version= st.nextToken();
						}
						File theFile = new File(documentRootDirectory,filename);//.substring(1,filename.length())
						if(theFile.canRead()&&theFile.getCanonicalPath().startsWith(root)){
							DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(theFile)));
							byte[] theData = new byte[(int)theFile.length()];
							fis.readFully(theData);
							fis.close();
							if(version.startsWith("HTTP")){
								StringBuffer header = new  StringBuffer();
								header.append("HTTP/1.0 200 OK\r\n");
								header.append("Date: "+new Date().toString()+"\r\n");
								header.append("Server: JHTTP/1.0\r\n");
								header.append("Content-length: "+theData.length+"\r\n");
								header.append("Content-type: "+contentType+"\r\n\r\n");
								out.write(header.toString().getBytes());
								out.flush();
							}
							out.write(theData);
							out.flush();
						}else{
							if(version.startsWith("HTTP ")){
								StringBuffer header = new  StringBuffer();
								header.append("HTTP/1.0 404 File Not Found\r\n");
								header.append("Date: "+new Date().toString()+"\r\n");
								header.append("Server: JHTTP/1.0\r\n");
								header.append("Content-type: text/html\r\n\r\n");
								out.write(header.toString().getBytes());
								out.flush();
							}
							out.write("File Not Found".getBytes());
							out.flush();
						}
					}else{//方法不等于“GET”
						if(version.startsWith("HTTP ")){
							StringBuffer header = new  StringBuffer();
							header.append("HTTP/1.0 501  Not Implemented\r\n");
							header.append("Date: "+new Date().toString()+"\r\n");
							header.append("Server: JHTTP/1.0\r\n");
							header.append("Content-type: text/html\r\n\r\n");
							out.write(header.toString().getBytes());
							out.flush();
						}
						//String re="<html><head><title><title></head></html>";
						out.write("File Not Found".getBytes());
						out.flush();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						conn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	public static String guessContentTypeFromName(String filename){
		String type="";
		if(filename.endsWith(".html")||filename.endsWith(".htm")){
			type= "text/html";
		}else if(filename.endsWith(".txt")||filename.endsWith(".java")){
			type= "text/plain";
		}else if(filename.endsWith(".gif")){
			type= "image/gif";
		}else if(filename.endsWith(".jpg")||filename.endsWith(".jpeg")){
			type=  "image/jpeg";
		}
		return type;
	}

}
