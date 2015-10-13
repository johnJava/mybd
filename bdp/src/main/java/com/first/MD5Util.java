package com.first;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public  final class MD5Util {
	public static String getMD5(String str){
		String reStr=null;
		try {
			//��������ָ���㷨���Ƶ���ϢժҪ
			MessageDigest md=MessageDigest.getInstance("MD5");
			//ʹ��ָ�����ֽڸ���ժҪ
			md.update(str.getBytes());
			//ͨ��ִ���������֮������ղ�����ɹ�ϣ����
			byte ss[]=md.digest();
			reStr=bytes2String(ss);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return reStr;
	}
	
	//���ֽ�����ת��Ϊ�ַ���
	private static String bytes2String(byte[] aa){
		String hash="";
		//ѭ������
		for(int i=0;i<aa.length;i++){
			int temp;
			//���С��0�������Ϊ����
			if(aa[i]<0)
				temp=256+aa[i];
			else
				temp=aa[i];
			if(temp<16)
				hash+="0";
			//ת��Ϊʮ������
			hash+=Integer.toString(temp,16); 
		}
		//ȫ��ת��Ϊ��д
		hash=hash.toUpperCase();
		return hash;
	}
	 public static void  main (String [] agrs) {
		String ss="TheRealMT";//212DC26B6246FAD4D2CB3740FC25866F8
		System.out.println(MD5Util.getMD5(ss));
	}
}

