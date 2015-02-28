package study.nio.achieve;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JHTTP extends Thread {

	private File documentRootDirectory;
	private String indexFileName = "index.html";
	private ServerSocket server;
	private int numThreads = 50;

	public JHTTP(File documentRootDirectory, int port, String indexFileName) throws IOException {
		if (documentRootDirectory.isFile())
			throw new IllegalArgumentException("documentRootDirectory must be a directory,not a file");
		this.documentRootDirectory = documentRootDirectory;
		if (null != indexFileName)
			this.indexFileName = indexFileName;
		this.server= new ServerSocket(port);
	}

	
	
	@Override
	public void run() {
		for (int i = 0; i < this.numThreads; i++) {
			Thread t = new Thread(new RequestProcessor(documentRootDirectory, indexFileName));
			t.start();
		}
		System.out.println("Accepting connections on port "+server.getLocalPort());
		System.out.println("Document Root: "+documentRootDirectory);
		while(true){
			try {
				Socket request = server.accept();
				RequestProcessor.processRequest(request);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	public static void main(String[] args) throws IOException {
		new JHTTP(new File("F:\\documentRootDirectory"), 80, "index.htm").start();
	}

}
