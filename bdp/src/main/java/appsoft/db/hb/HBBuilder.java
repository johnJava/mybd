package appsoft.db.hb;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import appsoft.util.exceptions.BDException;

public class HBBuilder {
	/*public static Put mkPut( String row,Map<String,Object> columnAndValues){
		return mkPut(row,HBRunner.DEFAULT_FAMILYNAM,columnAndValues);
	}*/
	public static Put mkPut( String row, String columnFamily,Map<String,Object> columnAndValues){
		Put put = new Put(Bytes.toBytes(row));
		for(Map.Entry<String,Object> entry:columnAndValues.entrySet()){
			String column = entry.getKey();
			Object value = entry.getValue();
			put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),internal_toBytes(value));
		}
		return put;
	}
	public static Put mkPut( String row, String columnFamily,String column,Object value){
		Put put = new Put(Bytes.toBytes(row));
	    put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),internal_toBytes(value));
		return put;
	}
	public static Put addDataForPut( Put put, String columnFamily,String column,Object value){
	    put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),internal_toBytes(value));
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

	private static byte[] internal_toBytes(Object obj){
		byte[] bytes=null;
		if(obj instanceof String){
			bytes=Bytes.toBytes((String)obj);
		}else if(obj instanceof Integer){
			bytes=Bytes.toBytes((Integer)obj);
		}else if(obj instanceof Long){
			bytes=Bytes.toBytes((Long)obj);
		}else if(obj instanceof Double){
			bytes=Bytes.toBytes((Double)obj);
		}else if(obj instanceof BigDecimal){
			bytes=Bytes.toBytes((BigDecimal)obj);
		}else if(obj instanceof Boolean){
			bytes=Bytes.toBytes((Boolean)obj);
		}else if(obj instanceof ByteBuffer){
			bytes=Bytes.toBytes((ByteBuffer)obj);
		}else{
			throw new BDException("对象类型["+obj.getClass().getName()+"]不属于基本类型");
		}
		return bytes;
	}
}
