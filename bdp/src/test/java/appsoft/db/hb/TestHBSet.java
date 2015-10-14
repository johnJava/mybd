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
		String tableName="testhbset2";
		HBSet hbset = HBSet.creatHBSet(tableName);
		hbset.save();
		Random random=new Random();
		for(int i=40000*1000+1;i<=45000*1000;i++){
			HBRow hrow = hbset.addRow("row"+i);
			//hrow.setRowkey("row12"+i);
			hrow.setValue("name", "point1"+i);
			hrow.setValue("vaule", random.nextInt(1000000)+"");
			System.out.println(" add: row"+i);
			if(i%(1000*1000)==0){
				hbset.save();
			}
		}
		hbset.save();
		long cost = System.currentTimeMillis()-begin;
		System.out.println("cost:"+cost+"ms");
	}

}
