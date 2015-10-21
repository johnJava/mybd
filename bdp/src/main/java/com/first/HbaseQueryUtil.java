package com.first;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
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
import org.apache.hadoop.hbase.client.coprocessor.DoubleColumnInterpreter;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.util.Bytes;

import appsoft.db.hb.HBBuilder;

public class HbaseQueryUtil {
	// 声明静态配置 HBaseConfiguration
	private static Configuration cfg = HBaseConfiguration.create();
	private static String tablename = null;
	private HTable table = null;
	private static String family = null;
	private static String column = null;

	public HbaseQueryUtil() throws IOException {
		System.setProperty("HADOOP_USER_NAME", "hdfs");
		System.setProperty("hadoop.home.dir", getClassesPath());
		cfg.set("fs.defaultFS", "hdfs://CH5:8020");
		HbaseQueryUtil.tablename = "pointdata1";
		HbaseQueryUtil.family = "t";
		HbaseQueryUtil.column = "value";
		this.table = new HTable(cfg, tablename);
	}

	public String getClassesPath() {
		String p = this.getClass().getResource("/").getPath();
		return p;
	}

	public static void main(String[] args) throws Throwable {
		String startRowKey = "SIS.JGUNIV.JG000001_9223372035409379640";
		String endRowKey = "SIS.JGUNIV.JG000001_9223372035409379650";
		HbaseQueryUtil hq = new HbaseQueryUtil();
		long begin = System.currentTimeMillis();
		//hq.scan(startRowKey, endRowKey);
		// hq.addCoprocessor();
		//hq.getAvg(startRowKey, endRowKey);
//		 hq.getMax(startRowKey, endRowKey);
//		 hq.getMin(startRowKey, endRowKey);
//		hq.getMax(startRowKey, endRowKey, 2);
//		hq.getMin(startRowKey, endRowKey, 2);
		hq.getAvg(startRowKey, endRowKey, 2);
		// hq.scan(startRowKey, endRowKey,2);
		// hq.selectByRowKeyColumn(tablename, startRowKey, family, null);
		// List<String> rowKeys = new ArrayList<String>(){
		// {
		// add("row_101000002");
		// add("row_101000004");
		// add("row_101000006");
		// add("row_101000008");
		// }
		// };
		// hq.scan(rowKeys );
		long cost = System.currentTimeMillis() - begin;
		System.out.println("cost " + cost + "ms");
	}

	public void addCoprocessor() throws IOException {
		Configuration hbaseconfig = HBaseConfiguration.create();
		HBaseAdmin hbaseAdmin = new HBaseAdmin(hbaseconfig);
		hbaseAdmin.disableTable(tablename);
		HTableDescriptor htd = hbaseAdmin.getTableDescriptor(TableName.valueOf(tablename));
		//htd.addCoprocessor(AggregateImplementation.class.getName());
		htd.addCoprocessor("appsoft.db.hb.coprocessor.MonitorAggregateImpl", new Path(  
                "hdfs:////user/eam/upload/aggregate.jar"), 1001,null);  
		hbaseAdmin.modifyTable(tablename, htd);
		hbaseAdmin.enableTable(tablename);
		hbaseAdmin.close();
	}

