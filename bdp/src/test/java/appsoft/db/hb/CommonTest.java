package appsoft.db.hb;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class CommonTest {

	public static void main(String[] args) {
		Put p1 = new Put(Bytes.toBytes("1"));
//		Cell kv = CellUtil.;
		p1.add(Bytes.toBytes("info"), Bytes.toBytes("v1"),
				Bytes.toBytes("100"));
		p1.add(Bytes.toBytes("info"), Bytes.toBytes("v2"),
				Bytes.toBytes("200"));
		p1.add(Bytes.toBytes("info"), Bytes.toBytes("v3"),
				Bytes.toBytes("300"));
		Map<String, Object> pm = p1.toMap();
		System.out.println("pm:"+pm);
		NavigableMap<byte[], List<Cell>> fcm = p1.getFamilyCellMap();
		Collection<List<Cell>> vs = fcm.values();
		Cell cc = vs.iterator().next().get(0);
		System.out.println(cc.getValueLength());
		System.out.println(new String(cc.getValue()));
		System.out.println(cc.getClass().getName());
		System.out.println("va:"+new String(cc.getValueArray()));
		//System.out.println("vs.tostr:"+vs.iterator().next().get(0).toString());
//		Map<String, Object> fm = (Map<String, Object>) pm.get("families");
//		fm.get("info");
//		for(Entry<String, Object> entry:pm.entrySet()){
//			System.out.println();
//		}
	}

}
