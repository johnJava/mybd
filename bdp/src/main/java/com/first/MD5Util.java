package com.first;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public  final class MD5Util {
	public static String getMD5(String str){
		String reStr=null;
		try {
			//创建具有指定算法名称的信息摘要
			MessageDigest md=MessageDigest.getInstance("MD5");
			//使用指定的字节更新摘要
			md.update(str.getBytes());
			//通过执行诸如填充之类的最终操作完成哈希计算
			byte ss[]=md.digest();
			reStr=bytes2String(ss);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return reStr;
	}
	
	//将字节数组转换为字符串
	private static String bytes2String(byte[] aa){
		String hash="";
		//循环数组
		for(int i=0;i<aa.length;i++){
			int temp;
			//如果小于0，将其变为正数
			if(aa[i]<0)
				temp=256+aa[i];
			else
				temp=aa[i];
			if(temp<16)
				hash+="0";
			//转换为十六进制
			hash+=Integer.toString(temp,16); 
		}
		//全部转换为大写
		hash=hash.toUpperCase();
		return hash;
	}
	 public static void  main (String [] agrs) {
		String ss="TheRealMT";//212DC26B6246FAD4D2CB3740FC25866F8
		System.out.println(MD5Util.getMD5(ss));
	}
}

