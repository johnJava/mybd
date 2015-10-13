package appsoft.db.hb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.hbase.client.Put;

public class HBSet {
	private static HBRunner runner =null;
	private static AtomicInteger CREATHBSETNUMS = new AtomicInteger(0);
	private static AtomicInteger ACTIVEHBSETNUMS = new AtomicInteger(0);
	private static AtomicInteger INSERTNUMS = new AtomicInteger(0);
	private String tableName="";
	private String family;
	private HBRow currRow=null;
	private List<Put> puts =null;
	private int DEFAULTROWSIZE=4000*1000;
	
	private HBSet(String tableName){
		this(tableName, HBRunner.getDefaultFamilyName());
	}
	private HBSet(String tableName,String family){
		if(runner==null) runner = new HBRunner();
		this.tableName=tableName;
		this.family=family;
		puts = new ArrayList<Put>(DEFAULTROWSIZE);
	}
	public static HBSet creatHBSet(String tableName) throws IOException{
		if(runner==null) runner = new HBRunner();
		runner.createTable(tableName);
		return getHBSet(tableName);
	}
	public static HBSet getHBSet(String tableName){
		if(!ensureTableExists(tableName)) return null;
		CREATHBSETNUMS.incrementAndGet();
		ACTIVEHBSETNUMS.incrementAndGet();
		return new HBSet(tableName);
	}
	public HBRow addRow(){
		return addRow(null);
	}
	public HBRow addRow(String rowkey){
		HBRow row =null;
		row=(rowkey==null || rowkey.equals(""))?row = new HBRow(this):new HBRow(this,rowkey);
		this.currRow=row;
		return row;
	}
	public void save() throws IOException{
		if(this.puts.size()==0) return;
		runner.batchInsert(tableName, puts);
		INSERTNUMS.addAndGet(puts.size());
		puts.clear();
	}
	public static boolean ensureTableExists(String tableName){
		return true;
	}
	public static int getCREATHBSETNUMS(){
		return CREATHBSETNUMS.get();
	}
	public static int getACTIVEHBSETNUMS(){
		return ACTIVEHBSETNUMS.get();
	}
	public void addPut(Put put) {
		this.puts.add(put);
	}
	public String getFamily() {
		return family;
	}
	@Override
	protected void finalize() throws Throwable {
		ACTIVEHBSETNUMS.decrementAndGet();
		super.finalize();
	}
	
}
