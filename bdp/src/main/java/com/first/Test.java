package com.first;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Shell;

import cn.gyee.appsoft.jrt.common.EdnaApiHelper;

//import com.first.example.StatisObserver;

public class Test {
	public static void main(String[] args) throws IOException, ParseException {
		
		/*long current = Long.MAX_VALUE;//System.currentTimeMillis();
		Date date = new Date(current);
		Date date1 = new Date(current+1);
		String t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
		String t1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date1);
		System.out.println("current="+current);
		System.out.println("t="+t);
		System.out.println("t1="+t1);*/
		
		//testDataH();
		//getPath();
//		long current = Long.MAX_VALUE;//System.currentTimeMillis();
//		Date date = new Date(current);
//		System.out.println(date.getClass().getName());
		//String test1="9";
		//String test2="2";
		//System.out.println(test1.compareTo(test2));
		Date date = new Date();
		Long l = date.getTime();
		Integer iv = l.intValue();
		System.out.println(l+":"+iv);
		System.out.println(iv+":"+iv.longValue());
		long utcLong =l/1000L;
		int utcInt =(int) (l/1000L);
		System.out.println("utcLong="+utcLong);
		System.out.println("utcInt="+utcInt);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(new Date(l)));
		long m = l/1000;
		System.out.println(sdf.format(new Date((m-8)*1000)));
		System.out.println(sdf.format(new Date((m-10)*1000)));
		long cur1=1445513846584l;
		long cur2 = 1445566869406l;//System.currentTimeMillis();
		System.out.println("tye1: "+EdnaApiHelper.parseUTCLongToDate(cur1/1000l));
		System.out.println("tye2: "+EdnaApiHelper.parseUTCLongToDate(cur2/1000l));
		long hk = 1323308943*1000l;
		System.out.println("hk="+sdf.format(new Date(hk)));

	
	}
	public static void  testDataH() throws IOException{
		DataHandler dh = new DataHandler(HBaseConfiguration.create(),Bytes.toBytes("measurepoint5"));
		dh.start();
		dh.addRowkey("4EA4ED2FC71029BD04F8FC80BFB16145-9223370592654246277-9223372036854775806");
	}
	public static void getPath(){
		String p = Shell.class.getClass().getResource("/").getPath();
		System.out.println("p="+p);
	}
}	


	class DataHandler extends Thread {
		static HTable table = null;
		// ������̬���� HBaseConfiguration
		private Configuration cfg =null;
		private Queue<String> rowkeys = new LinkedBlockingQueue<String>();
		static AtomicInteger count = new AtomicInteger(0);
		static ThreadPoolExecutor pool = null;
		private byte[] tablename;

		DataHandler(Configuration cfg ,byte[] tablename) throws IOException {
			this.tablename = tablename;
			this.cfg=cfg;
		}

		@SuppressWarnings("deprecation")
		public void initTable() throws IOException {
			if (table == null) {
				pool = HTable.getDefaultExecutor(cfg);
				pool.setCorePoolSize(5);
				pool.setMaximumPoolSize(10);
				pool.prestartCoreThread();
				table = new HTable(cfg, this.tablename, pool);
				table.setWriteBufferSize(5 * 1024 * 1024);// 5MB
			} else if (pool.isTerminated()) {
				pool = HTable.getDefaultExecutor(cfg);
				pool.setCorePoolSize(5);
				pool.setMaximumPoolSize(10);
				pool.prestartCoreThread();
				table.close();
				table = new HTable(cfg,this.tablename, pool);
				table.setWriteBufferSize(5 * 1024 * 1024);// 5MB
			}
		}

		public synchronized void addRowkey(String rowkey) {
			count.incrementAndGet();
			System.out.println("put rowkey [" + count + "]["+rowkey+"]...");
			rowkeys.add(rowkey);
		}
		/*
		@Override
		public void run() {
			System.out.println("running");
			String rowkey;
			while (true) {
				if(!rowkeys.isEmpty()){
					 rowkey = rowkeys.poll();
					 updateRow(rowkey);
				}
			}
		}

		
		private void updateRow(String rowkey){
				Get g = new Get(Bytes.toBytes(rowkey));
				Result result;
				try {
					initTable();
					result = table.get(g);
					NavigableMap<byte[], byte[]> kvs = result.getFamilyMap(Bytes.toBytes(StatisObserver.columnFamily));
					int sum=0;
					for(Entry<byte[], byte[]> kv:kvs.entrySet()){
						String val = Bytes.toString(kv.getValue());
						sum+=Integer.valueOf(val);
					}
					Put p = new Put(Bytes.toBytes(rowkey));
					p.add(Bytes.toBytes("info"), Bytes.toBytes("sum"), Bytes.toBytes(String.valueOf(sum)));
					table.put(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}*/

		@Override
		protected void finalize() throws Throwable {
			table.close();
			super.finalize();
		}

}
