package appsoft.db.hb;

import java.io.IOException;
import java.util.UUID;

import org.apache.hadoop.hbase.client.Put;

public class HBRow {
	private HBSet hbset=null;
	private String rowkey=null;
	private Put put=null;
	private boolean isAutoSave;
	public HBRow(HBSet hbset){
		this(hbset,"ATUO-"+(Long.MAX_VALUE-System.currentTimeMillis()+"-"+UUID.randomUUID()));
	}
	public HBRow(HBSet hbset,String rowkey){
		this.hbset=hbset;
		this.rowkey=rowkey;
		this.isAutoSave=hbset.isAutoSave();
	}
	public void setValue(String column,String value) throws IOException{
		if(this.isAutoSave){//自动保存直接保存
			HBSet.runner.insert(hbset.getTableName(), HBBuilder.mkPut(this.rowkey, hbset.getFamily(), column,value));
		}else{//将通过hbset的save方法进行保存
			if(put==null){
				put=HBBuilder.mkPut(this.rowkey, hbset.getFamily(), column,value);
				hbset.addPut(put);
			}else{
				HBBuilder.addDataForPut(put,hbset.getFamily(), column,value);
			}
		}
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
	
}
