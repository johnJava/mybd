package appsoft.db.hb;

import java.util.Map;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class HBBuilder {
	public static Put mkPut( String row,Map<String,String> columnAndValues){
		return mkPut(row,HBRunner.getDefaultFamilyName(),columnAndValues);
	}
	public static Put mkPut( String row, String columnFamily,Map<String,String> columnAndValues){
		Put put = new Put(Bytes.toBytes(row));
		for(Map.Entry<String,String> entry:columnAndValues.entrySet()){
			String column = entry.getKey();
			String value = entry.getValue();
			put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),Bytes.toBytes(value));
		}
		return put;
	}
	public static Put mkPut( String row, String columnFamily,String column,String value){
		Put put = new Put(Bytes.toBytes(row));
	    put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),Bytes.toBytes(value));
		return put;
	}
	public static Put addDataForPut( Put put, String columnFamily,String column,String value){
	    put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),Bytes.toBytes(value));
		return put;
	}
}
