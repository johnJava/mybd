package appsoft.db.hb;

import java.io.IOException;
import java.util.List;

import appsoft.db.hb.core.Nullable;
import appsoft.db.hb.handler.HBRowRsHandler;
import appsoft.db.hb.service.QueryExtInfo;
import appsoft.db.hb.service.QueryService;

public class HBQueryer implements QueryService{

	private HBSet hbset=null;
	private HBRowRsHandler handler=null;
	
	public HBQueryer(HBSet hbset) {
		this.hbset=hbset;
		handler=new HBRowRsHandler();
	}
	@Override
	public List<HBRow> getRows(String startRowKey, String endRowKey,@Nullable QueryExtInfo queryextinfo) throws IOException {
		return hbset.getRunner().query(hbset.getTableName(),startRowKey, endRowKey,queryextinfo, handler).getRows();
	}

	@Override
	public HBRow getRow(String rowkey) throws IOException{
		return hbset.getRunner().query(hbset.getTableName(),rowkey, handler).getRow();
	}

	@Override
	public List<HBRow> getRows(List<String> rowkeys) throws IOException{
		return  hbset.getRunner().query(hbset.getTableName(),rowkeys, handler).getRows();
	}

	@Override
	public long count(String startRowKey, String endRowKey) {
		return 0;
	}

	@Override
	public long[] countAndSum(String startRowKey, String endRowKey) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public double getAvg(String startRowKey, String endRowKey, String column) throws IOException {
		return hbset.getRunner().getAvg(hbset.getTableName(), startRowKey, endRowKey, column);
	}
	@Override
	public double getMax(String startRowKey, String endRowKey, String column) throws IOException {
		return  hbset.getRunner().getMax(hbset.getTableName(), startRowKey, endRowKey, column);
	}
	@Override
	public double getMin(String startRowKey, String endRowKey, String column) throws IOException {
		return  hbset.getRunner().getMin(hbset.getTableName(), startRowKey, endRowKey, column);
	}



}
