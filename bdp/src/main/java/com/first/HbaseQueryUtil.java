package com.first;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

import appsoft.db.hb.HBBuilder;


public class HbaseQueryUtil  {
	// ������̬���� HBaseConfiguration
	private static Configuration cfg = HBaseConfiguration.create();
	private static String tablename=null;
	private HTable table = null;
	private static String family=null;
	private static String column=null;
	public HbaseQueryUtil() throws IOException{
		System.setProperty("HADOOP_USER_NAME","hdfs");
		System.setProperty("hadoop.home.dir",getClassesPath());
		HbaseQueryUtil.tablename="monitors";
		HbaseQueryUtil.family = "t";
		HbaseQueryUtil.column="vaule";
		this.table=new HTable(cfg, tablename);
	}

	public String getClassesPath(){
		String p = this.getClass().getResource("/").getPath();
		return p;
	}
	public static void main(String[] args) throws Exception {
		String startRowKey="row_101000002";
		String endRowKey="row_101000008";
		HbaseQueryUtil hq = new HbaseQueryUtil();
		hq.scan(startRowKey, endRowKey);
		hq.selectByRowKeyColumn(tablename, startRowKey, family, null);
		List<String> rowKeys = new ArrayList<String>(){
			{
				add("row_101000002");
				add("row_101000004");
				add("row_101000006");
				add("row_101000008");
			}
		};
		hq.scan(rowKeys );
	}
	// ��ʾ��������
	public  void scan(String startRowKey, String endRowKey) throws Exception {
		Scan s = new Scan();
		//���ݲ������ʱ���ѯ
		s.setStartRow(Bytes.toBytes(startRowKey));
		s.setStopRow(Bytes.toBytes(endRowKey));
		ResultScanner rs = table.getScanner(s);
		for (Result r : rs) {
			NavigableMap<byte[], byte[]> kvs = r.getFamilyMap(Bytes.toBytes(family));
			for(Entry<byte[], byte[]> kv:kvs.entrySet()){
				System.out.println( Bytes.toString(r.getRow())+":"+ Bytes.toString(kv.getKey())+":"+ Bytes.toString(kv.getValue()));
			}
		}
	}
	   // ����rowkeys��ȡʵ��
		public  void scan(String startRowKey, String endRowKey,int period) throws Exception {
			Scan s = new Scan();
			//���ݲ������ʱ���ѯ
			ResultScanner rs = table.getScanner(s);
			for (Result r : rs) {
				NavigableMap<byte[], byte[]> kvs = r.getFamilyMap(Bytes.toBytes(family));
				for(Entry<byte[], byte[]> kv:kvs.entrySet()){
					System.out.println( Bytes.toString(r.getRow())+":"+ Bytes.toString(kv.getKey())+":"+ Bytes.toString(kv.getValue()));
				}
			}
		}
	// ��ʾ��������
		public  void scan(List<String> rowKeys) throws Exception {
			//���ݲ������ʱ���ѯ
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
	// ͨ���н����л�ȡһ������
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
