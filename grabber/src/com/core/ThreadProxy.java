package com.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.htmlparser.util.ParserException;

public class ThreadProxy {
	private  String url;
	private  String haveurl;
	private  OperThread oper1 = null;
	private  OperThread oper2 = null;
	private  OperThread oper3 = null;
	public ThreadProxy(String url,String haveurl) {
		this.url=url;
		this.haveurl=haveurl;
	}

	@SuppressWarnings("unused")
	public void update() {
		Timer t = new Timer() ;    // 建立Timer类对象
		MyTask3 mytask3 = new MyTask3() ;    // 定义任务
		MyTask mytask = new MyTask() ;    // 定义任务
		MyTask2 mytask2 = new MyTask2() ;    // 定义任务
		MyTask2 mytask21 = new MyTask2() ;    // 定义任务
		MyTask2 mytask22 = new MyTask2() ;    // 定义任务
	    t.schedule(mytask3,0,1000) ;    // 设置任务的执行，0秒后开始，每10秒重复调用一次
	    t.schedule(mytask,0,1000) ;    // 设置任务的执行，2秒后开始，每10秒重复调用一次
	    t.schedule(mytask2,5,1000) ;    // 设置任务的执行，2秒后开始，每10秒重复调用一次
	    //t.schedule(mytask21,0,1000) ;    // 设置任务的执行，2秒后开始，每10秒重复调用一次
	    //t.schedule(mytask22,0,1000) ;    // 设置任务的执行，2秒后开始，每10秒重复调用一次
	}

	class OperThread  implements Runnable {
		private int type;

		public OperThread(int type) {
			this.type = type;
		}

		public void run() {
			System.out.println("调度--------------------------------->"+type);
			try {
				if (type == 1) {
					t1RequstHandler();
				} else if(type == 2){
					t2RequstHandler();
				}else {
//					if(upc!=0&&Common.getInstance().count==0)return;
//					if(upc==0)upc++;
					System.out.println("haveurl=="+haveurl);
					String strurl="";
					strurl=haveurl.replaceAll("a-a-a-a-a", "a-a-a-10-"+Math.round(Math.random()*100000+100000));
					System.out.println("strurl=="+strurl);
					Common.getInstance().updateCount(strurl+getKeyword());
				}
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}

		private void t1RequstHandler() throws ParserException {
			Common.getInstance().list1UpdateEndIndex=0;
			String strurl="";
			strurl=url.replaceAll("a-a-a-a-a", "a-a-a-10-"+Math.round(Math.random()*100000+100000));
			Common.getInstance().doList1Update(Common.getInstance().doSnatch(strurl+getKeyword()+"&rand="+new Date().getTime()));
		}

		private void t2RequstHandler() throws ParserException {
			System.out.println("Common.getInstance().count==="+Common.getInstance().count);
			if(Common.getInstance().count==0)return;
			int page=Common.getInstance().count/40+1;
			String strurl="";
			strurl=url.replaceAll("a-a-a-a-a", "a-a-a-10-"+Math.round(Math.random()*100000+100000));
			String msg = Common.getInstance().doList2Update(Common.getInstance().doSnatch(strurl.replaceAll("0-0.shtml", "0-"+page+".shtml")+getKeyword()+"&rand="+new Date().getTime()));
			if(msg.equals("next")){
				System.out.println(msg+">>>>");
			}
		}
	}
	public String getKeyword(){
		return "?keyword="+new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
	}
	class MyTask extends TimerTask{// 任务调度类都要继承TimerTask

		@Override
		public void run() {
			if(oper1==null)oper1 = new OperThread(1);
			new  Thread(oper1).start();
		}  
	}
	class MyTask2 extends TimerTask{// 任务调度类都要继承TimerTask

		@Override
		public void run() {
			if(oper2==null)oper2 = new OperThread(2);
			new  Thread(oper2).start();
		}  
	}
	class MyTask3 extends TimerTask{// 任务调度类都要继承TimerTask

		@Override
		public void run() {
			if(oper3==null)oper3 = new OperThread(3);
			new  Thread(oper3).start();
		}  
	}
}
