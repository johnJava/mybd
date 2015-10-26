package cn.gyee.appsoft.jrt.service;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.Logger;

import appsoft.db.hb.HBBuilder;
import appsoft.db.hb.HBRunner;
import appsoft.util.AggregateType;
import appsoft.util.HDFSUtil;
import appsoft.util.Log;
import appsoft.wpcache.bean.Expire;
import appsoft.wpcache.bean.MemCache;
import appsoft.wpcache.data.PropertiesValue;
import appsoft.wpcache.service.MemClient;
import cn.gyee.appsoft.jrt.common.EdnaApiHelper;
import cn.gyee.appsoft.jrt.handler.PointDataRsHandler;
import cn.gyee.appsoft.jrt.model.PointData;

public class EdnaSocketApiService implements IOperatorRealTime {
	private static HBRunner runner = null;
	private final static String TABLENAME = "pointdata1";
	private static PointDataRsHandler rshandler = null;
	private static MemClient memClient;
	private static Logger log = Log.get(EdnaSocketApiService.class);
	private final static String DEFAULT_VALUE_COLOMN = "value";
	public static Map<String, String> ednaPointMap = new HashMap<String, String>();
	private int DEFAULT_WORKER_SIZE = 5;
	private ExecutorService executorservice = null;
	private Queue<Put> putQueue = new ConcurrentLinkedQueue<Put>();
	private int maxCacheRowSize =  50* 10000;// 50万条
	private List<WriteHbaseWorker> workers;
	private volatile int order=1;

	private void updateOrder(){
		if(order==DEFAULT_WORKER_SIZE){
			order=1;
		}else{
			order++;
		}
	}
	
	public EdnaSocketApiService() {
		rshandler = new PointDataRsHandler();
		initExecutorservice();
	}

	private HBRunner getRunner() {
		if (runner == null)
			runner = new HBRunner();
		return runner;
	}

