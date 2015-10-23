package appsoft.wpcache.service;
import java.io.IOException;
import java.text.ParseException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class MemcacheTest {

    public static void main(String[] args) throws ParseException {
       MemcachedClientBuilder builder = 
    		   new XMemcachedClientBuilder(AddrUtil.getAddresses("ch6:12000"));
       MemcachedClient memcachedClient;
       try {
           memcachedClient = builder.build();
           MemClient client= new  MemClient();
           client.setMemcachedClient(memcachedClient);
           CacheService cs=new CacheService();
           cs.setMemClient(client);
           long start = System.currentTimeMillis();
           for (int i = 0; i < (1000); i++) {
        	   System.out.println("第["+i+"]次生成缓存数据");
        	   cs.cache("set");
           }
        	   
        cs.cache("get");
       	long end = System.currentTimeMillis();
		System.out.println("执行一次缓存:"+(end-start)+"毫秒");
		 client.shutdown();
       }catch (IOException e) {
           System.err.println("Shutdown MemcachedClient fail");
           e.printStackTrace();
       }

    }
    
    

}