	public double getMax(String startRowKey, String endRowKey) {
		Scan s = new Scan();
		s.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));
		s.setStartRow(Bytes.toBytes(startRowKey));
		s.setStopRow(Bytes.toBytes(endRowKey));
		DoubleColumnInterpreter columnInterpreter = new DoubleColumnInterpreter();
		AggregationClient client = new AggregationClient(cfg);
		double rs = 0;
		try {   
			 rs = client.max(table, columnInterpreter, s);
			 System.out.println("max[" + startRowKey + "," + endRowKey + "] = " + rs);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return rs;
	}

	public void getMax(String startRowKey, String endRowKey, int period) throws Throwable {
		String[] keys = startRowKey.split("_");
		String pointId = keys[0];
		String beginTimeStr = keys[1];
		long begin = Long.valueOf(beginTimeStr);
		String endTimeStr = endRowKey.split("_")[1];
		long end = Long.valueOf(endTimeStr);
		System.out.println(pointId + ":" + begin + ":" + end);
		String internal_startRowKey = startRowKey;
		String internal_endRowKey;
		int index = 0;
		// List<Long> maxes = new ArrayList<Long>();
		List<Thread> ths = new ArrayList<Thread>();
		int size = (int) ((end - begin) / period);
		double[] maxes = new double[size];
		for (long i = begin + period; i <= end && i > 0; i += period) {
			internal_endRowKey = pointId + "_" + i;
			Thread th = new Thread(new ScanRunable(internal_startRowKey, internal_endRowKey, "max", maxes, index++));
			th.start();
			ths.add(th);
			internal_startRowKey = internal_endRowKey;
		}
		for (Thread th : ths) {
			th.join();
		}
		System.out.println("maxes.length = " + maxes.length);
		System.out.print("maxes = ");
		for (int i = 0; i < maxes.length; i++) {
			System.out.print(maxes[i]);
			if (i + 1 < maxes.length) {
				System.out.print(",");
			} else {
				System.out.println();
			}
		}
	}
	public void getMin(String startRowKey, String endRowKey, int period) throws Throwable {
		String[] keys = startRowKey.split("_");
		String pointId = keys[0];
		String beginTimeStr = keys[1];
		long begin = Long.valueOf(beginTimeStr);
		String endTimeStr = endRowKey.split("_")[1];
		long end = Long.valueOf(endTimeStr);
		System.out.println(pointId + ":" + begin + ":" + end);
		String internal_startRowKey = startRowKey;
		String internal_endRowKey;
		int index = 0;
		List<Thread> ths = new ArrayList<Thread>();
		int size = (int) ((end - begin) / period);
		double[] mins = new double[size];
		for (long i = begin + period; i <= end && i > 0; i += period) {
			internal_endRowKey = pointId + "_" + i;
			Thread th = new Thread(new ScanRunable(internal_startRowKey, internal_endRowKey, "min", mins, index++));
			th.start();
			ths.add(th);
			internal_startRowKey = internal_endRowKey;
		}
		for (Thread th : ths) {
			th.join();
		}
		System.out.print("maxes = ");
		for (int i = 0; i < mins.length; i++) {
			System.out.print(mins[i]);
			if (i + 1 < mins.length) {
				System.out.print(",");
			} else {
				System.out.println();
			}
		}
	}
	
	public void getAvg(String startRowKey, String endRowKey, int period) throws Throwable {
		String[] keys = startRowKey.split("_");
		String pointId = keys[0];
		String beginTimeStr = keys[1];
		long begin = Long.valueOf(beginTimeStr);
		String endTimeStr = endRowKey.split("_")[1];
		long end = Long.valueOf(endTimeStr);
		System.out.println(pointId + ":" + begin + ":" + end);
		String internal_startRowKey = startRowKey;
		String internal_endRowKey;
		int index = 0;
		List<Thread> ths = new ArrayList<Thread>();
		int size = (int) ((end - begin) / period);
		double[] avgs = new double[size];
		for (long i = begin + period; i <= end && i > 0; i += period) {
			internal_endRowKey = pointId + "_" + i;
			Thread th = new Thread(new ScanRunable(internal_startRowKey, internal_endRowKey, "Avg", avgs, index++));
			th.start();
			ths.add(th);
			internal_startRowKey = internal_endRowKey;
		}
		for (Thread th : ths) {
			th.join();
		}
		System.out.print("maxes = ");
		for (int i = 0; i < avgs.length; i++) {
			System.out.print(avgs[i]);
			if (i + 1 < avgs.length) {
				System.out.print(",");
			} else {
				System.out.println();
			}
		}
	}

	private class ScanRunable implements Runnable {
		private String startRowKey;
		private String endRowKey;
		private String type;
		private double[] rs;
		private int index;

		public ScanRunable(String startRowKey, String endRowKey, String type, double[] rs, int index) {
			this.startRowKey = startRowKey;
			this.endRowKey = endRowKey;
			this.type = type;
			this.rs = rs;
			this.index = index;
		}

		@Override
		public void run() {
			double val = -1;
			try {
				if (this.type.equalsIgnoreCase("max")) {
					val = getMax(startRowKey, endRowKey);
					rs[index] = val;
				} else if (this.type.equalsIgnoreCase("min")) {
					val = getMin(startRowKey, endRowKey);
					rs[index] = val;
				}else if (this.type.equalsIgnoreCase("avg")) {
					val = getAvg(startRowKey, endRowKey);
					rs[index] = val;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			rs[index] = val;
		}
	}

	public long getMin(String startRowKey, String endRowKey) throws Throwable {
		Scan s = new Scan();
		s.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));
		s.setStartRow(Bytes.toBytes(startRowKey));
		s.setStopRow(Bytes.toBytes(endRowKey));
		LongColumnInterpreter columnInterpreter = new LongColumnInterpreter();
		AggregationClient client = new AggregationClient(cfg);
		long rs = client.min(table, columnInterpreter, s);
		System.out.println("min[" + startRowKey + "," + endRowKey + "] = " + rs);
		return rs;
	}

	public double getAvg(String startRowKey, String endRowKey) throws Throwable {
		Scan s = new Scan();
		s.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));
		s.setStartRow(Bytes.toBytes(startRowKey));
		s.setStopRow(Bytes.toBytes(endRowKey));
		DoubleColumnInterpreter columnInterpreter = new DoubleColumnInterpreter();
		AggregationClient client = new AggregationClient(cfg);
		double rs = client.avg(table, columnInterpreter, s);
		System.out.println("avg[" + startRowKey + "," + endRowKey + "] = " + rs);
