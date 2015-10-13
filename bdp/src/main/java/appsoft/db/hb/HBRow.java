package appsoft.db.hb;

import java.util.UUID;

import org.apache.hadoop.hbase.client.Put;

public class HBRow {
	private HBSet hbset=null;
	private String rowkey=null;
	private Put put=null;
	public HBRow(HBSet hbset){
		this(hbset,"ATUO-"+(Long.MAX_VALUE-System.currentTimeMillis()+"-"+UUID.randomUUID()));
	}
	public HBRow(HBSet hbset,String rowkey){
		this.hbset=hbset;
		this.rowkey=rowkey;
	}
	public void setValue(String column,String value){
		if(put==null){
			put=HBBuilder.mkPut(this.rowkey, hbset.getFamily(), column,value);
			hbset.addPut(put);
		}else{
			HBBuilder.addDataForPut(put,hbset.getFamily(), column,value);
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
}
