package cn.gyee.appsoft.jrt.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import appsoft.wpcache.data.DbTool;
import cn.gyee.appsoft.jrt.common.EdnaApiHelper;
import cn.gyee.appsoft.jrt.model.PointData;

public class TestEdnaSocketApiService {

	private SimpleDateFormat sdf = null;
	EdnaSocketApiService operater;
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
	private double jsswrfdl=0;
	private double totwh=0;

	@Before
	public void initIOperatorRealTime() {
		this.operater = new EdnaSocketApiService();
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Date date = sdf.parse("");
//		beginTime="2015-11-23 00:00:01";
//		endTime="2015-10-29 00:00:00";
//		hisTime="2015-10-21 10:55:57";
//		beginTime="2015-11-04 00:00:00";
		//endTime="2015-11-22 00:00:00";
//		hisTime="2015-10-21 10:55:57";
		beginTime = sdf.format(new Date());
		Date date = new Date();
//		Date date=null;
		try {
			date = sdf.parse(beginTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long l = date.getTime();
		long m = l / 1000;
		begin = m * 1000;
		end = (m + 24*3600*1000) * 1000;
		endTime = sdf.format(new Date(end));
		hisTime = beginTime;
		step=1*1000;
		period = 1800;
		fullPointNames = LoadPointInfo(5000);
		LoadJsstas();
		fullPointName="P0001.E0000.P0000014";//fullPointNames.get(0);
		System.out.println("beginTime=" + beginTime);
		System.out.println("endTime=" + endTime);
		random = new Random();
		costs = new Vector<String>();
	}

	
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
	enum pointtype{
		jsssgl,jsssfs,dqwpp,jsswrfdl,jsswyfdl,jsswnfdl
	}
	public double getVauleByType(String ptype){
		double value=0;
//		if("totwh".equalsIgnoreCase(ptype)||"jsyfdl".equalsIgnoreCase(ptype)||"jsrfdl".equalsIgnoreCase(ptype)){
//			return 0;
//		}
		if("jsssgl".equalsIgnoreCase(ptype)){//实时功率
			value = 1000+random.nextInt(500) + random.nextDouble();
		}else if("jsrpjfs".equalsIgnoreCase(ptype)||"wdspd".equalsIgnoreCase(ptype)||"jsssfs".equalsIgnoreCase(ptype)){//风速
			value =6+random.nextInt(19) + random.nextDouble();
		}else if("actpwr".equalsIgnoreCase(ptype)||"dqwpp".equalsIgnoreCase(ptype)){//预测功率
			value = 1000+random.nextInt(1000) + random.nextDouble();
		}else if("jsswyfdl".equalsIgnoreCase(ptype)){
			value = 30*1000+random.nextInt(1000) + random.nextDouble();
		}else if("jsswnfdl".equalsIgnoreCase(ptype)){
			value = 365*1000+random.nextInt(1000) + random.nextDouble();
		}else if("jssta".equalsIgnoreCase(ptype)){//风机状态
			value = random.nextInt(5);
		}else if("plcsta".equalsIgnoreCase(ptype)){//风机状态
			int valueIndex = random.nextInt(jsstas.size()-1);
			// System.out.println("风机状态valueIndex:"+valueIndex);
			 value=Integer.valueOf(jsstas.get(valueIndex));
			 //value=13;
			 //System.out.println("风机状态:"+value);
		}else if("errcode".equalsIgnoreCase(ptype)){//"errcode".equalsIgnoreCase(ptype)
			value = 1+random.nextInt(227);
		}else if("warcode".equalsIgnoreCase(ptype)){//"errcode".equalsIgnoreCase(ptype)
			int randomIndex = random.nextInt(3);
			switch (randomIndex) {
			case 0:
				value = 228+random.nextInt(122);
				break;
			case 1:
				value =1000+random.nextInt(2191);
				break;
			case 2:
				value =4000+random.nextInt(2127);
				break;
			default:
				value = 228+random.nextInt(122);
				break;
			}
		}else if("totwh".equalsIgnoreCase(ptype)){//总发电量
			totwh+=50;
			value =totwh+random.nextDouble();
			//value = 100+random.nextInt(100)+ random.nextDouble();
		}else if("jsswrfdl".equalsIgnoreCase(ptype)){//风场上网电量原始值 //日发电量
			jsswrfdl+=50;
			value =jsswrfdl+random.nextDouble();
		}else{
			value =random.nextInt(1500) + random.nextDouble();
		}//'wdspd','actpwr','dayenepro','limitpwr'
		//风机状态 jssta
		return value;
	}
	public static List<String> jsstas = new ArrayList<String>();
	
	public static Map<String, String> points = new HashMap<String, String>();
	
	public static void LoadJsstas() {
		String sql="select facturyCode from gyee_equipmentstatus ";
		DbTool dt = DbTool.getDbTool();
		try {
			long start = System.currentTimeMillis();
			ArrayList<HashMap<String, String>> al = dt.listAll(sql);
			System.out.println("加载数据库的中测点...");
			for (int i = 0; i < al.size(); i++) {
				HashMap<String, String> map = al.get(i);
				String statusId = map.get("facturycode");
				System.out.println("statusId="+statusId);
				jsstas.add(statusId);
			}
//			jsstas.add(String.valueOf(4));
//			jsstas.add(String.valueOf(5));
//			jsstas.add(String.valueOf(7));
//			jsstas.add(String.valueOf(100));
			long end = System.currentTimeMillis();
			System.out.println("缓存数据库中"+jsstas.size()+"个状态用时:" + (end - start) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> LoadPointInfo(int limit) {
		ArrayList<String> fs = new ArrayList<String>();
		// and isCache=1 and powerstationid='01' limit " + limit
		//String sql = "select id,longid,realtimeid from gyee_equipmentmeasuringpoint where isCalc=0 ";
		//String sql ="select * from gyee_equipmentmeasuringpoint  where pointDefinition in ('jsssgl','jsssfs','dqwpp')" ;//'totwh','jsswrfdl','jsswyfdl','jsswnfdl','wdspd','actpwr','jssta','plcsta','errcode','warcode'
		//String sql="select * from gyee_EquipmentMeasuringPoint where 1=1 and pointDefinition='dqwpp'  and isCalc=false";//'jsssgl','jsssfs','dqwpp','wdspd','actpwr','jsssfs','jsswrfdl','jsswyfdl','jsswnfdl'
		String sql="select realtimeid,pointDefinition from gyee_equipmentmeasuringpoint where 1=1 and pointDefinition='plcsta'";//'jsswrfdl','wdspd','actpwr','wdspd','jsswrfdl','actpwr','plcsta' and ISNULL(pointDefinition)=false and isCache=true
		DbTool dt = DbTool.getDbTool();
		try {
			long start = System.currentTimeMillis();
			ArrayList<HashMap<String, String>> al = dt.listAll(sql);
			System.out.println("加载数据库的中测点...");
			for (int i = 0; i < al.size(); i++) {
				HashMap<String, String> map = al.get(i);
				String realTimeId = map.get("realtimeid");
				realTimeId = realTimeId.trim();
				fs.add(realTimeId);
				points.put(realTimeId, map.get("pointdefinition"));
			}
//			fs.add("P0001.E0000.P0000036");
//			fs.add("P0001.E0000.P0000043");
			long end = System.currentTimeMillis();
			System.out.println("缓存数据库中"+fs.size()+"个测点用时:" + (end - start) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fs;
	}
	
	@Test
	public void putData(){
		long realStart = System.currentTimeMillis();
		PointData point = null;
		int count=0;
		for (long i = begin;  i > 0; i += step) {//i <= end &&
			for (int j = 0; j < this.fullPointNames.size(); j++) {
				point = new PointData();
				String pointid = fullPointNames.get(j);
				point.setPointId(pointid);
				int utcTime = (int) (i / 1000L);
				point.setUtcTime(utcTime);
				//point.setValue(random.nextInt(2000) + random.nextDouble());
				double v = getVauleByType(points.get(pointid));
				point.setValue(v);
				count++;
				System.out.println(count+":"+pointid+":"+v+":"+EdnaApiHelper.parseUTCLongToDate(utcTime));
				//printf(point);
				operater.putRealTimeData(pointid, point);// 插入实时数据
				operater.putHistoryData(pointid, point);// 插入历史数据
			}
		}
		long realEnd = System.currentTimeMillis();
		String cost = "插入实时和历史数据用时:"+(realEnd-realStart)+"毫秒";
		System.out.println(cost);
	}
	
	public void putRealTimeData() {
		long realStart = System.currentTimeMillis();
		PointData point = null;
		int count=0;
		for (long i = begin; i <= end && i > 0; i += step) {
			for (int j = 0; j < this.fullPointNames.size(); j++) {
				point = new PointData();
				point.setPointId(fullPointNames.get(j));
				int utcTime = (int) (i / 1000L);
				point.setUtcTime(utcTime);
				point.setValue(random.nextInt(100) + random.nextDouble());
				count++;
				System.out.print(count+":");
				printf(point);
				operater.putRealTimeData(fullPointNames.get(j), point);// 插入实时数据
			}
		}
		long realEnd = System.currentTimeMillis();
		String cost = "插入实时数据"+count+"个点用时:"+(realEnd-realStart)+"毫秒";
		System.out.println(cost);
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

	//@Test
	public void testGetHistoryRawData() {
		List<PointData> ps = operater.getHistoryRawData(fullPointName, beginTime, endTime);
		System.out.println("RawData(获取给定测点一段时间内的历史原始值) begin ");
		for (PointData pd : ps) {
			if(pd.getPointId()!=null&&pd.getUtcTime()!=null&&pd.getValue()!=null)
				System.out.println("RawData:"+pd.getPointId()+":"+EdnaApiHelper.parseUTCLongToDate(pd.getUtcTime())+":"+pd.getValue());
			else
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

	//@Test
	public void testGetHistorySnapDatas() {
		List<PointData> ps = operater.getHistorySnapData(fullPointName, beginTime, endTime, period);
		System.out.println("SnapDatas(获取给定测点一段时间内的历史数据，按照间隔的快照值) begin");
		for (PointData pd : ps) {
			if(pd.getPointId()!=null&&pd.getUtcTime()!=null&&pd.getValue()!=null)
				System.out.println("SnapDatas:"+pd.getPointId()+":"+EdnaApiHelper.parseUTCLongToDate(pd.getUtcTime())+":"+pd.getValue());
			else
				System.out.println("SnapDatas:"+pd.toString());
		}
		System.out.println("SnapDatas(获取给定测点一段时间内的历史数据，按照间隔的快照值) end");
	}

	
	public void testGetAvgHistoryData() {
		long b = System.currentTimeMillis();
		PointData pd = operater.getAvgHistoryData(fullPointName, beginTime, endTime);
		this.costs.add("cost["+(System.currentTimeMillis()-b)+"],avg(获取给定测点一段时间内的平均值) value = " + pd.getValue());
		System.out.println("cost["+(System.currentTimeMillis()-b)+"ms]avg(获取给定测点一段时间内的平均值) value = " + pd.getValue());
	}

	//@Test
	public void testGetAvgHistoryDatas() {
		long b = System.currentTimeMillis();
		List<PointData> ps = operater.getAvgHistoryData(fullPointName, beginTime, endTime, period);
		ArrayList<Double> values = new ArrayList<Double>();
		for (PointData pd : ps) {
			values.add(pd.getValue());
		}
		String msg = "avg(获取给定测点一段时间内的历史数据，按照间隔的平均值) values = "+values;
		System.out.println(msg);
		System.out.println("cost["+(System.currentTimeMillis()-b)+"],"+msg);
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
