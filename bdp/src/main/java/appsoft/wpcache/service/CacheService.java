package appsoft.wpcache.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.exception.MemcachedException;
import appsoft.wpcache.bean.Expire;
import appsoft.wpcache.bean.MemCache;
import appsoft.wpcache.data.DbTool;
import cn.gyee.appsoft.jrt.common.EdnaApiHelper;
import cn.gyee.appsoft.jrt.model.PointData;


/**
 * 测试缓存类
 * @author sean
 */
public class CacheService {
	
	private MemClient memClient;
	
	public void cache(String type) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Vector<String> pointVec = LoadPointInfo();
		Random random = new Random();
		try {
			System.currentTimeMillis();
			for(int i=0;i<pointVec.size();i++){
				String pointID=pointVec.get(i);
				PointData pd = new PointData();
				pd.setPointId(pointID);
				pd.setValue(random.nextDouble());
				pd.setUtcTime(EdnaApiHelper.parseDateStringToUTC(sdf.format(new Date())));
				System.out.println("pd["+i+"]"+pd.toString());
				if(pointID==null||pointID.equals(""))continue;
				if("set".equalsIgnoreCase(type)){
					MemCache<PointData> cache = new MemCache<PointData>(Expire.FOREVER,pointID, pd);
					memClient.set(cache);
				}else if("get".equalsIgnoreCase(type)){
					PointData result = memClient.get(pointID);
					if(result!=null)
					System.out.println("result="+result.toString());
				}
			}
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 缓存数据库中测点 
	 * @return Vector<String>
	 */
	 public static Vector<String> LoadPointInfo(){
	    	Vector<String> pointVec=new  Vector<String>();
	    	// and powerstationid='01' limit 20000
	    	String sql ="select id,longid,realtimeid from gyee_equipmentmeasuringpoint where isCalc=0 and isCache=1  and powerstationid<>'01'";
	    	DbTool dt=DbTool.getDbTool();
	    	try {
	    		long start = System.currentTimeMillis();
	    		ArrayList<HashMap<String,String>> al = dt.listAll(sql);
				System.out.println("al size  :"+al.size());
//				System.out.println(al);
				for (int i = 0; i < al.size(); i++) {
					HashMap<String,String> map =  al.get(i);
					String realTimeId =map.get("realtimeid");
					String longid =map.get("longid");
					realTimeId=realTimeId.trim();
					pointVec.add(realTimeId);
				}
				long end = System.currentTimeMillis();
				System.out.println("缓存数据库中所有测点用时:"+(end-start)+"毫秒");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return pointVec;
	    }
	 
	public MemClient getMemClient() {
		return memClient;
	}

	public void setMemClient(MemClient memClient) {
		this.memClient = memClient;
	}

	public void cachePointData(){
		
	}
}
