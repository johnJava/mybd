package com.common;

public class LogUtil {

	private static String type="debug";
	
	public static void debugPrintf(String msg){
		if(type.equalsIgnoreCase("debug"))
		System.out.println("debug:"+msg);
	}
	public static void infoPrintf(String msg){
		System.out.println("info:"+msg);
	}
}
