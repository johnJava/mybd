package cn.gyee.appsoft.jrt.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import appsoft.wpcache.data.DbTool;
import cn.gyee.appsoft.jrt.model.PointData;

public class TestEdnaSocketApiService {

	private SimpleDateFormat sdf = null;
	IOperatorRealTime operater;
	String beginTime;
	String endTime;
	long begin;
	long end;
	int step;
	String fullPointName;
	int period;
	List<String> fullPointNames;
	String hisTime;
	Random random;
	List<String> costs;

	@Before
	public void initIOperatorRealTime() {
		this.operater = new EdnaSocketApiService();
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Date date = sdf.parse("");
		Date date = new Date();
		long l = date.getTime();
		System.out.println("l=" + l);
		long m = l / 1000;
		begin = m * 1000;
		end = (m + 5000) * 1000;
		step=1000;
		beginTime = sdf.format(date);
		endTime = sdf.format(new Date(end));
		hisTime = beginTime;
//		beginTime="2015-10-21 10:55:57";
//		endTime="2015-10-21 10:56:07";
//		hisTime="2015-10-21 10:55:57";
		period = 2;
		fullPointNames = LoadPointInfo(3);
		fullPointName=fullPointNames.get(0);
		System.out.println("beginTime=" + beginTime);
		System.out.println("endTime=" + endTime);
		random = new Random();
		costs = new Vector<String>();
	}

	@After
	public void printf() {
		System.out.println("fullPointNames=" + fullPointNames.toString());
		System.out.println("beginTime=" + beginTime);
		System.out.println("endTime=" + endTime);
		System.out.println("hisTime=" + hisTime);
		for (int i = 0; i < costs.size(); i++) {
			 String cost = costs.get(i);
			System.out.println("msg["+i+"] "+cost);
		}
	}

