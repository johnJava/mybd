package appsoft.db.hb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Get;
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
	public static Get addColForGet( Get get, String columnFamily,String column){
		get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
		return get;
	}
	public static Get mkGet( String row, String columnFamily){
		return mkGet(row, columnFamily,new ArrayList<String>(1));
	}
	public static Get mkGet( String row, String columnFamily,String column){
		List<String> cols = new ArrayList<String>(1);
		cols.add(column);
		return mkGet(row, columnFamily, cols);
	}
	public static Get mkGet( String row,String columnFamily,List<String> columns){
		Get get = new Get(Bytes.toBytes(row));
		if(columnFamily!=null&&columns!=null&columns.size()>0){
			for (String col:columns) {
				get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(col));
			}
		}else if(columnFamily!=null){
			get.addFamily(Bytes.toBytes(columnFamily));
		}
		return get;
	}
}
