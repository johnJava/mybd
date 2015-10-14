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
		HBSet hbset = HBSet.getHBSet("monitordatas",1000*1000);
	    //hbset.setAutoSave(true);
		Random random=new Random();
		int max=10000*10000;
		System.out.println("begin");
		for(int i=(1000*1000+1);i<=(2000*1000);i++){
			HBRow hrow = hbset.addRow("row_"+(max+i));
			hrow.setValue("name", "point_"+i);
			hrow.setValue("vaule", random.nextInt(100)+"");
			if(i%(1000*200)==0){
				//System.out.println("add row "+i);
				log.info("add {}","row "+i);
			}
			if(i%(1000*1000)==0){
				//hbset.save();
			}
		}
		hbset.save();
		long cost = System.currentTimeMillis()-begin;
		log.info("cost {}", +cost+"ms");
	}

}
