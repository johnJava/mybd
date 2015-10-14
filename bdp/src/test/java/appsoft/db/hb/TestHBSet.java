package appsoft.db.hb;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import appsoft.util.Log;
public class TestHBSet {
	public static void main(String[] args) throws IOException {
		new TestHBSet().testHBSet();
	}
	@Test
	public void testHBSet() throws IOException{
		long begin = System.currentTimeMillis();
		String tableName="testhbset2";
		HBSet hbset = HBSet.getHBSet(tableName, (1000*1000+10));
		//hbset.setAutoSave(true);
		Random random=new Random();
		for(int i=48000*1000+1;i<=(49000*1000);i++){
			HBRow hrow = hbset.addRow("row"+i);
			//hrow.setRowkey("row12"+i);
			hrow.setValue("name", "point1"+i);
			hrow.setValue("vaule", random.nextInt(1000000)+"");
			if(i%(1000*500)==0){
				//Log.info("add {}","row "+i);
				System.out.println("add row "+i);
			}
//			if(i%(1000*1000)==0){
//				hbset.save();
//			}
		}
		hbset.save();
		long cost = System.currentTimeMillis()-begin;
		Log.info("cost {}", +cost+"ms");
	}

}