	private MemClient getMemClient() {
		if (memClient == null) {
			HashMap<String, String> map = PropertiesValue.getPropValueInstance().getPropMap();
			String memAddressUrl = map.get("wp.cache.host") + ":" + map.get("wp.cache.port");
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(memAddressUrl));
			MemcachedClient memcachedClient;
			try {
				memcachedClient = builder.build();
				memcachedClient.getConnector().setSessionTimeout(60000l);
				memClient = new MemClient();
				memClient.setMemcachedClient(memcachedClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (!memClient.getMemcachedClient().getConnector().isStarted()) {
			try {
				memClient.getMemcachedClient().getConnector().start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return memClient;
	}

	@Override
	public void addPointMemory(String fullPointName, int refInterval) {
		// TODO Auto-generated method stub

	}

	@Override
	public PointData getRealTimeData(String fullPointName) {
		PointData pd = null;
		try {
			pd = getMemClient().get(fullPointName);
			printf(fullPointName, pd);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return pd;
	}

	@Override
	public List<PointData> getRealTimeData(List<String> fullPointNames) {
		List<PointData> ps = new ArrayList<PointData>();
		PointData pd = null;
		for (String fullPointName : fullPointNames) {
			pd = getRealTimeData(fullPointName);
			if (pd != null)
				ps.add(pd);
		}
		return ps;
	}

	@Override
	public List<PointData> getHistoryRawData(String fullPointName, String beginTime, String endTime) {
		List<PointData> ps = null;
		try {
			ps = getRunner().query(TABLENAME, generateRowkey(fullPointName, endTime), generateRowkey(fullPointName, beginTime), null, rshandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ps;
	}

	@Override
	public List<PointData> getHistorySnapData(String fullPointName, String beginTime, String endTime, int period) {
		int begin = 0;
		int end = 0;
		try {
			begin = EdnaApiHelper.parseDateStringToUTC(beginTime);
			end = EdnaApiHelper.parseDateStringToUTC(endTime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		List<String> rowKeys = new ArrayList<String>();
		for (int i = 0; (begin + i) <= end; i += period) {
			rowKeys.add(generateRowkey(fullPointName, begin + i));
		}
		List<PointData> ps = null;
		try {
			ps = getRunner().query(TABLENAME, rowKeys, rshandler);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public PointData getHistorySnapData(String fullPointName, String hisTime) {
		PointData pd = null;
		List<PointData> ps = null;
		try {
			ps = getRunner().query(TABLENAME, generateRowkey(fullPointName, hisTime), rshandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ps != null && ps.size() == 1)
			pd = ps.get(0);
		return pd;
	}

	@Override
	public List<PointData> getHistoryMatrixSnapData(List<String> fullPointName, String hisTime) {
		List<PointData> ps = null;
		List<String> rowKeys = new ArrayList<String>();
		for (String fp : fullPointName) {
			rowKeys.add(generateRowkey(fp, hisTime));
		}
		try {
			ps = getRunner().query(TABLENAME, rowKeys, rshandler);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public List<PointData> getAvgHistoryData(String fullPointName, String beginTime, String endTime, int period) {
		List<PointData> ps = internal_aggregateCompute(AggregateType.AVG, fullPointName, beginTime, endTime, period);
		return ps;
	}

	@Override
	public PointData getAvgHistoryData(String fullPointName, String beginTime, String endTime) {
		List<PointData> ps = internal_aggregateCompute(AggregateType.AVG, fullPointName, beginTime, endTime);
		PointData pd = null;
		if (ps != null && ps.size() == 1)
			pd = ps.get(0);
		return pd;
	}

	@Override
	public List<PointData> getMaxHistoryData(String fullPointName, String beginTime, String endTime, int period) {
		List<PointData> ps = internal_aggregateCompute(AggregateType.MAX, fullPointName, beginTime, endTime, period);
		return ps;
	}

	@Override
	public PointData getMaxHistoryData(String fullPointName, String beginTime, String endTime) {
		List<PointData> ps = internal_aggregateCompute(AggregateType.MAX, fullPointName, beginTime, endTime);
		PointData pd = null;
		if (ps != null && ps.size() == 1)
			pd = ps.get(0);
		return pd;
	}

	@Override
	public List<PointData> getMinHistoryData(String fullPointName, String beginTime, String endTime, int period) {
		List<PointData> ps = internal_aggregateCompute(AggregateType.MIN, fullPointName, beginTime, endTime, period);
		return ps;
	}

	@Override
	public PointData getMinHistoryData(String fullPointName, String beginTime, String endTime) {
		List<PointData> ps = internal_aggregateCompute(AggregateType.MIN, fullPointName, beginTime, endTime);
		PointData pd = null;
		if (ps != null && ps.size() == 1)
			pd = ps.get(0);
		return pd;
	}

	@Override
	public List<String> getRealTimeServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongId(String fullPointName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer putHistoryData(String fullPointName, List<PointData> pds) {
		int result = 0;
		for (PointData point : pds) {
			try {
				result++;
				getRunner().insert(TABLENAME, toPut(fullPointName, point));
			} catch (IOException e) {
				result--;
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public Integer putHistoryData(String fullPointName, PointData point) {
		int result = 0;
		try {
			getRunner().insert(TABLENAME, toPut(fullPointName, point));
			result++;
		} catch (IOException e) {
			e.printStackTrace();
			result = 0;
		}
		return result;
	}

	public void asyncPutHistoryData(String fullPointName, PointData point) {
		addPutTask(toPut(fullPointName, point));
	}

	private Put toPut(String fullPointName, PointData point) {
		String row = generateRowkey(fullPointName, point.getUtcTime());
		Put put = HBBuilder.mkPut(row, "pointid", point.getPointId());
		HBBuilder.addDataForPut(put, "value", point.getValue());
		if (point.getUtcTime() != null)
			HBBuilder.addDataForPut(put, "utctime", point.getUtcTime());
		if (point.getMsTime() != null)
			HBBuilder.addDataForPut(put, "mstime", point.getMsTime());
		if (point.getStatus() != null)
			HBBuilder.addDataForPut(put, "status", point.getStatus());
		return put;
	}

	private List<PointData> internal_aggregateCompute(AggregateType type, String fullPointName, String beginTime, String endTime) {
		return internal_aggregateCompute(type, fullPointName, beginTime, endTime, 0);
	}

	private List<PointData> internal_aggregateCompute(AggregateType type, String fullPointName, String beginTime, String endTime, int period) {
		List<PointData> pds = new ArrayList<PointData>();
		PointData pd = new PointData();
		try {
			if (period > 0) {
				int begin = 0;
				int end = 0;
				try {
					begin = EdnaApiHelper.parseDateStringToUTC(beginTime);
					end = EdnaApiHelper.parseDateStringToUTC(endTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String internal_startRowKey;
				String internal_endRowKey;

				List<Thread> ths = new ArrayList<Thread>();
				int size = (end - begin) / period;
				double[] rs = new double[size];
				int index = size - 1;
				for (int i = begin + period; i <= end && i > 0; i += period) {
					internal_startRowKey = generateRowkey(fullPointName, i);
					internal_endRowKey = generateRowkey(fullPointName, i - period);
					Thread th = new Thread(new ScanRunable(TABLENAME, internal_startRowKey, internal_endRowKey, HBRunner.DEFAULT_FAMILYNAM, DEFAULT_VALUE_COLOMN, type, rs, index--));
					th.start();
					ths.add(th);
				}
				for (Thread th : ths) {
					try {
						th.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for (double result : rs) {
					pd = new PointData();
					pd.setPointId("pointid");// 避免程序出错，设置为固定值
					pd.setUtcTime((int) (System.currentTimeMillis() / 1000L));// 避免程序出错，设置为固定值
					pd.setValue(result);
					pds.add(pd);
				}

			} else {
				double result = getRunner().aggregateCompute(TABLENAME, type, generateRowkey(fullPointName, endTime), generateRowkey(fullPointName, beginTime), "value");
				pd = new PointData();
				pd.setPointId("pointid");// 避免程序出错，设置为固定值
				pd.setUtcTime((int) (System.currentTimeMillis() / 1000L));// 避免程序出错，设置为固定值
				pd.setValue(result);
				pds.add(pd);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pds;
	}

	/**
	 * 
	 * @param fullPointName
	 *            测点全名，站点名.服务名.点号
	 * @param hisTime
	 *            监测时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @return 生成rowkey
	 */
	private String generateRowkey(String fullPointName, String hisTime) {
		String rowkey = null;
		try {
			int utctime = EdnaApiHelper.parseDateStringToUTC(hisTime);
			rowkey = generateRowkey(fullPointName, utctime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rowkey;
	}

	/**
	 * @param fullPointName
	 *            测点全名，站点名.服务名.点号
	 * @param hisTime
	 *            监测时间 整型
	 * @return 生成rowkey
	 */
	private String generateRowkey(String fullPointName, int hisTime) {
		String rowkey = fullPointName + "_" + (Long.MAX_VALUE - hisTime);
		return rowkey;
	}

	private class ScanRunable implements Runnable {
		private String startRowKey;
		private String endRowKey;
		private double[] rs;
		private int index;
		private AggregateType type;
		private String tableName;
		private String family;
		private String column;

		public ScanRunable(String tableName, String startRowKey, String endRowKey, String family, String column, AggregateType type, double[] rs, int index) {
			this.startRowKey = startRowKey;
			this.endRowKey = endRowKey;
			this.type = type;
			this.rs = rs;
			this.index = index;
			this.tableName = tableName;
			this.family = family;
			this.column = column;
			this.tableName = tableName;
		}

		@Override
		public void run() {
			double val = -1;
			try {
				val = getRunner().aggregateCompute(tableName, type, startRowKey, endRowKey, family, column);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			rs[index] = val;
		}
	}

	// @Override
	// public Integer putRealTimeData(String fullPointName, List<PointData> pds)
	// {
	// int result=0;
	// for(PointData pd:pds){
	// try {
	// result++;
	// getMemClient().set(new MemCache<PointData>(Expire.FOREVER,fullPointName,
	// pd));
	// } catch (TimeoutException e) {
	// result--;
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// result--;
	// e.printStackTrace();
	// } catch (MemcachedException e) {
	// result--;
	// e.printStackTrace();
	// }
	// }
	//
	// return result;
	// }

	@Override
	public Integer putRealTimeData(String fullPointName, PointData point) {
		int result;
		try {
			boolean s = getMemClient().set(new MemCache<PointData>(Expire.FOREVER, fullPointName, point));
			result = (s) ? 1 : 0;
		} catch (TimeoutException e) {
			printf("putRealTimeData error{}", fullPointName, point);
			e.printStackTrace();
			result = 0;
		} catch (InterruptedException e) {
			printf("putRealTimeData error{}", fullPointName, point);
			e.printStackTrace();
			result = 0;
		} catch (MemcachedException e) {
			printf("putRealTimeData error{}", fullPointName, point);
			e.printStackTrace();
			result = 0;
		}
		return result;
	}

	private void printf(String fullPointName, PointData point) {
		// printf("{}", fullPointName, point);
	}

	private void printf(String format, String fullPointName, PointData point) {
		log.info(format, "fullPointName=" + fullPointName + ",point=" + ((point == null) ? "null" : point.toString()));
	}

	@Override
	public boolean uploadSmallFile(String fileName, byte[] bytes) throws IOException {
		return HDFSUtil.uploadSmallFile(fileName, bytes);
	}

	@Override
	public boolean uploadBigFile(String fileName, FileChannel fc) throws IOException {
		return HDFSUtil.uploadBigFile(fileName, fc);
	}

	@Override
	public void writePoint(String serviceIp, String servicePort, List<PointData> pds) {
		for (PointData pd : pds) {
			writePoint(serviceIp, servicePort, pd);
		}
	}

	@Override
	public void writePoint(String serviceIp, String servicePort, PointData pd) {
		printf("writePoint {}", serviceIp + "&" + servicePort, pd);
		this.putRealTimeData(pd.getPointId(), pd);
	}

	private void initExecutorservice() {
		this.executorservice = Executors.newFixedThreadPool(DEFAULT_WORKER_SIZE);
		this.workers = new ArrayList<WriteHbaseWorker>(DEFAULT_WORKER_SIZE);
		for (int i = 0; i < DEFAULT_WORKER_SIZE; i++) {
			WriteHbaseWorker worker = new WriteHbaseWorker(maxCacheRowSize,i+1);
			workers.add(worker);
			this.executorservice.execute(worker);
		}
	}

	private void addPutTask(Put put) {
		synchronized (this.putQueue) {
			this.putQueue.add(put);
			try {
				this.putQueue.notifyAll();
			} catch (IllegalMonitorStateException e) {
				e.printStackTrace();
			}
		}
	}

	public int getCurrentQuequeSize() {
		return this.putQueue.size();
	}

	@Override
	protected void finalize() throws Throwable {
		for (WriteHbaseWorker worker : workers) {
			worker.commit();
		}
		this.executorservice.shutdown();
		super.finalize();
	}

	class WriteHbaseWorker implements Runnable {
		private List<Put> puts = null;
		private int maxRowSize;
		private HTable workerTable;
		private int currentOrder;
		

		public WriteHbaseWorker(int maxRowSize,int currentOrder) {
			this.maxRowSize = maxRowSize;
			puts = new ArrayList<Put>(maxRowSize + 1);
			this.currentOrder=currentOrder;
			try {
				workerTable=getRunner().getMyHTable(TABLENAME);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			log.info("WriteHbaseWorker[" + Thread.currentThread().getName() + "]begin");
			Put put = null;
			while (true) {
				if(currentOrder!=order) continue;
				synchronized (putQueue) {
					if (!putQueue.isEmpty()) {
						put = putQueue.poll();
						if (put != null)
							this.puts.add(put);
					} else {
						try {
							putQueue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if (this.puts.size() == this.maxRowSize) {
					try {
						updateOrder();
						commit();
						this.puts.clear();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private void commit() throws IOException {
			
			if (puts != null && this.puts.size() > 0){
				log.info(Thread.currentThread().getName()+" commit...");
				getRunner().batchInsert(workerTable, puts);
			}
		}
	}

	@Override
	public Integer asyncPutData(String fullPointName, PointData point) {
		asyncPutHistoryData(fullPointName, point);
		// putRealTimeData(fullPointName, point);
		return 1;
	}
}
