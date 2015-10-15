package appsoft.db.hb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.hbase.client.Put;
import org.mortbay.log.Log;

import appsoft.db.hb.service.AggregateService;
import appsoft.db.hb.service.BasicService;

public class HBSet implements AggregateService,BasicService{
	public static HBRunner runner =null;
	private static AtomicInteger CREATHBSETNUMS = new AtomicInteger(0);
	private static AtomicInteger ACTIVEHBSETNUMS = new AtomicInteger(0);
	private static AtomicInteger INSERTNUMS = new AtomicInteger(0);
	private static int DEFAULTROWSIZE=100;
	private String tableName="";
	private String family;
	private HBRow currRow=null;
	private List<Put> puts =null;
	private boolean isAutoSave;
	
	private HBSet(String tableName){
		this(tableName, HBRunner.getDefaultFamilyName());
	}
	private HBSet(String tableName,String family){
		this(tableName, family, DEFAULTROWSIZE);
	}
	private HBSet(String tableName,String family,int cacheRows){
		initRunner();
		this.tableName=tableName;
		this.family=family;
		puts = new ArrayList<Put>(cacheRows);
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
		initRunner();
		if(families==null||families.size()==0){
			Log.warn("未设置列族,系统默认列族为t");
			runner.createTable(tableName);
		}else{
			runner.createTable(tableName,families);
		}
		return getHBSet(tableName);
	}
	public static HBSet getHBSet(String tableName){
		return new HBSet(tableName,HBRunner.getDefaultFamilyName());
	}
	public static HBSet getHBSet(String tableName,String family){
		return getHBSet(tableName, family, DEFAULTROWSIZE);
	}
	public static HBSet getHBSet(String tableName,int cacheRows){
		return getHBSet(tableName, HBRunner.getDefaultFamilyName(), cacheRows);
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
	public static HBRunner getRunner() {
		initRunner();
		return runner;
	}
	public static void initRunner(){
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
	@Override
	public List<HBRow> getRows(String startRowKey, String endRowKey) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<HBRow> getRows(String startRowKey, String endRowKey, int period) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HBRow getRow(String rowkey) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<HBRow> getRows(List<String> rowkeys) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long[] getAvgs(String startRowKey, String endRowKey, int period) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getAvg(String startRowKey, String endRowKey) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<HBRow> getMaxRows(String startRowKey, String endRowKey, int period) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HBRow getMaxRow(String startRowKey, String endRowKey) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<HBRow> getMinRows(String startRowKey, String endRowKey, int period) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HBRow getMinRow(String startRowKey, String endRowKey) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long[] countAndSum() {
		// TODO Auto-generated method stub
		return null;
	}
}
