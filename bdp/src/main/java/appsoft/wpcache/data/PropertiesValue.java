package appsoft.wpcache.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class PropertiesValue {


	private static PropertiesValue pv;

	private static HashMap<String,String>  propMap;
	
	public  HashMap<String, String> getPropMap() {
		return propMap;
	}

	private PropertiesValue(){
		propMap = new HashMap<String,String>();
	}
	
	public static PropertiesValue  getPropValueInstance(){
		if(pv==null){
			pv=new PropertiesValue();
			pv.InstancePropValue();
		}
		return pv;
	}
	
	public  void InstancePropValue(){
		Properties prop = new Properties();
		try {
			InputStream inputstream = getClass().getResourceAsStream("/cacheconfig.properties");
			prop.load(inputstream);
			prop.load(inputstream);
			//加载属性列表
			Iterator<String> it=prop.stringPropertyNames().iterator();
			while(it.hasNext()){
			    String key=it.next();
			    propMap.put(key, prop.getProperty(key));
			}
			inputstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}     
	}

}
