package appsoft.db.hb;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;
public class TestHBSet {
	public static void main(String[] args) throws IOException {
		new TestHBSet().testHBSet();
	}
	@Test
	public void testHBSet() throws IOException{
		long begin = System.currentTimeMillis();
		String tableName="testhbset";
		HBSet hbset = HBSet.creatHBSet(tableName);
		Random random=new Random();
		for(int i=2000*1000;i<3000*1000;i++){
			HBRow hrow = hbset.addRow();
			hrow.setValue("name", "point1"+i);
			hrow.setValue("vaule", random.nextInt(1000000)+"");
			hrow.setRowkey("row12"+i);
			System.out.println(" add: row"+i);
		}
		hbset.save();
		long cost = System.currentTimeMillis()-begin;
		System.out.println("×ÜºÄÊ±£º"+cost+"ms");
	}

}
