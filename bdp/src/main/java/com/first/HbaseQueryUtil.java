package com.first;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.coprocessor.AggregateImplementation;
import org.apache.hadoop.hbase.util.Bytes;

import appsoft.db.hb.HBBuilder;


public class HbaseQueryUtil  {
	// 声明静态配置 HBaseConfiguration
	private static Configuration cfg = HBaseConfiguration.create();
	private static String tablename=null;
	private HTable table = null;
	private static String family=null;
	private static String column=null;
	public HbaseQueryUtil() throws IOException{
		System.setProperty("HADOOP_USER_NAME","hdfs");
		System.setProperty("hadoop.home.dir",getClassesPath());
		cfg.set("fs.defaultFS", "hdfs://CH5:8020");
		HbaseQueryUtil.tablename="monitors";
		HbaseQueryUtil.family = "t";
		HbaseQueryUtil.column="value";
		this.table=new HTable(cfg, tablename);
	}

	public String getClassesPath(){
		String p = this.getClass().getResource("/").getPath();
		return p;
	}
	public static void main(String[] args) throws Throwable {
		int max=10000*10000;
		String startRowKey="row_"+(max-10);
		String endRowKey="row_"+(max-1);
		HbaseQueryUtil hq = new HbaseQueryUtil();
		long begin = System.currentTimeMillis();
		hq.scan(startRowKey, endRowKey);
		//hq.addCoprocessor();
		hq.getMax(startRowKey, endRowKey);
		//hq.scan(startRowKey, endRowKey,2);
		//hq.selectByRowKeyColumn(tablename, startRowKey, family, null);
//		List<String> rowKeys = new ArrayList<String>(){
//			{
//				add("row_101000002");
//				add("row_101000004");
//				add("row_101000006");
//				add("row_101000008");
//			}
//		};
//		hq.scan(rowKeys );
		long cost = System.currentTimeMillis()-begin;
		System.out.println("cost "+cost+"ms");
	}
	
	public void addCoprocessor() throws IOException{
		Configuration hbaseconfig = HBaseConfiguration.create();  
		HBaseAdmin hbaseAdmin = new HBaseAdmin(hbaseconfig);  
		hbaseAdmin.disableTable(tablename);  
		HTableDescriptor htd = hbaseAdmin.getTableDescriptor(TableName.valueOf(tablename));  
		htd.addCoprocessor(AggregateImplementation.class.getName());  
		hbaseAdmin.modifyTable(tablename, htd);  
		hbaseAdmin.enableTable(tablename);  
		hbaseAdmin.close(); 
	}
	public void getMax(String startRowKey, String endRowKey) throws Throwable{
		Scan s = new Scan();
		s.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));
		s.setStartRow(Bytes.toBytes(startRowKey));
		s.setStopRow(Bytes.toBytes(endRowKey));
		LongColumnInterpreter columnInterpreter = new LongColumnInterpreter();
		AggregationClient client = new AggregationClient(cfg);
		Long rs = client.max(table, columnInterpreter, s);
		System.out.println("rs = "+rs);
	}
	
	// 显示所有数据
	public  void scan(String startRowKey, String endRowKey) throws Exception {
		Scan s = new Scan();
		//根据测点名和时间查询
		s.setRowOffsetPerColumnFamily(1);
		//s.setReversed(false);
		s.setStartRow(Bytes.toBytes(startRowKey));
		s.setStopRow(Bytes.toBytes(endRowKey));
		ResultScanner rs = table.getScanner(s);
		for (Result r : rs) {
			NavigableMap<byte[], byte[]> kvs = r.getFamilyMap(Bytes.toBytes(family));
			for(Entry<byte[], byte[]> kv:kvs.entrySet()){
				System.out.println( Bytes.toString(r.getRow())+":"+ Bytes.toString(kv.getKey())+":"+ Bytes.toInt(kv.getValue()));
			}
		}
	}
	   // 利用rowkeys获取实现
		public  void scan(String startRowKey, String endRowKey,int period) throws Exception {
			String[] keys = startRowKey.split("_");
			String pointId = keys[0];
			String beginTimeStr = keys[1];
			long begin = Long.valueOf(beginTimeStr);
			String endTimeStr = endRowKey.split("_")[1];
			long end = Long.valueOf(endTimeStr);
			System.out.println(pointId+":"+begin+":"+end);
			List<Get> gets= new ArrayList<Get>();
			for (long i = 0; (begin+i) <end; i+=period) {
				String rk = pointId+"_"+(begin+i);
				System.out.println("rk="+rk);
				gets.add(HBBuilder.mkGet(rk, family));
			}
			Result[] rs = table.get(gets);
			for (Result r : rs) {
				NavigableMap<byte[], byte[]> kvs = r.getFamilyMap(Bytes.toBytes(family));
				for(Entry<byte[], byte[]> kv:kvs.entrySet()){
					System.out.println("PERIOD "+Bytes.toString(r.getRow())+":"+ Bytes.toString(kv.getKey())+":"+ Bytes.toString(kv.getValue()));
				}
			}
		}
	// 显示所有数据
		public  void scan(List<String> rowKeys) throws Exception {
			//根据测点名和时间查询
			List<Get> gets= new ArrayList<Get>();
			for (int i = 0; i < rowKeys.size(); i++) {
				gets.add(HBBuilder.mkGet(rowKeys.get(i), family));
			}
			Result[] rs = table.get(gets);
			for (Result r : rs) {
				NavigableMap<byte[], byte[]> kvs = r.getFamilyMap(Bytes.toBytes(family));
				for(Entry<byte[], byte[]> kv:kvs.entrySet()){
					System.out.println("RKS "+Bytes.toString(r.getRow())+":"+ Bytes.toString(kv.getKey())+":"+ Bytes.toString(kv.getValue()));
				}
			}
		}
	// 通过行健和列获取一条数据
	public  void selectByRowKeyColumn( String tablename,String row, String family,
			String qualifier) throws IOException {
		Get g = new Get(Bytes.toBytes(row));
		if(family != null &&qualifier != null) {
			g.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
		}else if (family != null ){
			g.addFamily(Bytes.toBytes(family));
		}
		Result result = table.get(g);
		if (family != null && qualifier != null) {
			byte[] qualifiervalue = result.getValue(Bytes.toBytes(family),
					Bytes.toBytes(qualifier));
			System.out.println("family: qualifier==" + Bytes.toString(qualifiervalue));
		}else if(family != null){
			NavigableMap<byte[], byte[]> kvs = result.getFamilyMap(Bytes.toBytes(family));
			for(Entry<byte[], byte[]> kv:kvs.entrySet()){
				System.out.println("GET "+Bytes.toString(result.getRow())+":"+ Bytes.toString(kv.getKey())+":"+ Bytes.toString(kv.getValue()));
			}
		}
	}



}
