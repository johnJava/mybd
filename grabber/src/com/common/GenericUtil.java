package com.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericUtil {
	public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@SuppressWarnings("serial")
	public static List<String> creditlist=new ArrayList<String>(){
		
		{
			add("ico_xx_1");
			add("ico_xx_2");
			add("ico_xx_3");
			add("ico_xx_4");
			add("ico_xx_5");
		}
	};
	public static String cookieFormat(HashMap<String, String> cookieMap){
		if(cookieMap.size()==0)return "";
		 StringBuffer buff = new StringBuffer(2000);
		 for(Map.Entry<String, String> entry: cookieMap.entrySet()){
			 String key=entry.getKey().trim();
			 LogUtil.debugPrintf("begin=="+key);
			 if(key.equalsIgnoreCase("domain")||key.equalsIgnoreCase("HttpOnly")||key.equalsIgnoreCase("path"))
			 {
				 LogUtil.debugPrintf("ignore=="+key);
				 continue; 
			 } 
			 buff.append(entry.toString()+";");
		 }
		 buff.deleteCharAt(buff.lastIndexOf(";"));
		 return  buff.toString();
	}
}
