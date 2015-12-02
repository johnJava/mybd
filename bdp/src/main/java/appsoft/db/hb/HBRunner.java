package appsoft.db.hb;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.DoubleColumnInterpreter;
import org.apache.hadoop.hbase.coprocessor.AggregateImplementation;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;

import appsoft.db.hb.core.Nullable;
import appsoft.db.hb.handler.RsHandler;
import appsoft.db.hb.rowkey.IntRowKey;
import appsoft.db.hb.service.QueryExtInfo;
import appsoft.util.AggregateType;
import appsoft.util.Log;

public class HBRunner {
	private static Configuration cfg = HBaseConfiguration.create();
//	private static HTablePool pool = null;
	public final static String DEFAULT_FAMILYNAM="t";
	private Map<String,HTable> tables =null; 
	private final static int DEFAULT_POOL_SIZE=5;
	private final int DEFAULT_BUFFERSIZE=5*1024*1024;//5MB
	private Logger log=null;
	private HConnection conn=null;
	public HBRunner(){
		this(DEFAULT_POOL_SIZE);
	}
	public HBRunner(int poolsize){
//		if(pool==null){
//			initPool(poolsize);
//		}
		System.setProperty("HADOOP_USER_NAME","hdfs");
		System.setProperty("hadoop.home.dir",getClassesPath());//"D:/workspace/work3/dragon/bdp/target/classes"
		log=Log.get(HBRunner.class);
		tables = new ConcurrentHashMap<String, HTable>();
	}
	
//	private void initPool(int poolsize){
//		pool = new HTablePool(cfg, poolsize,PoolMap.PoolType.ThreadLocal); 
//	}
	public String getClassesPath(){
		IntRowKey r = new IntRowKey(1);
		Class<? extends IntRowKey> cla = r.getClass();
		URL res = cla.getResource("/");
		String p ;
		if(null!=res)
			p = res.getPath();
		else
			p="D:/hadoopbin";
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
			tableDesc.addCoprocessor(AggregateImplementation.class.getName());  //自带聚合函数实现
			admin.createTable(tableDesc);
			Log.info("{}","create table success!");
			flag = true;
		}
		return flag;
	}
	public synchronized boolean insert(String tableName,Put put) throws IOException{
		HTableInterface table = getTable(tableName);
		table.put(put);
		table.flushCommits();
		//table.close();
		return true;
	}
	public synchronized boolean batchInsert(String tableName,List<Put> puts) throws IOException{
		HTableInterface table = getTable(tableName);
		log.info("{}","table put...");
		table.put(puts);
		log.info("{}","begin commit...");
		table.flushCommits();
		//table.close();
		log.info("{}","commit successfully");
		return true;
	}
	public boolean batchInsert(HTable table,List<Put> puts) throws IOException{
		log.info("{}","table put...");
		table.put(puts);
		log.info("{}","begin commit...");
		table.flushCommits();
		//table.close();
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
	public double getMax(String tableName,String startRowKey, String endRowKey,String column) throws IOException{
		return getMax(tableName, startRowKey, endRowKey,HBRunner.DEFAULT_FAMILYNAM ,column);
	}
	public double getMax(String tableName,String startRowKey, String endRowKey,String column, int period) throws IOException{
		return getMax(tableName, startRowKey, endRowKey,HBRunner.DEFAULT_FAMILYNAM ,column);
	}
	public double getMax(String tableName,String startRowKey, String endRowKey,String family,String column) throws IOException{
		return aggregateCompute(tableName, AggregateType.MAX, startRowKey, endRowKey, family, column);
	}
	public double getMin(String tableName,String startRowKey, String endRowKey,String column) throws IOException{
		return getMin(tableName, startRowKey, endRowKey,HBRunner.DEFAULT_FAMILYNAM ,column);
	}
	public double getMin(String tableName,String startRowKey, String endRowKey,String family,String column) throws IOException{
		return aggregateCompute(tableName, AggregateType.MIN, startRowKey, endRowKey, family, column);
	}
	public double getAvg(String tableName,String startRowKey, String endRowKey,String column) throws IOException{
		return getAvg(tableName, startRowKey, endRowKey,HBRunner.DEFAULT_FAMILYNAM ,column);
	}
	public double getAvg(String tableName,String startRowKey, String endRowKey,String family,String column) throws IOException{
		return aggregateCompute(tableName,AggregateType.AVG, startRowKey,  endRowKey, family, column);
	}
	public double aggregateCompute(String tableName, AggregateType type,String startRowKey,String endRowKey,String column) throws IOException{
		return aggregateCompute(tableName, type,startRowKey, endRowKey, HBRunner.DEFAULT_FAMILYNAM, column);
	}
	public double aggregateCompute(String tableName, AggregateType type,String startRowKey,String endRowKey,String family,String column) throws IOException{
		Scan s = new Scan();
		s.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));
		s.setStartRow(Bytes.toBytes(startRowKey));
		s.setStopRow(Bytes.toBytes(endRowKey));
		DoubleColumnInterpreter columnInterpreter = new DoubleColumnInterpreter();
		AggregationClient client = new AggregationClient(cfg);
		double rs = 0;
		try {
			if (type==AggregateType.MAX) {
				rs = client.max(TableName.valueOf(tableName), columnInterpreter, s);
			} else if (type==AggregateType.MIN) {
				rs = client.min(TableName.valueOf(tableName), columnInterpreter, s);
			}else if (type==AggregateType.AVG) {
				rs = client.avg(TableName.valueOf(tableName), columnInterpreter, s);
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		log.info("aggregate[" + startRowKey + "," + endRowKey + "] = " + rs);
		return rs;
	}
	public <T> T query(String tableName,List<String> rowKeys,RsHandler<T> rsh) throws IOException{
		List<Get> gets= new ArrayList<Get>();
		for (int i = 0; i < rowKeys.size(); i++) {
			gets.add(HBBuilder.mkGet(rowKeys.get(i), DEFAULT_FAMILYNAM));
		}
		Result[] rs = getTable(tableName).get(gets);
		return  rsh.handle(tableName,rs);
	}
	 
	private HTable getTable(String tableName) throws IOException{
		HTable table = tables.get(tableName);
		if(table==null){
			table = new HTable(TableName.valueOf(tableName), getCreatConn());
			table.setWriteBufferSize(DEFAULT_BUFFERSIZE);
			table.setAutoFlush(false,false);
			tables.put(tableName, table);
		}
		return table;
	}
	public HTable getMyHTable(String tableName) throws IOException{
		HTable table  = new HTable(TableName.valueOf(tableName), getCreatConn());
		return table;
	}
	public boolean delByRowkey(String tableName,String rowKey){
		return true;
	}
	public void close(){
		for(Entry<String, HTable> entry:this.tables.entrySet()){
			HTable t = entry.getValue();
			try {
				t.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	  
	@Override
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}
	//	public void shutDownPool() throws IOException{
//		pool.close();
//	}
	private HConnection getCreatConn(){
		if(conn==null||conn.isClosed()){
			try {
				conn = HConnectionManager.createConnection(cfg,Executors.newFixedThreadPool(DEFAULT_POOL_SIZE));//(cfg, Executors.newFixedThreadPool(DEFAULT_POOL_SIZE));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
}
