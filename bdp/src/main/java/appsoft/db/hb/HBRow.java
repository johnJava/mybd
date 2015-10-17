package appsoft.db.hb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.hadoop.hbase.client.Put;

public class HBRow {
	private HBSet hbset=null;
	private String rowkey=null;
	private Put put=null;
	private boolean isAutoSave;
	private Map<String,Object> colAndVals;
	public HBRow(HBSet hbset){
		this(hbset,"ATUO-"+(Long.MAX_VALUE-System.currentTimeMillis()+"-"+UUID.randomUUID()));
	}
	public HBRow(HBSet hbset,String rowkey){
		this.hbset=hbset;
		this.rowkey=rowkey;
		this.isAutoSave=hbset.isAutoSave();
		colAndVals = new HashMap<String, Object>();
	}
	public void setValue(String column,Object value) throws IOException{
		if(this.isAutoSave){//自动保存直接保存
			hbset.getRunner().insert(hbset.getTableName(), HBBuilder.mkPut(this.rowkey, hbset.getFamily(), column,value));
		}else{//将通过hbset的save方法进行保存
			if(put==null){
				put=HBBuilder.mkPut(this.rowkey, hbset.getFamily(),column, value);
				hbset.addPut(put);
			}else{
				HBBuilder.addDataForPut(put,hbset.getFamily(), column,value);
			}
		}
		colAndVals.put(column, value);
	}
	public HBSet getHBSet(){
		return this.hbset;
	}
	public String getRowkey() {
		return rowkey;
	}
	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}
	public boolean isAutoSave() {
		return isAutoSave;
	}
	public void setAutoSave(boolean isAutoSave) {
		this.isAutoSave = isAutoSave;
	}
	
	public Object getValue(String col){
		return this.colAndVals.get(col);
	}
	public Map<String, Object> getColAndVals() {
		return colAndVals;
	}
}
