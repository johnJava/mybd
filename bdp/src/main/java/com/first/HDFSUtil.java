package com.first;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

public class HDFSUtil {
	static Configuration conf = new Configuration();
	static FileSystem hdfs = null;
	static String dir = "/user/eam/upload/";
	static {
		System.setProperty("HADOOP_USER_NAME","hdfs");
		conf.set("fs.defaultFS", "hdfs://CH5:8020");
		try {
			hdfs = FileSystem.get(conf);
		} catch (IOException e) {
			System.out.println("连接HDFS失败!");
			e.printStackTrace();
			hdfs = null;
		}
	}

	public boolean uploadSmallFile(String fileName, byte[] bytes)
			throws IOException {
		checkHDFS();
		Path dst = new Path(dir + fileName);
		FSDataOutputStream output = hdfs.create(dst);
		output.write(bytes);
		output.hflush();
		output.flush();
		output.close();
		System.out.println(fileName + " is uploaded.");
		return true;
	}

	public boolean uploadBigFile(String fileName, FileChannel fc)
			throws IOException {
		checkHDFS();
		Path dst = new Path(dir + fileName);
		FSDataOutputStream output = hdfs.create(dst);
		ByteBuffer buffer = ByteBuffer.allocate(2048);
		buffer.clear();
		while (fc.read(buffer) != -1) {
			buffer.flip();
			output.write(buffer.array());
		}
		fc.close();
		output.hflush();
		output.flush();
		output.close();
		System.out.println(fileName + " is uploaded.");
		return true;
	}

	public void download(String fileName, OutputStream out) throws IOException {
		checkHDFS();
		Path dst = new Path(dir + fileName);
		FSDataInputStream input = hdfs.open(dst);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while (input.read(buffer) != -1) {
			out.write(buffer.array());
		}
		input.close();
		out.flush();
		out.close();
		System.out.println(fileName + " is downloaded.");
	}

	public static void main(String[] args) throws IOException {
		FileInputStream fin = new FileInputStream("/home/wangliang/Downloads");
		HDFSUtil hfs = new HDFSUtil();
		hfs.uploadBigFile("Downloads", fin.getChannel());
		fin.close();
	}
	
	@Test
	public void testUploadSmallFile() throws IOException {
		FileInputStream fin = new FileInputStream("/home/wangliang/source/staticsf.jar");
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while (fin.read(buffer) != -1) {
			out.write(buffer);
		}
		fin.close();
		uploadSmallFile("staticsf.jar", out.toByteArray());
		out.flush();
		out.close();
	}

	public void testDownload() throws IOException {
		long begin = System.currentTimeMillis();
		FileOutputStream out = new FileOutputStream("E:\\eamhadoop\\FX2.rar");
		download("FX.rar", out);
		out.flush();
		out.close();
        long cost = System.currentTimeMillis()-begin;
		System.out.println("耗时："+cost+"ms");
	}

	public UploadBatcher getUploadBatcher() {
		return new UploadBatcher();
	}

	public DownloadBatcher getDownloadBatcher() {
		return new DownloadBatcher();
	}

	private void checkHDFS() {
		if (hdfs == null) {
			try {
				hdfs = FileSystem.get(conf);
			} catch (IOException e) {
				System.out.println("连接HDFS失败!");
				e.printStackTrace();
				hdfs = null;
			}
		}
	}

	class UploadBatcher {

	}

	class DownloadBatcher {

	}
}