	/**
	 * 缓存数据库中测点
	 * 
	 * @return List<String>
	 */
	public static List<String> LoadPointInfo(int limit) {
		ArrayList<String> fs = new ArrayList<String>();
		String sql = "select id,longid,realtimeid from gyee_equipmentmeasuringpoint where isCalc=0 and powerstationid='01' limit " + limit;
		DbTool dt = DbTool.getDbTool();
		try {
			long start = System.currentTimeMillis();
			ArrayList<HashMap<String, String>> al = dt.listAll(sql);
			for (int i = 0; i < al.size(); i++) {
				HashMap<String, String> map = al.get(i);
				String realTimeId = map.get("realtimeid");
				fs.add(realTimeId);
			}
			long end = System.currentTimeMillis();
			System.out.println("缓存数据库中01风场[" + limit + "]测点用时:" + (end - start) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fs;
	}
	@Ignore
	public void putRealTimeData() {
		PointData point = null;
		for (long i = begin; i <= end && i > 0; i += step) {
			for (int j = 0; j < this.fullPointNames.size(); j++) {
				point = new PointData();
				point.setPointId(fullPointName);
				int utcTime = (int) (i / 1000L);
				point.setUtcTime(utcTime);
				point.setValue(random.nextInt(100) + random.nextDouble());
				operater.putRealTimeData(fullPointName, point);// 插入实时数据
			}
		}
	}
	
	@Ignore
	public void putHistoryData() {
		long realStart = System.currentTimeMillis();
		PointData point = null;
		for (long i = begin; i <= end && i > 0; i += step) {
			for (int j = 0; j < this.fullPointNames.size(); j++) {
				point = new PointData();
				point.setPointId(fullPointNames.get(j));
				int utcTime = (int) (i / 1000L);
				point.setUtcTime(utcTime);
				point.setValue(random.nextInt(100) + random.nextDouble());
				printf(point);
				operater.putHistoryData(point.getPointId(), point);
			}
		}
		long realEnd = System.currentTimeMillis();
		String cost = "插入历史数据用时:"+(realEnd-realStart)+"毫秒";
		System.out.println(cost);
		costs.add(cost);
	}
	public void printf(PointData point){
		System.out.println(point.toString());
	}
	@Ignore
	public void putHistoryDatas() {
		long realStart = System.currentTimeMillis();
		PointData point = null;
		List<PointData> pds = new ArrayList<PointData>();
		for (long i = begin; i <= end && i > 0; i += step) {
			point = new PointData();
			point.setPointId(fullPointName);
			int utcTime = (int) (i / 1000L);
			point.setUtcTime(utcTime);
			point.setValue(random.nextInt(100) + random.nextDouble());
			printf(point);
			pds.add(point);
		}
		operater.putHistoryData(fullPointName, pds );
		long realEnd = System.currentTimeMillis();
		String cost = "批量插入历史数据用时:"+(realEnd-realStart)+"毫秒";
		System.out.println(cost);
		costs.add(cost);
	}


	/**
	 * 断面获取给定时刻多点的历史数据快照值
	 */
	@Ignore
	public void testGetHistoryMatrixSnapData() {
		List<PointData> ps = operater.getHistoryMatrixSnapData(fullPointNames, hisTime);
		System.out.println("MatrixSnapData(断面获取给定时刻多点的历史数据快照值) begin ");
		for (PointData pd : ps) {
			System.out.println("MatrixSnapData:"+pd.toString());
		}
		System.out.println("MatrixSnapData(断面获取给定时刻多点的历史数据快照值) end ");
	}

	@Ignore
	public void testGetHistoryRawData() {
		List<PointData> ps = operater.getHistoryRawData(fullPointName, beginTime, endTime);
		System.out.println("RawData(获取给定测点一段时间内的历史原始值) begin ");
		for (PointData pd : ps) {
			System.out.println("RawData:"+pd.toString());
		}
		System.out.println("RawData(获取给定测点一段时间内的历史原始值) end ");
	}

	@Ignore
	public void testGetHistorySnapData() {
		PointData pd = operater.getHistorySnapData(fullPointName, hisTime);
		System.out.println("SnapData(单点某时刻值) begin ");
		System.out.println("SnapData:"+pd.toString());
		System.out.println("SnapData(单点某时刻值) end ");
	}

	@Ignore
	public void testGetHistorySnapDatas() {
		List<PointData> ps = operater.getHistorySnapData(fullPointName, beginTime, endTime, period);
		System.out.println("SnapDatas(获取给定测点一段时间内的历史数据，按照间隔的快照值) begin");
		for (PointData pd : ps) {
			System.out.println("SnapDatas:"+pd.toString());
		}
		System.out.println("SnapDatas(获取给定测点一段时间内的历史数据，按照间隔的快照值) end");
	}

	@Ignore
	public void testGetAvgHistoryData() {
		long b = System.currentTimeMillis();
		PointData pd = operater.getAvgHistoryData(fullPointName, beginTime, endTime);
		this.costs.add("cost["+(System.currentTimeMillis()-b)+"],avg(获取给定测点一段时间内的平均值) value = " + pd.getValue());
		System.out.println("avg(获取给定测点一段时间内的平均值) value = " + pd.getValue());
	}

	@Ignore
	public void testGetAvgHistoryDatas() {
		long b = System.currentTimeMillis();
		List<PointData> ps = operater.getAvgHistoryData(fullPointName, beginTime, endTime, period);
		ArrayList<Double> values = new ArrayList<Double>();
		for (PointData pd : ps) {
			values.add(pd.getValue());
		}
		String msg = "avg(获取给定测点一段时间内的历史数据，按照间隔的平均值) values = "+values;
		System.out.println(msg);
		this.costs.add("cost["+(System.currentTimeMillis()-b)+"],"+msg);
	}

	@Ignore
	public void testGetMaxHistoryData() {
		long b = System.currentTimeMillis();
		PointData pd = operater.getMaxHistoryData(fullPointName, beginTime, endTime);
		String msg = "Max(获取给定测点一段时间内的最大值) value = " + pd.getValue();
		System.out.println(msg);
		this.costs.add("cost["+(System.currentTimeMillis()-b)+"],"+msg);
	}

	@Ignore
	public void testGetMaxHistoryDatas() {
		long b = System.currentTimeMillis();
		List<PointData> ps = operater.getMaxHistoryData(fullPointName, beginTime, endTime, period);
		ArrayList<Double> values = new ArrayList<Double>();
		for (PointData pd : ps) {
			values.add(pd.getValue());
		}
		String msg = "max(获取给定测点一段时间内的历史数据，按照间隔的最大值) values = "+values;
		System.out.println(msg);
		this.costs.add("cost["+(System.currentTimeMillis()-b)+"],"+msg);
	}

	@Ignore
	public void testGetMinHistoryData() {
		long b = System.currentTimeMillis();
		PointData pd = operater.getMinHistoryData(fullPointName, beginTime, endTime);
		String msg = "min(获取给定测点一段时间内的最小值) value = " + pd.getValue();
		System.out.println(msg);
		this.costs.add("cost["+(System.currentTimeMillis()-b)+"],"+msg);
	}

	@Ignore
	public void testGetMinHistoryDatas() {
		long b = System.currentTimeMillis();
		List<PointData> ps = operater.getMinHistoryData(fullPointName, beginTime, endTime, period);
		ArrayList<Double> values = new ArrayList<Double>();
		for (PointData pd : ps) {
			values.add(pd.getValue());
		}
		String msg = "min(获取给定测点一段时间内的历史数据，按照间隔的最小值) values = "+values;
		System.out.println(msg);
		this.costs.add("cost["+(System.currentTimeMillis()-b)+"],"+msg);
	}

	@Ignore
	public void testGetRealTimeData() {
		long b = System.currentTimeMillis();
		PointData pd = operater.getRealTimeData(fullPointName);
		String msg = "取实时数据fullPointName["+fullPointName+"] = " + pd.getValue();
		System.out.println(msg);
		this.costs.add("cost["+(System.currentTimeMillis()-b)+"],"+msg);
	}

	@Ignore
	public void testGetRealTimeDatas() {
		long b = System.currentTimeMillis();
		List<PointData> ps = operater.getRealTimeData(fullPointNames);
		String msg = "取实时数据fullPointNames["+fullPointNames+"] = "+ps;
		System.out.println(msg);
		this.costs.add("cost["+(System.currentTimeMillis()-b)+"],"+msg);
	}

	@Ignore
	public void testGetRealTimeServices() {
	}

}
