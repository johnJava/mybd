package appsoft.db.hb.handler;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import appsoft.db.hb.HBRow;
import appsoft.db.hb.HBRunner;
import appsoft.db.hb.HBSet;
import appsoft.util.CommonUtil;

public class HBRowRsHandler implements RsHandler<HBSet> {

	@Override
	public HBSet handle(String tablename,ResultScanner rs) throws IOException {
		HBSet set = null;
		if(rs!=null){
			set = HBSet.getHBSet(tablename);
			for (Result r : rs) {
				convertToHBSet(set,r);
			}
		}
		return set;
	}

	@Override
	public HBSet handle(String tablename,Result[] rs) throws IOException {
		HBSet set = null;
		if(rs!=null){
			set = HBSet.getHBSet(tablename);
			for (Result r : rs) {
				convertToHBSet(set,r);
			}
		}
		return set;
	}

	@Override
	public HBSet handle(String tablename,Result result) throws IOException {
		HBSet set = HBSet.getHBSet(tablename);
		convertToHBSet(set,result);
		return set;
	}

	public boolean convertToHBSet(HBSet hbset,Result result) throws IOException{
		boolean flag = false;
		CommonUtil.checkNull(result);
		NavigableMap<byte[], byte[]> kvs = result.getFamilyMap(Bytes.toBytes(HBRunner.DEFAULT_FAMILYNAM));
		if(kvs!=null){
			flag = true;
			HBRow row = hbset.addRow(Bytes.toString(result.getRow()));
			for(Entry<byte[], byte[]> kv:kvs.entrySet()){
				row.setValue(Bytes.toString(kv.getKey()), Bytes.toString(kv.getValue()));
			}
		}

		return flag;
	}
	
}
