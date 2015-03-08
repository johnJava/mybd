package com.common;

public class LogUtil {

//	private static String type="single";
	private static int level=2;
	
	public static void debugPrintf(String msg){
		if(level>2)
		System.out.println("debug:"+msg);
	}
	public static void infoPrintf(String msg){
		if(level>1)
		System.out.println("info:"+msg);
	}
	public static void singleDebugPrintf(String msg){
		if(level>0)
		System.out.println("debug:"+msg);
	}
}
