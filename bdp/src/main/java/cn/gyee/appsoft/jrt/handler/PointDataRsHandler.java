package cn.gyee.appsoft.jrt.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import appsoft.db.hb.HBRunner;
import appsoft.db.hb.handler.RsHandler;
import appsoft.util.CommonUtil;
import cn.gyee.appsoft.jrt.model.PointData;

public class PointDataRsHandler implements RsHandler<List<PointData>> {

	@Override
	public List<PointData> handle(String tablename, ResultScanner rs) throws IOException {
		List<PointData> ps = new ArrayList<PointData>();
		if(rs!=null){
			for (Result r : rs) {
				PointData pointdata = new PointData();
				convertToPointData(pointdata,r);
				ps.add(pointdata);
			}
		}
		return ps;
	}

	@Override
	public List<PointData> handle(String tablename, Result[] rs) throws IOException {
		List<PointData> ps = new ArrayList<PointData>();
		if(rs!=null){
			for (Result r : rs) {
				PointData pointdata = new PointData();
				convertToPointData(pointdata,r);
				ps.add(pointdata);
			}
		}
		return ps;
	}

	@Override
	public List<PointData> handle(String tablename, Result r) throws IOException {
		List<PointData> ps = new ArrayList<PointData>(1);
		PointData pointdata = new PointData();
		convertToPointData(pointdata,r);
		ps.add(pointdata);
		return ps;
	}
	public boolean convertToPointData(PointData pointdata,Result result) throws IOException{
		boolean flag = false;
		CommonUtil.checkNull(result);
		NavigableMap<byte[], byte[]> kvs = result.getFamilyMap(Bytes.toBytes(HBRunner.DEFAULT_FAMILYNAM));
		if(kvs!=null){
			flag = true;
			for(Entry<byte[], byte[]> kv:kvs.entrySet()){
				setValueForPointdata(pointdata, kv.getKey(), kv.getValue());
			}
		}
		return flag;
	}
	public void setValueForPointdata(PointData pointdata,byte[] key, byte[] value){
		String col=Bytes.toString(key);
		if("pointId".equalsIgnoreCase(col)){
			pointdata.setPointId(Bytes.toString(value));
		}else if("value".equalsIgnoreCase(col)){
			pointdata.setValue(Bytes.toDouble(value));
		}else if("utcTime".equalsIgnoreCase(col)){
			pointdata.setUtcTime(Bytes.toInt(value));
		}else if("msTime".equalsIgnoreCase(col)){
			pointdata.setMsTime(Bytes.toInt(value));
		}else if("status".equalsIgnoreCase(col)){
			pointdata.setStatus(Bytes.toShort(value));
		}
		
	}
}
