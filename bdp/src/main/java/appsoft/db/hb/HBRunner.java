package appsoft.db.hb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;

@SuppressWarnings("deprecation")
public class HBRunner {
	private static Configuration cfg = HBaseConfiguration.create();
	private static HTablePool pool = null;
	private final static String DEFAULT_FAMILYNAM="info";
	private final static int DEFAULT_POOL_SIZE=1;
	private final int DEFAULT_BUFFERSIZE=500*1024*1024;//5MB
	public HBRunner(){
		this(DEFAULT_POOL_SIZE);
	}
	public HBRunner(int poolsize){
		if(pool==null)
		pool = new HTablePool(cfg, poolsize);
		System.setProperty("HADOOP_USER_NAME","hdfs");
		System.setProperty("hadoop.home.dir",getClassesPath());
	}
	public String getClassesPath(){
		String p = this.getClass().getResource("/").getPath();
		return p;
	}
	public boolean createTable(String tableName) throws IOException{
		return createTable(tableName,DEFAULT_FAMILYNAM);
	}
	public boolean createTable(String tableName,String familyName) throws IOException{
		ArrayList<String> ls = new ArrayList<String>();
		ls.add(familyName);
		return createTable(tableName,ls);
	}
	public boolean createTable(String tableName,List<String> familyNames) throws IOException{
		boolean flag = false;
		@SuppressWarnings("resource")
		HBaseAdmin admin = new HBaseAdmin(cfg);
		if (admin.tableExists(TableName.valueOf(tableName))) {
			System.out.println("table Exists!");
			flag=false;
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
			for(int i=0;i<familyNames.size();i++){
				tableDesc.addFamily(new HColumnDescriptor(familyNames.get(i)));
			}
			admin.createTable(tableDesc);
			System.out.println("create table success!");
			flag = true;
		}
		return flag;
	}
	public boolean insert(String tableName,Put put) throws IOException{
		List<Put> puts= new ArrayList<Put>();
		puts.add(put);
		return batchInsert(tableName,puts);
	}
	public boolean batchInsert(String tableName,List<Put> puts) throws IOException{
		HTableInterface table = pool.getTable(tableName);
		table.setWriteBufferSize(DEFAULT_BUFFERSIZE);
		table.setAutoFlush(false);
		System.out.println("commit1...");
		table.put(puts);
		System.out.println("commit2...");
		table.flushCommits();
		pool.putTable(table);
		System.out.println("commit successfully");
		return true;
	}
	public boolean delByRowkey(String tableName,String rowKey){
		return true;
	}
	public void setFlushInterval(int interval){
		
	}
	public void setWriteBufferSize(int bufSize){
		
	}
	public static String getDefaultFamilyName(){
		return DEFAULT_FAMILYNAM;
	}
	public void shutDownPool() throws IOException{
		pool.close();
	}
}
