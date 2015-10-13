package com.first;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
public class CopyHDFSUtil {

	  //initialization  
    static Configuration conf = new Configuration();  
    static FileSystem hdfs;  
    static {  
    	conf.set("fs.defaultFS", "hdfs://cdh5:8020");
    	//conf.set("fs.default.name", "8020");
//        conf.addResource(new Path( "core-site.xml"));  
//        conf.addResource(new Path( "hdfs-site.xml"));  
//        conf.addResource(new Path( "mapred-site.xml"));  
//        path = "/usr/java/hbase-0.90.3/conf/";  
//        conf.addResource(new Path( "hbase-site.xml"));  
        try {  
            hdfs = FileSystem.get(conf);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    //create a direction  
    public void createDir(String dir) throws IOException {  
        Path path = new Path(dir);  
        hdfs.mkdirs(path);  
        System.out.println("new dir \t" + conf.get("fs.default.name") + dir);  
    }     
      
    //copy from local file to HDFS file  
    public void copyFile(String localSrc, String hdfsDst) throws IOException{  
        Path src = new Path(localSrc);        
        Path dst = new Path(hdfsDst);  
        hdfs.copyFromLocalFile(src, dst);  
          
        //list all the files in the current direction  
        FileStatus files[] = hdfs.listStatus(dst);  
        System.out.println("Upload to \t" + conf.get("fs.default.name") + hdfsDst);  
        for (FileStatus file : files) {  
            System.out.println(file.getPath());  
        }  
    }  
      
    //create a new file  
    public void createFile(String fileName, String fileContent) throws IOException {  
        Path dst = new Path(fileName);  
        byte[] bytes = fileContent.getBytes();  
        FSDataOutputStream output = hdfs.create(dst);  
        output.write(bytes);
        System.out.println("new file \t" + conf.get("fs.default.name") + fileName);  
    }  
    //create a new file  
    public void createFile(String fileName, byte[] bytes) throws IOException {  
    	Path dst = new Path(fileName);  
    	FSDataOutputStream output = hdfs.create(dst);  
    	output.write(bytes);
    	output.hflush();
    	output.flush();
    	output.close();
    	System.out.println("new file \t" + conf.get("fs.default.name") + fileName);  
    }  
    public void createFile(String fileName, FileChannel fc) throws IOException {  
    	Path dst = new Path(fileName);  
    	FSDataOutputStream output = hdfs.create(dst); 
    	ByteBuffer buffer = ByteBuffer.allocate( 2048 );
    	buffer.clear();
       // fc.read( buffer );
    	while(fc.read( buffer )!=-1){
    		buffer.flip();
    		output.write(buffer.array());
    	}
    	fc.close();
    	output.hflush();
    	output.flush();
    	output.close();
    	System.out.println("new file \t" + conf.get("fs.default.name") + fileName);  
    }  
      
    //list all files  
    public void listFiles(String dirName) throws IOException {  
        Path f = new Path(dirName);  
        FileStatus[] status = hdfs.listStatus(f); 
        System.out.println(dirName + " has all files:");  
        for (int i = 0; i< status.length; i++) {  
            System.out.println(status[i].getPath().toString());  
        }  
    }  
  
    //judge a file existed? and delete it!  
    public void deleteFile(String fileName) throws IOException {  
        Path f = new Path(fileName);  
        boolean isExists = hdfs.exists(f);  
        if (isExists) { //if exists, delete  
            boolean isDel = hdfs.delete(f,true);  
            System.out.println(fileName + "  delete? \t" + isDel);  
        } else {  
            System.out.println(fileName + "  exist? \t" + isExists);  
        }  
    }  
  
    public void testListFiles() throws IOException{
    	listFiles("/user/eam/upload");
    }
    @Test
    public void testDeleteFile() throws IOException{
    	deleteFile("/user/eam/upload/hadoop大数据.rar");
    }
    
    
    
    public static void main(String[] args) throws IOException {  
    	CopyHDFSUtil ofs = new CopyHDFSUtil();  
        System.out.println("\n=======create dir=======");  
        String dir = "/user/eam/upload";  
//        ofs.createDir(dir);  
        System.out.println("\n=======copy file=======");  
//        String src = "/opt/hivetemp/kv1.txt";  
//        ofs.copyFile(src, dir);  
        System.out.println("\n=======create a file=======");  
        String fileContent = "Hello, world! Just a test.";
        long begin = System.currentTimeMillis();
        FileInputStream fin = new FileInputStream( "E:\\eamhadoop\\vm-back.rar" );
        FileChannel fc = fin.getChannel();
        ofs.createFile(dir+"/vm-back.rar", fc);
        long cost = System.currentTimeMillis()-begin;
		System.out.println("耗时："+cost+"ms");
    }  

}
