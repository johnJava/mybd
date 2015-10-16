package appsoft.db.hb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.PoolMap;
import org.slf4j.Logger;

import appsoft.db.hb.core.Nullable;
import appsoft.db.hb.handler.RsHandler;
import appsoft.db.hb.service.QueryExtInfo;
import appsoft.util.Log;

@SuppressWarnings("deprecation")
public class HBRunner {
	private static Configuration cfg = HBaseConfiguration.create();
	private static HTablePool pool = null;
	public final static String DEFAULT_FAMILYNAM="t";
	private final static int DEFAULT_POOL_SIZE=5;
	private final int DEFAULT_BUFFERSIZE=5*1024*1024;//5MB
	private Logger log=null;
	public HBRunner(){
		this(DEFAULT_POOL_SIZE);
	}
	public HBRunner(int poolsize){
		if(pool==null){
			pool = new HTablePool(cfg, poolsize,PoolMap.PoolType.ThreadLocal);  
		}
		System.setProperty("HADOOP_USER_NAME","hdfs");
		System.setProperty("hadoop.home.dir",getClassesPath());
		log=Log.get(HBRunner.class);
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
			Log.info("{}","table Exists!");
			flag=false;
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
			for(int i=0;i<familyNames.size();i++){
				tableDesc.addFamily(new HColumnDescriptor(familyNames.get(i)));
			}
			admin.createTable(tableDesc);
			Log.info("{}","create table success!");
			flag = true;
		}
		return flag;
	}
	public boolean insert(String tableName,Put put) throws IOException{
		HTableInterface table = pool.getTable(tableName);
		if(table.isAutoFlush()){
			table.setWriteBufferSize(DEFAULT_BUFFERSIZE);
			table.setAutoFlush(false);
		}
		table.put(put);
		table.close();
		return true;
	}
	public boolean batchInsert(String tableName,List<Put> puts) throws IOException{
		HTableInterface table = getTable(tableName);
		log.info("{}","table put...");
		table.put(puts);
		log.info("{}","begin commit...");
		table.flushCommits();
		table.close();
		log.info("{}","commit successfully");
		return true;
	}
	public <T> T query(String tableName,String startRowKey, String endRowKey,@Nullable QueryExtInfo queryextinfo,RsHandler<T> rsh) throws IOException{
		Scan s = new Scan();
		if(queryextinfo!=null){
			s.setMaxResultSize(queryextinfo.getLimit());
		}
		//根据测点名和时间查询
		s.setStartRow(Bytes.toBytes(startRowKey));
		s.setStopRow(Bytes.toBytes(endRowKey));
		ResultScanner rs = getTable(tableName).getScanner(s);
		return rsh.handle(tableName,rs);
	}
	public <T> T query(String tableName,String rowkey,RsHandler<T> rsh) throws IOException{
		Result r = getTable(tableName).get(HBBuilder.mkGet(rowkey, DEFAULT_FAMILYNAM));
		return rsh.handle(tableName,r);
	}
	public <T> T query(String tableName,List<String> rowKeys,RsHandler<T> rsh) throws IOException{
		List<Get> gets= new ArrayList<Get>();
		for (int i = 0; i < rowKeys.size(); i++) {
			gets.add(HBBuilder.mkGet(rowKeys.get(i), DEFAULT_FAMILYNAM));
		}
		Result[] rs = getTable(tableName).get(gets);
		return  rsh.handle(tableName,rs);
	}
	 
	private HTableInterface getTable(String tableName) throws IOException{
		HTableInterface table = pool.getTable(tableName);
		if(table.isAutoFlush()){
			table.setWriteBufferSize(DEFAULT_BUFFERSIZE);
			table.setAutoFlushTo(false);
		}
		return table;
	}
	public boolean delByRowkey(String tableName,String rowKey){
		return true;
	}
	public void shutDownPool() throws IOException{
		pool.close();
	}
}
