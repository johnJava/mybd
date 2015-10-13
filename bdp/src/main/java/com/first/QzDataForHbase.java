package com.first;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;


public class QzDataForHbase {
	static String tablename="pointvalues";
	public static void createTable() throws Exception
	{
		creatTable(tablename, "xmz001", "sl001");
	}
	
	public static void main(String[] args) throws Exception 
	{
		//createTable();
		insertData(); 
	}
	public static void insertData() throws Exception 
	{
		ArrayList al = readFile("d:/安瑞科气站模拟数据.txt");
		int dataMoniSize=al.size();
		/*for(int i=0;i<al.size();i++)
		{
			ArrayList alEach=(ArrayList)al.get(i);
			for(int j=0;j<alEach.size();j++)
			{
				HashMap  hm=(HashMap)alEach.get(j);
				String tablename=(String)hm.get("tablename");
				String colname=(String)hm.get("colname");
				String colvalue=(String)hm.get("colvalue");
				System.out.println(i+"->"+j+"tablename:"+tablename);
				System.out.println(i+"->"+j+"colname:"+colname);
				System.out.println(i+"->"+j+"colvalue:"+colvalue);
			}
			
		} */
		int rowSizeToInsert=10000*10000;
		
		
		Configuration cfg = HBaseConfiguration.create();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date=dateFormat.parse("2010-01-01 13:00:00.000");
//		System.out.println("date55:"+date);
		//long startTimeLong=date.getTime();//
//		System.out.println("startTimeLong:"+startTimeLong);
		long startTimeLong=1262322000000L;//2010-01-01 13:00:00.000
//		Thread.currentThread().sleep(2000);
		for(int i=0;i<rowSizeToInsert;i++)
		{
			ArrayList alEach=(ArrayList)al.get(i%dataMoniSize);
			String rowKey=(startTimeLong+i)+"";
			System.out.println(i+"->当前ROWKEY:"+rowKey);
			Calendar ca = Calendar.getInstance();
			Date date5 = ca.getTime();
			date5.setTime(startTimeLong+i);
//			System.out.println("Calendar.getInstance().getTime()date5:"+date5);
			String date1Str=dateFormat.format(date5);
			System.out.println("rowKey:"+rowKey+"->"+date1Str);
			HTable table = new HTable(cfg, tablename);
			
//			System.out.println(i+"->put '" + rowKey + "','" + columnFamily + ":" + column+ "','" + data + "'");
//			table.close();
			
			for(int j=0;j<alEach.size();j++)
			{
				HashMap hm=(HashMap)alEach.get(j);
				String tablenameTmp=(String)hm.get("tablename");
				String colname=(String)hm.get("colname");
				String colvalue=(String)hm.get("colvalue");
				
				String columnFamily=tablenameTmp;
				String column=colname;
				String data=colvalue;
				
				if(j==0)
				{
					//Put p0 = new Put(Bytes.toBytes(rowKey));
					//p0.add(Bytes.toBytes(columnFamily), Bytes.toBytes("datestr"),Bytes.toBytes(date1Str));
					//table.put(p0);
				}
				
				Put p1 = new Put(Bytes.toBytes(rowKey));
				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),Bytes.toBytes(data));
				table.put(p1);
				System.out.println(i+"->put '" + rowKey + "','" + columnFamily + ":" + column+ "','" + data + "'");
			
		 
				
				
			}
			table.close();
			
		}
		
	
	}
	
	public  static ArrayList  readFile(String filePath) throws Exception 
	{
		ArrayList al=new ArrayList();
		try {
			BufferedReader br=new BufferedReader(new FileReader(new File(filePath)));
			String line="";
			int i=0;
			while((line=br.readLine())!=null)
			{
				i++;
				System.out.println(i+"line:"+line);
				String[] strArray1 = line.split("#bigdot#");
				ArrayList alEach=new ArrayList();
				al.add(alEach);
				for(int j=0;j<strArray1.length;j++)
				{
					String part=strArray1[j];
					//System.out.println(j+"part:"+part);
					String[] partArray = part.split(";");
					if(partArray.length>4)
					{
						HashMap  hm=new HashMap();
						String tablename=partArray[1];
						String colname=partArray[3];
						String colvalue=partArray[4];
						hm.put("tablename", tablename);
						hm.put("colname", colname);
						if(colvalue.equalsIgnoreCase("True"))
						{
							colvalue="1";
						}
						else if(colvalue.equalsIgnoreCase("False"))
						{
							colvalue="0";
						}
						else if(colvalue.startsWith("."))
						{
							colvalue="0"+colvalue;
						}
						hm.put("colvalue", colvalue);
						
						alEach.add(hm);
					}
				}
				
				if(i>10)
				{
					//break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return al;
	}
	// 创建一张表
		public static  void creatTable(String tablename, String columnFamily1,String columnFamily2)
				throws Exception {
			Configuration cfg = HBaseConfiguration.create();
			HBaseAdmin admin = new HBaseAdmin(cfg);
			if (admin.tableExists(TableName.valueOf(tablename))) {
				System.out.println("table Exists!");
				System.exit(0);
			} else {
				HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tablename)); ;
				tableDesc.addFamily(new HColumnDescriptor(columnFamily1));
				tableDesc.addFamily(new HColumnDescriptor(columnFamily2));
				admin.createTable(tableDesc);
				System.out.println("create table success!");
			}
			admin.close();
		}
	 
}

