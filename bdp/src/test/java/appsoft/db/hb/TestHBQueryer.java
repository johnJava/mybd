package appsoft.db.hb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestHBQueryer {

	public static void main(String[] args) {

	}
	public TestHBQueryer() {
		this.query = HBSet.getHBSet("monitors").getHbqueryer();
	}
	private HBQueryer query = null;
	@Before
	public void initHBQueryer(){
		this.query = HBSet.getHBSet("monitors").getHbqueryer();
	}
	
	@Test
	public void testGetRowsByRowkey() throws IOException{
		HBRow row = this.query.getRow("row_"+(Long.MAX_VALUE-2));
		System.out.println("testGetRowsByRowkey "+row.getColAndVals().toString());
	}
	@Test
	public void testGetRowsByRowkeys() throws IOException{
		List<String> rowKeys = new ArrayList<String>(){
			private static final long serialVersionUID = 1L;
			{
				add("row_"+(Long.MAX_VALUE-2));
				add("row_"+(Long.MAX_VALUE-4));
				add("row_"+(Long.MAX_VALUE-6));
				add("row_"+(Long.MAX_VALUE-8));
			}
		};
		List<HBRow> rows = this.query.getRows(rowKeys);
		for (HBRow row:rows) {
			System.out.println("testGetRowsByRowkeys "+row.getColAndVals().toString());
		}
	}
	@Test
	public void testGetRowsByInterval() throws IOException{
		String startRowKey="row_"+(Long.MAX_VALUE-2);
		String endRowKey="row_"+(Long.MAX_VALUE-8);
		List<HBRow> rows = this.query.getRows(startRowKey,endRowKey,null);
		for (HBRow row:rows) {
			System.out.println("testGetRowsByInterval "+row.getColAndVals().toString());
		}
	}
	@Test
	public void testGetAvg() throws IOException{
		String startRowKey="row_"+(Long.MAX_VALUE-2);
		String endRowKey="row_"+(Long.MAX_VALUE-8);
		String column="value";
		double rs = this.query.getAvg(startRowKey, endRowKey, column);
		System.out.println("avg="+rs);
	}
	@Test
	public void testGetMin() throws IOException{
		String startRowKey="row_"+(Long.MAX_VALUE-2);
		String endRowKey="row_"+(Long.MAX_VALUE-8);
		String column="value";
		double rs = this.query.getMin(startRowKey, endRowKey, column);
		System.out.println("min="+rs);
	}
	@Test
	public void testGetMax() throws IOException{
		String startRowKey="row_"+(Long.MAX_VALUE-2);
		String endRowKey="row_"+(Long.MAX_VALUE-8);
		String column="value";
		double rs = this.query.getMax(startRowKey, endRowKey, column);
		System.out.println("max="+rs);
	}
}
