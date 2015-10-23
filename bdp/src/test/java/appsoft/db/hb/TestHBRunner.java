package appsoft.db.hb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.hadoop.hbase.client.Put;
import org.junit.Ignore;
import org.junit.Test;

import appsoft.db.hb.HBBuilder;
import appsoft.db.hb.HBRunner;


public class TestHBRunner {
	
	public void testCreateTable() throws IOException{
		HBRunner runner = new HBRunner();
		List<String> ls = new ArrayList<String>();
		ls.add("test1");
		ls.add("test2");
		ls.add("test3");
		ls.add("test4");
		ls.add("test5");
		boolean result = runner.createTable("test",ls);
		System.out.print("result=="+result);
	}
	@Ignore
	public void testInsert() throws IOException{
		String tableName="test";
		HBRunner runner = new HBRunner();
		boolean result = runner.createTable(tableName);
		System.out.print("result1=="+result);
		Map<String, Object> columnAndValues = new  HashMap<String,Object>();
		columnAndValues.put("name", "point1");
		columnAndValues.put("vaule", "123");
		System.out.print("mkPut=="+1);
		Put put = HBBuilder.mkPut("row001","t",columnAndValues );
		System.out.print("mkPut=="+2);
		result=runner.insert(tableName, put);
		System.out.print("result2=="+result);
	}
	
	public void testbatchInsert() throws IOException{
		String tableName="test";
		HBRunner runner = new HBRunner();
		List<Put> puts = new ArrayList<Put>();
		Random random=new Random();
		for(int i=0;i<10;i++){
			String rowkey ="row"+i;
			Map<String, Object> columnAndValues = new  HashMap<String,Object>();
			columnAndValues.put("name", "point"+i);
			columnAndValues.put("vaule", random.nextInt(1000000)+"");
			Put put = HBBuilder.mkPut(rowkey,"t",columnAndValues );
			puts.add(put);
		}
		boolean result = runner.batchInsert(tableName, puts);
		System.out.print("result2=="+result);
	}
}
