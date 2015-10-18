package appsoft.db.hb;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;

import appsoft.util.Log;
public class TestHBSet {
	public static void main(String[] args) throws IOException {
		new TestHBSet().testHBSet();
	}
	@Test
	public void testHBSet() throws IOException{
		Logger log = Log.get(TestHBSet.class);
		long begin = System.currentTimeMillis();
		//String tableName="testhbset2";
		HBSet hbset = HBSet.creatHBSet("monitors");
	    //hbset.setAutoSave(true);
		Random random=new Random();
		for(int i=1;i<=(10);i++){
			System.out.println("add row "+i);
			HBRow hrow = hbset.addRow("row_"+(Long.MAX_VALUE-i));
			hrow.setValue("name", "point_"+i);
			hrow.setValue("value", random.nextDouble());
//			if(i%(1000*200)==0){
//				//System.out.println("add row "+i);
//				log.info("add {}","row "+i);
//			}
			if(i%(1000*1000)==0){
				//hbset.save();
			}
		}
		hbset.save();
		long cost = System.currentTimeMillis()-begin;
		log.info("cost {}", +cost+"ms");
	}

}
