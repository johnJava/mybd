package appsoft.db.hb;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import appsoft.db.hb.HBRow;
import appsoft.db.hb.HBSet;

import com.first.MD5Util;

public class HBaseTestUtil implements Runnable{
	public static  void main(String[] agrs) throws IOException, InterruptedException {
		long begint = System.currentTimeMillis();
		new HBaseTestUtil(11011,12010).insert();
		long cost = System.currentTimeMillis()-begint;
		System.out.println("sumsum耗时："+cost+"ms");
		
	}
	public HBaseTestUtil(int beginPointNum,int endPointNum){
		this.beginPointNum=beginPointNum;
		this.endPointNum=endPointNum;
	}
	private int beginPointNum=1;
	private int endPointNum=0;
	static String tablename = "measurepoint3";
	static AtomicInteger count=new AtomicInteger(0);
	static volatile int costs=0;
	public void run() {
		try {
			insert();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void insert() throws IOException{
		long maxlong =Long.MAX_VALUE;
		Random random=new Random();
		long cost;
		long begin = System.currentTimeMillis();
		int beginP=this.beginPointNum;
		int endP=(this.endPointNum!=0)?this.endPointNum:1000;
		HBSet hb = HBSet.getHBSet("measurepoint3");
		for(int i=beginP;i<=endP;i++){
			for(int j=1;j<=1000;j++){
				String name="point"+i;
				String rowkey=MD5Util.getMD5(name)+"-"+(maxlong-System.currentTimeMillis())+"-"+(maxlong-count.incrementAndGet());
				HBRow row = hb.addRow(rowkey);
				row.setValue("name", name);
				row.setValue("value", random.nextInt(1000000)+"");
			}
			System.out.println("put 1百万["+count+"]");
		}
		hb.save();
		cost = System.currentTimeMillis()-begin;
		System.out.println("beginPointNum["+beginPointNum+"]耗时："+cost+"ms");
		System.out.println("sum耗时："+costs+"ms");
	}
	
}
