package com.first;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Shell;

@SuppressWarnings("resource")
public class HBaseUtil implements Runnable{

	// 声明静态配置 HBaseConfiguration
	private static Configuration cfg = HBaseConfiguration.create();
	public HBaseUtil() throws IOException{
		System.setProperty("HADOOP_USER_NAME","hdfs");
		System.setProperty("hadoop.home.dir",getClassesPath());
		cfg.set("fs.defaultFS", "hdfs://CH5:8020");
	}

	public String getClassesPath(){
		String p = this.getClass().getResource("/").getPath();
		//System.out.println("path="+p);
		return p;
	}
	// 创建一张表
	public  void creat(String tablename, String columnFamily)
			throws Exception {
		HBaseAdmin admin = new HBaseAdmin(cfg);
		if (admin.tableExists(TableName.valueOf(tablename))) {
			System.out.println("table Exists!");
			System.exit(0);
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tablename)); ;
			tableDesc.addFamily(new HColumnDescriptor(columnFamily));
			admin.createTable(tableDesc);
			System.out.println("create table success!");
		}
	}

	// 添加一条数据，通过Table Put为已经存在的表来添加数据
	public  void put(String tablename, String row, String columnFamily,
			String column, String data) throws Exception {
		HTable table = new HTable(cfg, tablename);
		Put p1 = new Put(Bytes.toBytes(row));
		p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),
				Bytes.toBytes(data));
		table.put(p1);
		System.out.println("put '" + row + "','" + columnFamily + ":" + column
				+ "','" + data + "'");
		table.close();
	}
	// 添加一条数据，通过Table Put为已经存在的表来添加数据
		public  void increment(String tablename, String row, String columnFamily,
				String column, String data) throws Exception {
			HTable table = new HTable(cfg, tablename);
			Increment increment =new Increment(Bytes.toBytes(row));
//			increment.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column),
//					Bytes.toBytes(data));
			table.increment(increment );
			//table.incrementColumnValue(row, family, qualifier, amount)
			System.out.println("put '" + row + "','" + columnFamily + ":" + column
					+ "','" + data + "'");
			table.close();
		}
	// 通过行健和列获取一条数据
	public  void selectByRowKeyColumn(String tablename, String row, String family,
			String qualifier) throws IOException {
		HTable table = new HTable(cfg, tablename);
		Get g = new Get(Bytes.toBytes(row));
		if(family != null &&qualifier != null) {
			g.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
		}else if (family != null ){
			g.addFamily(Bytes.toBytes(family));
		}
		Result result = table.get(g);
		System.out.println("Get: " + result);
		if (family != null && qualifier != null) {
			byte[] qualifiervalue = result.getValue(Bytes.toBytes(family),
					Bytes.toBytes(qualifier));
			System.out.println("family: qualifier==" + Bytes.toString(qualifiervalue));
		}
	}
	// 通过行健删除一条数据
	public  void delete(String tablename, String row) throws IOException {
		HTable table = new HTable(cfg, tablename);
		Delete d = new Delete(Bytes.toBytes(row));
		table.delete(d);
		System.out.println("delete '" + row + "'");
	}
	// 显示所有数据
	public  void scan(String tablename) throws Exception {
		HTable table = new HTable(cfg, tablename);
		Scan s = new Scan();
		//s.setTimeStamp(1432311692961l);//指定时间
		//s.setMaxResultSize(2l);
		//根据测点名和时间查询
		s.setStartRow(Bytes.toBytes("row_100000004"));
		s.setStopRow(Bytes.toBytes("row_101000008"));
		ResultScanner rs = table.getScanner(s);
		for (Result r : rs) {
			System.out.println("Scan: " + r);
		}
	}

	public  boolean deleteTable(String tablename) throws IOException {
		HBaseAdmin admin = new HBaseAdmin(cfg);
		if (admin.tableExists(TableName.valueOf(tablename))) {
			try {
				admin.disableTable(TableName.valueOf(tablename));
				admin.deleteTable(TableName.valueOf(tablename));
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}

		}
		return true;
	}

	
	
	public void testPut() throws IOException, Exception{
		String tablename = "table1";
		String columnFamily = "col1"; 
		String name="point"+1;
		String row = MD5Util.getMD5(name)+"-"+new StringBuffer(System.currentTimeMillis()+"").reverse()+"-"+UUID.randomUUID().toString();
		Random random=new Random();
		String data=random.nextInt(1000000)+"";
		new HBaseUtil().put(tablename, row, columnFamily, name, data);
		Thread.sleep(5000);
		data=random.nextInt(1000000)+"";
		new HBaseUtil().put(tablename, row, columnFamily, name, data);
	}
	
	public void testDelete() throws IOException, Exception{
		String tablename = "table1";
		String RowKey = "A33833FA5D372857E653BA6EDDC950CB-6087391752341-b3f027b6-6068-418b-8eef-f5c6b89b2f72";
		new HBaseUtil().delete(tablename, RowKey);
	}
	@org.junit.Test
	public void testGetByRowKey() throws IOException, Exception{
		String tablename = "measurepoint4";
		String columnFamily = "info"; 
		String name=null;
		String RowKey = "FAF05B58C0E43736448CC6DC9ED3D2DE-9223370596273467652-9223372036854775806";
		new HBaseUtil().selectByRowKeyColumn(tablename, RowKey, columnFamily, null);
	}
	
	public void testScan() throws IOException, Exception{
		String tablename = "measurepoint";
		new HBaseUtil().scan(tablename);;
	}
	
	public static  void main(String[] agrs) throws Exception {
		//new HBaseUtil().testDDL();
		
		long begint = System.currentTimeMillis();
		HBaseUtil.allbegin=begint;
		//new HBaseUtil().creat("testcreat", "info");
		//new HBaseUtil().testGetByRowKey();
		//new HBaseUtil(300001,305000).testInsert();
		//new HBaseUtil(1001,1001).testWideInsert();
		new HBaseUtil().scan("monitordatas");
		int begin =56000;
//		int step =1000;
//		while(begin<55001){
//			Thread th = new Thread(new HBaseUtil(begin,begin+step));
//			th.start();
//			//th.join();
//			begin+=step;
//		}
		long cost = System.currentTimeMillis()-begint;
		System.out.println("sumsum耗时："+cost+"ms");
		//new HBaseUtil().testInsert();
		
	}
	public HBaseUtil(int beginPointNum,int endPointNum){
		this.beginPointNum=beginPointNum;
		this.endPointNum=endPointNum;
	}
	private int beginPointNum=1;
	private int endPointNum=0;
	static String tablename = "testhbset2";
	static AtomicInteger count=new AtomicInteger(0);
	static volatile int costs=0;
	static volatile long allbegin=0;
	static volatile long allend=0;
	
	public void run() {
			testInsert();
	}
	@SuppressWarnings("deprecation")
	public  void testInsert(){
		String columnFamily = "info";
		/**/
		long cost;
		HTable table =null;
		try {
			long maxlong =Long.MAX_VALUE;
//			HBaseUtil ch=new HBaseUtil();
			Random random=new Random();
			System.out.println("begin connect ...");
			long begin = System.currentTimeMillis();
		    table = new HTable(cfg, tablename);
		    table.setWriteBufferSize(5*1024*1024);//5MB
		    table.setAutoFlush(false);
			cost = System.currentTimeMillis()-begin;
			System.out.println("connect successfully");
			System.out.println("connect耗时："+cost+"ms");
			costs+=cost;
			System.out.println("sumconnect耗时："+costs+"ms");
			int beginP=this.beginPointNum;
			int endP=(this.endPointNum!=0)?this.endPointNum:1000;
			begin = System.currentTimeMillis();
			ArrayList<Put> list = new ArrayList<Put>((endP+1-beginP)*1000);
			for(int i=beginP;i<=endP;i++){
				for(int j=1;j<=1000;j++){
					String name="point"+i;
					String userHash = MD5Util.getMD5(name)+"-"+(maxlong-System.currentTimeMillis())+"-"+(maxlong-count.incrementAndGet());
					Put p1 = new Put(Bytes.toBytes(userHash));
					p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("name"),
							Bytes.toBytes(name));
					p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("value"),
							Bytes.toBytes(random.nextInt(1000000)+""));
					//p1.setWriteToWAL(false);
					list.add(p1);
//					System.out.println("put 1百万["+count+"] '" + userHash + "','" + columnFamily + ":" + "name"
//							+ "','" + name + "'");
					//count.incrementAndGet();
//					count++;
					//System.out.println("put 1百万["+count+"] '" + userHash + "','" + columnFamily + ":" + "value"
						//	+ "','" + random.nextInt(1000000) + "'");
//					ch.put(tablename, userHash, columnFamily, "name",
//							name);
//					ch.put(tablename, userHash, columnFamily, "value",
//							random.nextInt(1000000)+"");
				}
				System.out.println("put 百万["+count+"]");
				if(list.size()>=(1000*1000)){
					table.put(list); 
					table.flushCommits();
					list.clear();
				}
			}
			if(list.size()>0){
				table.put(list); 
				table.flushCommits();
				list.clear();
			}
			table.close();
			allend=System.currentTimeMillis();
			cost = System.currentTimeMillis()-begin;
			System.out.println("beginPointNum["+beginPointNum+"]耗时："+cost+"ms");
			System.out.println("count耗时："+(allend-allbegin)+"ms");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null!=table)
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	@SuppressWarnings("deprecation")
	public  void testWideInsert(){
		String columnFamily = "info";
		String columnFamilyV = "v";
		/**/
		long cost;
		HTable table =null;
		try {
			long maxlong =Long.MAX_VALUE;
//			HBaseUtil ch=new HBaseUtil();
			Random random=new Random();
			System.out.println("begin connect ...");
			long begin = System.currentTimeMillis();
		    table = new HTable(cfg, tablename);
		    table.setWriteBufferSize(5*1024*1024);//5MB
		    table.setAutoFlush(false);
			cost = System.currentTimeMillis()-begin;
			System.out.println("connect successfully");
			System.out.println("connect耗时："+cost+"ms");
			costs+=cost;
			System.out.println("sumconnect耗时："+costs+"ms");
			int beginP=this.beginPointNum;
			int endP=(this.endPointNum!=0)?this.endPointNum:1000;
			begin = System.currentTimeMillis();
			ArrayList<Put> list = new ArrayList<Put>((endP+1-beginP)*1000);
			Map<String,Map<String,String>> rowkeys = new HashMap<String, Map<String,String>>();
			//Map<String,String> map = new HashMap<String, String>();
			//int sum=0;
			for(int j=1;j<=3600;j++){
				for(int i=beginP;i<=endP;i++){
					String name="point"+i;
					String userHash;
					int sum;
					if(rowkeys.containsKey(name)){
						userHash=rowkeys.get(name).get("rowkey");
						sum =Integer.valueOf(rowkeys.get(name).get("sum"));
						count.incrementAndGet();
					}else{
						 userHash = MD5Util.getMD5(name)+"-"+(maxlong-System.currentTimeMillis())+"-"+(maxlong-count.incrementAndGet());
						 Map<String,String>  contents= new HashMap<String, String>();
						 contents.put("rowkey", userHash);
						 contents.put("sum", String.valueOf(0));
						 sum=0;
						 rowkeys.put(name, contents);		
					}
					//System.out.println("userHash:"+userHash);
					Put p1 = new Put(Bytes.toBytes(userHash));
					p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("name"),
							Bytes.toBytes(name));
					int v = random.nextInt(100);
						p1.add(Bytes.toBytes(columnFamilyV), Bytes.toBytes("value"+j),
								Bytes.toBytes(v+""));
						//p1.setWriteToWAL(false);
					sum+=v;
					rowkeys.get(name).put("sum", String.valueOf(sum));
					if(j==3600){
						p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("status"),Bytes.toBytes("end"));
					}
					list.add(p1);
					System.out.println("put["+count+"]");
				}
			}
			
			//System.out.println("sum="+sum);
			if(list.size()>0){
				table.put(list); 
				table.flushCommits();
				list.clear();
			}
			table.close();
			allend=System.currentTimeMillis();
			cost = System.currentTimeMillis()-begin;
			System.out.println("beginPointNum["+beginPointNum+"]耗时："+cost+"ms");
			System.out.println("count耗时："+(allend-allbegin)+"ms");
			for( Entry<String, Map<String, String>> entry:rowkeys.entrySet()){
				System.out.println(entry.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null!=table)
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	public void testDDL(){
		
		String columnFamily = "info";

		try {
//			long begin = System.currentTimeMillis();
//			HBaseUtil ch=new HBaseUtil();
			creat(tablename, columnFamily);
//			long Length = Long.SIZE/8;
//			Random random=new Random();
//			
//			HTable table = new HTable(cfg, tablename);
//			for(int i=1;i<11;i++){
//				for(int j=1;j<11;j++){
//					String name="point"+i;
//					String userHash = MD5Util.getMD5(name)+"-"+System.currentTimeMillis();
//					
////					ch.put(tablename, userHash, columnFamily, "name",
////							name);
////					ch.put(tablename, userHash, columnFamily, "value",
////							random.nextInt(1000000)+"");
//				}
//			}
			
//			 HBaseTestCase.put(tablename, "TheRealMT", columnFamily,
//			 "password", "123456");
//			 HBaseTestCase.get(tablename, "TheRealMT", columnFamily,
//			 "password");
//			HBaseTestCase.scan(tablename);
			/*
			 * if(true==HBaseTestCase.delete(tablename))
			 * System.out.println("Delete table:"+tablename+"success!");
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
