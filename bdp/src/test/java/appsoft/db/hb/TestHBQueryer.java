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
		HBRow row = this.query.getRow("row_101000002");
		System.out.println("testGetRowsByRowkey "+row.getColAndVals().toString());
	}
	@Test
	public void testGetRowsByRowkeys() throws IOException{
		List<String> rowKeys = new ArrayList<String>(){
			private static final long serialVersionUID = 1L;
			{
				add("row_101000002");
				add("row_101000004");
				add("row_101000006");
				add("row_101000008");
			}
		};
		List<HBRow> rows = this.query.getRows(rowKeys);
		for (HBRow row:rows) {
			System.out.println("testGetRowsByRowkeys "+row.getColAndVals().toString());
		}
	}
	@Test
	public void testGetRowsByInterval() throws IOException{
		String startRowKey="row_101000002";
		String endRowKey="row_101000008";
		List<HBRow> rows = this.query.getRows(startRowKey,endRowKey,null);
		for (HBRow row:rows) {
			System.out.println("testGetRowsByInterval "+row.getColAndVals().toString());
		}
	}
}