//		Long med = client.median(table, columnInterpreter, s);
//		System.out.println("med = " + med);
//		long rc = client.rowCount(table, columnInterpreter, s);
//		System.out.println("rc = " + rc);
//		double st = client.std(table, columnInterpreter, s);
//		System.out.println("st=" + st);
//		Long sum = client.sum(table, columnInterpreter, s);
//		System.out.println("sum=" + sum);
		return rs;
	}

	// 显示所有数据
	public void scan(String startRowKey, String endRowKey) throws Exception {
		Scan s = new Scan();
		// 根据测点名和时间查询
		//s.setRowOffsetPerColumnFamily(1);
		// s.setReversed(false);
		s.setStartRow(Bytes.toBytes(startRowKey));
		s.setStopRow(Bytes.toBytes(endRowKey));
		ResultScanner rs = table.getScanner(s);
		double sum = 0;
		double count = 0;
		for (Result r : rs) {
			count++;
			//r.getColumn(Bytes.toBytes(family), Bytes.toBytes(column));
			List<Cell> cells = r.getColumnCells(Bytes.toBytes(family), Bytes.toBytes(column));
			for (int i = 0; i < cells.size(); i++) {
				Cell cell = cells.get(i);
				double val = Bytes.toDouble(cell.getValueArray());
				sum+=val;
			}
//			NavigableMap<byte[], byte[]> kvs = r.getFamilyMap(Bytes.toBytes(family));
//			for (Entry<byte[], byte[]> kv : kvs.entrySet()) {
//				byte[] val = kv.getValue();
//				System.out.println(Bytes.toString(r.getRow()) + ":" + Bytes.toString(kv.getKey()) + ":" + Bytes.toDouble(val));
//				sum += Bytes.toDouble(val);
//			}
		}
		System.out.println("local avg :" + (sum / count));
	}

	// 利用rowkeys获取实现
	public void scan(String startRowKey, String endRowKey, int period) throws Exception {
		String[] keys = startRowKey.split("_");
		String pointId = keys[0];
		String beginTimeStr = keys[1];
		long begin = Long.valueOf(beginTimeStr);
		String endTimeStr = endRowKey.split("_")[1];
		long end = Long.valueOf(endTimeStr);
		System.out.println(pointId + ":" + begin + ":" + end);
		List<Get> gets = new ArrayList<Get>();
		for (long i = 0; (begin + i) < end; i += period) {
			String rk = pointId + "_" + (begin + i);
			System.out.println("rk=" + rk);
			gets.add(HBBuilder.mkGet(rk, family));
		}
		Result[] rs = table.get(gets);
		for (Result r : rs) {
			NavigableMap<byte[], byte[]> kvs = r.getFamilyMap(Bytes.toBytes(family));
			for (Entry<byte[], byte[]> kv : kvs.entrySet()) {
				System.out.println("PERIOD " + Bytes.toString(r.getRow()) + ":" + Bytes.toString(kv.getKey()) + ":" + Bytes.toString(kv.getValue()));
			}
		}
	}

	// 显示所有数据
	public void scan(List<String> rowKeys) throws Exception {
		// 根据测点名和时间查询
		List<Get> gets = new ArrayList<Get>();
		for (int i = 0; i < rowKeys.size(); i++) {
			gets.add(HBBuilder.mkGet(rowKeys.get(i), family));
		}
		Result[] rs = table.get(gets);
		for (Result r : rs) {
			NavigableMap<byte[], byte[]> kvs = r.getFamilyMap(Bytes.toBytes(family));
			for (Entry<byte[], byte[]> kv : kvs.entrySet()) {
				System.out.println("RKS " + Bytes.toString(r.getRow()) + ":" + Bytes.toString(kv.getKey()) + ":" + Bytes.toString(kv.getValue()));
			}
		}
	}

	// 通过行健和列获取一条数据
	public void selectByRowKeyColumn(String tablename, String row, String family, String qualifier) throws IOException {
		Get g = new Get(Bytes.toBytes(row));
		if (family != null && qualifier != null) {
			g.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
		} else if (family != null) {
			g.addFamily(Bytes.toBytes(family));
		}
		Result result = table.get(g);
		if (family != null && qualifier != null) {
			byte[] qualifiervalue = result.getValue(Bytes.toBytes(family), Bytes.toBytes(qualifier));
			System.out.println("family: qualifier==" + Bytes.toString(qualifiervalue));
		} else if (family != null) {
			NavigableMap<byte[], byte[]> kvs = result.getFamilyMap(Bytes.toBytes(family));
			for (Entry<byte[], byte[]> kv : kvs.entrySet()) {
				System.out.println("GET " + Bytes.toString(result.getRow()) + ":" + Bytes.toString(kv.getKey()) + ":" + Bytes.toString(kv.getValue()));
			}
		}
	}

}
