package cn.gyee.appsoft.jrt.service;

import java.net.URL; 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import appsoft.db.hb.rowkey.IntRowKey;
import appsoft.wpcache.data.DbTool;
import cn.gyee.appsoft.jrt.common.EdnaApiHelper;
import cn.gyee.appsoft.jrt.model.PointData;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class TestMeM {

	EdnaSocketApiService operater;
	List<String> keys;
	public TestMeM() {
		this.operater = new EdnaSocketApiService();
		loadKeys();
		//LoadPointInfo(1000*1000);
	}
	
	public static void main(String[] args) throws ParseException, InterruptedException{
//		Date d = new Date(1448506331*1000L);
//		System.out.println(EdnaApiHelper.parseUTCLongToDate(1448506331));
//		System.out.println(d);
//		checkTime();
		TestMeM t = new TestMeM();
		t.query();
		//t.iteratorMem();
		//t.iteratorMem();
//		System.out.println(Long.MAX_VALUE-Integer.MAX_VALUE);
		//t.TableTest();
//		BigDecimal b1 = new BigDecimal(2.0);
//		BigDecimal b2 = new BigDecimal(2.0);
//		System.out.println(b1.multiply(b2).doubleValue());
//		ArrayList<String> as = new ArrayList<String>();
//		as.add("1");
//		as.add("2");
//		as.add("3");
//		as.add("4");
//		String str = as.toString();
//		str=str.substring(1, str.length()-1);
//		str=str.replace(",", "\',\'");
//		System.out.println(str);
//		long utc = Long.MAX_VALUE-9223372035406874965l;
//		System.out.println(EdnaApiHelper.parseUTCLongToDate(utc));
//		getClassesPath();
	}
	public static String getClassesPath(){
		IntRowKey r = new IntRowKey(1);
		Class<? extends IntRowKey> cla = r.getClass();
		URL res = cla.getResource("/");
		String p = res.getPath();
		System.out.println("getClassesPath():"+p);
		return p;
	}
	 public void TableTest(){
//		 TreeMap<String, String> ts = new TreeMap<String, String>();		 
	        Table<String, String, String> aTable = HashBasedTable.create();  
	        for (int i = 100; i < 103; i++) {
				for (int j = 1; j < 4; j++) {
					aTable.put(String.valueOf(i), String.valueOf(j), String.valueOf(i)+String.valueOf(j));
				}
			}
	        System.out.println(aTable.row("100"));//湖南
	        System.out.println(aTable.column("1"));//某分公司
	        System.out.println(aTable.get("100", "1"));
	        
//	        for (char a = 'A'; a <= 'C'; ++a) {  
//	            for (Integer b = 1; b <= 3; ++b) {   
//	                aTable.put(Character.toString(a), b, String.format("%c%d", a, b));  
//	            }  
//	        }  
//	   
//	        System.out.println(aTable.column(2));  
//	        System.out.println(aTable.row("B"));   
//	        System.out.println(aTable.get("B", 2));  
//
//	        System.out.println(aTable.contains("D", 1));   
//	        System.out.println(aTable.containsColumn(3));   
//	        System.out.println(aTable.containsRow("C"));  
//	        System.out.println(aTable.columnMap()); 
//	        System.out.println(aTable.rowMap());   
//	        System.out.println(aTable.remove("B", 3)); 
	    }
	public void query(){
		List<PointData> rs = operater.getRealTimeData(keys);
		for(PointData pd:rs){
			System.out.println(pd.toString());	
		}
	}
	public void iteratorMem(){
		List<PointData> rs = operater.getRealTimeData(keys);
//		double sum1=0;
//		double sum2=0;
//		for(PointData p:rs){
//			System.out.println(p.toString());
//			if(p.getPointId().contains("E0000")){
//				sum1+=p.getValue();
//			}else{
//				sum2+=1;
//			}
//		}
//		System.out.println("sum1=="+sum1);
//		System.out.println("sum2=="+sum2);
		int s0=0;
		int s1=0;
		int s2=0;
		int s3=0;
		int s4=0;
		int s5=0;
		for(PointData bean:rs){
			Integer status = bean.getValue().intValue();
			switch (status) {
			case 0:
				s0++;
				break;
			case 1:
				s1++;
				break;
			case 2:
				s2++;
				break;
			case 3:
				s3++;
				break;
			case 4:
				s4++;
				break;
			case 5:
				s5++;
				break;
			}
				
		}
		System.out.println("s0："+s0);
		System.out.println("s1："+s1);
		System.out.println("s2："+s2);
		System.out.println("s3："+s3);
		System.out.println("s4："+s4);
		System.out.println("s5："+s5);
	}
	public void loadKeys(){
		keys = new ArrayList<String>(){
			private static final long serialVersionUID = 1L;
		{
			add("P0001.E0000.P0000060");//待机
			add("P0001.E0000.P0000053");//运行
			add("P0001.E0000.P0000074");//故障停机
			add("P0001.E0000.P0000081");//通讯中断
			add("P0001.E0000.P0000067");//维护停机
			add("P0001.E0000.P0000136");//其他状态
//			add("P0000.01065.10026233");
//			add("P0001.E0000.P0000039");
//			add("P0001.E0000.P0000040");
//			add("P0001.E0000.P0000041");
//			add("P0001.E0000.P0000042");
		}};
	}
	public  void LoadPointInfo(int limit) {
		keys = new ArrayList<String>();
		String sql="select realtimeid,pointDefinition from gyee_equipmentmeasuringpoint where 1=1 and pointDefinition in('jssta') and powerStationId='01'";//'jsswrfdl','wdspd','actpwr','wdspd','jsswrfdl','actpwr','plcsta' and ISNULL(pointDefinition)=false and isCache=true
		DbTool dt = DbTool.getDbTool();
		try {
			long start = System.currentTimeMillis();
			ArrayList<HashMap<String, String>> al = dt.listAll(sql);
			System.out.println("加载数据库的中测点...");
			for (int i = 0; i < al.size(); i++) {
				HashMap<String, String> map = al.get(i);
				String realTimeId = map.get("realtimeid");
				realTimeId = realTimeId.trim();
				keys.add(realTimeId);
			}
			long end = System.currentTimeMillis();
			System.out.println("缓存数据库中"+keys.size()+"个测点用时:" + (end - start) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void checkTime() throws ParseException, InterruptedException{
		while(true){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long currentTime = System.currentTimeMillis();
			String currentTimeStr = format.format(new Date(currentTime));
			int currentUtcTime = EdnaApiHelper.parseDateStringToUTC(currentTimeStr);
			int remainder = currentUtcTime%30;
			int updateV;
			if(remainder<15){
				updateV = currentUtcTime-remainder;
			}else{
				updateV = currentUtcTime+(30-remainder);
			}
			String updateStr = EdnaApiHelper.parseUTCLongToDate(updateV);
			System.out.println(currentTimeStr+":"+currentUtcTime+":"+remainder+":"+(currentUtcTime%30==0));
			if(!(currentUtcTime%30==0)){
				System.out.println(updateStr+":"+updateV+":"+(updateV%30==0));
			}
			Thread.sleep(1000l);
		}
		
	}
}
