package appsoft.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;

public class HDFSUtil {
	static Configuration conf = new Configuration();
	static FileSystem hdfs = null;
	static String dir = "/user/eam/upload/";
	static Logger log = Log.get(HDFSUtil.class);
	static {
		System.setProperty("HADOOP_USER_NAME","hdfs");
		System.setProperty("hadoop.home.dir", getClassesPath());
		conf.set("fs.defaultFS", "hdfs://CH5:8020");
		try {
			hdfs = FileSystem.get(conf);
		} catch (IOException e) {
			log.info("连接HDFS失败!");
			e.printStackTrace();
			hdfs = null;
		}
	}
	public static String getClassesPath() {
		String p = new HDFSUtil().getClass().getResource("/").getPath();
		return p;
	}
	public static boolean  uploadSmallFile(String fileName, byte[] bytes)
			throws IOException {
		checkHDFS();
		Path dst = new Path(dir + fileName);
		FSDataOutputStream output = hdfs.create(dst);
		output.write(bytes);
		output.hflush();
		output.flush();
		output.close();
		log.info(fileName + " is uploaded.");
		return true;
	}

	public static boolean uploadBigFile(String fileName, FileChannel fc)
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
		log.info(fileName + " is uploaded.");
		return true;
	}

	public static void download(String fileName, OutputStream out) throws IOException {
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
		log.info(fileName + " is downloaded.");
	}

	private static void checkHDFS() {
		if (hdfs == null) {
			try {
				hdfs = FileSystem.get(conf);
			} catch (IOException e) {
				log.info("连接HDFS失败!");
				e.printStackTrace();
				hdfs = null;
			}
		}
	}

}
