package appsoft.db.hb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.hbase.client.Put;

import appsoft.db.hb.service.WriterService;

public class HBSet implements WriterService{
	private HBRunner runner =null;
	private static AtomicInteger CREATHBSETNUMS = new AtomicInteger(0);
	private static AtomicInteger ACTIVEHBSETNUMS = new AtomicInteger(0);
	private static AtomicInteger INSERTNUMS = new AtomicInteger(0);
	private static int DEFAULTROWSIZE=100;
	private String tableName="";
	private String family;
	private HBRow currRow=null;
	private List<HBRow> rows=null;
	private List<Put> puts =null;
	private HBQueryer hbqueryer=null;
	private boolean isAutoSave;
	
	private HBSet(String tableName){
		this(tableName, HBRunner.DEFAULT_FAMILYNAM);
	}
	private HBSet(String tableName,String family){
		this(tableName, family, DEFAULTROWSIZE);
	}
	private HBSet(String tableName,String family,int cacheRows){
		initRunner();
		this.tableName=tableName;
		this.family=family;
		puts = new ArrayList<Put>(cacheRows);
		rows = new ArrayList<HBRow>(cacheRows);
		initRunner();
	}
	public static HBSet creatHBSet(String tableName) throws IOException{
		return creatHBSet(tableName, new ArrayList<String>());
	}
	public static HBSet creatHBSet(String tableName,String family) throws IOException{
		ArrayList<String> fs = new ArrayList<String>();
		fs.add(family);
		return creatHBSet(tableName,fs);
	}
	public static HBSet creatHBSet(String tableName,List<String> families) throws IOException{
		HBRunner r = new HBRunner();
		if(families==null||families.size()==0){
			r.createTable(tableName);
		}else{
			r.createTable(tableName,families);
		}
		return getHBSet(tableName);
	}
	public static HBSet getHBSet(String tableName){
		return new HBSet(tableName,HBRunner.DEFAULT_FAMILYNAM);
	}
	public static HBSet getHBSet(String tableName,String family){
		return getHBSet(tableName, family, DEFAULTROWSIZE);
	}
	public static HBSet getHBSet(String tableName,int cacheRows){
		return getHBSet(tableName, HBRunner.DEFAULT_FAMILYNAM, cacheRows);
	}
	public static HBSet getHBSet(String tableName,String family,int cacheRows){
		if(!ensureTableExists(tableName)) return null;
		CREATHBSETNUMS.incrementAndGet();
		ACTIVEHBSETNUMS.incrementAndGet();
		return new HBSet(tableName, family, cacheRows);
	}
	@Override
	public HBRow addRow(){
		return addRow(null);
	}
	@Override
	public HBRow addRow(String rowkey){
		HBRow row =null;
		row=(rowkey==null || rowkey.equals(""))?row = new HBRow(this):new HBRow(this,rowkey);
		this.currRow=row;
		rows.add(row);
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
	public HBRunner getRunner() {
		initRunner();
		return runner;
	}
	private void initRunner(){
		if(runner==null) runner = new HBRunner();
	}
	public String getTableName() {
		return tableName;
	}
	public boolean isAutoSave() {
		return isAutoSave;
	}
	public void setAutoSave(boolean isAutoSave) {
		this.isAutoSave = isAutoSave;
		if(this.currRow!=null)
		this.currRow.setAutoSave(isAutoSave);
	}
	public HBQueryer getHbqueryer() {
		initHbqueryer();
		return hbqueryer;
	}
	private void initHbqueryer() {
		if(this.hbqueryer==null){
			this.hbqueryer=new HBQueryer(this);
		}
	}
	public HBRow getRow(){
		return rows.get(0);
	}
	public HBRow getRow(int index){
		return rows.get(index);
	}
	public int count(){
		return rows.size();
	}
	public void close(){
		this.rows.clear();
		this.puts.clear();
		this.currRow=null;
	} 
	public List<HBRow> getRows(){
		return this.rows;
	}
}
