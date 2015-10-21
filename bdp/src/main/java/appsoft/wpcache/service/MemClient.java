package appsoft.wpcache.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import appsoft.wpcache.bean.Expire;
import appsoft.wpcache.bean.MemCache;

import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.exception.MemcachedException;
/**
 * 重新封装的MemcachedClient客户端
 * @author sean
 * @see net.rubyeye.xmemcached.MemcachedClient
 */
public class MemClient{

	private net.rubyeye.xmemcached.MemcachedClient memcachedClient;
	
	public static int getExpireNumber(int num,Expire expire){
		if (expire == Expire.FOREVER) {
			num = 0;
		} else if (expire == Expire.MINUTE) {
			num = 60 * num;
		} else if (expire == Expire.HOUR) {
			num = 60 * 60 * num;
		} else if (expire == Expire.DAY) {
			num = 60 * 60 * 24 * num;
		}
		return num;
	}
	
	/**
	 * 获得实体
	 * @param key
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public <T>T get(String key) throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.get(key);
	}
	
	/**
	 * 给某缓存计数器累加
	 * @param key
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public long incr(String key)throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.incr(key, 1);
	}
	
	/**
	 * 给某缓存计数器递减，只有一些特殊需求是，会用到递减
	 * @param key
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public long decr(String key)throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.decr(key, 1);
	}
	
	/**
	 * 获得某个缓存的计数器,如果返回-1,说明不存在
	 * @param key
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public long getCounter(String key)throws TimeoutException, InterruptedException, MemcachedException{
		Counter count = memcachedClient.getCounter(key,-1);
		return count.get();
	}
	
	/**
	 * 手动更新缓存的过期时间
	 * @param key
	 * @param num
	 * @param expire
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean touch(String key,int num,Expire expire) throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.touch(key,getExpireNumber(num, expire));
	}
	
	/**
	 * 手动删除缓存内容
	 * @param key
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean delete(String key) throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.delete(key);
	}
	
	/**
	 * 无论何时都保存
	 * 放入散装数据
	 * 永久有效
	 * @param key
	 * @param value
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean set(String key,Object value) throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.set(key,0,value);
	}
	
	/**
	 * 仅当存储空间中不存在键相同的数据时才保存
	 * 放入散装数据
	 * 永久有效
	 * @param key
	 * @param value
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean add(String key,Object value) throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.add(key,0,value);
	}
	
	/**
	 * 仅当存储空间中存在键相同的数据时才保存
	 * 放入散装数据
	 * 永久有效
	 * @param key
	 * @param value
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean replace(String key,Object value) throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.replace(key,0,value);
	}
	
	/**
	 * 无论何时都保存
	 * @param cache
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean set(MemCache<?> cache) throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.set(cache.getKey(), cache.getExpire(), cache.getValue());
	}
	
	/**
	 * 仅当存储空间中不存在键相同的数据时才保存
	 * @param cache
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean add(MemCache<?> cache) throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.add(cache.getKey(), cache.getExpire(), cache.getValue());
	}
	
	/**
	 * 仅当存储空间中存在键相同的数据时才保存
	 * @param cache
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean replace(MemCache<?> cache) throws TimeoutException, InterruptedException, MemcachedException{
		return memcachedClient.replace(cache.getKey(), cache.getExpire(), cache.getValue());
	}
	
	/**
	 * 断开客户端
	 * @throws IOException
	 */
	public void shutdown() throws IOException{
		memcachedClient.shutdown();
	}

	public net.rubyeye.xmemcached.MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public void setMemcachedClient(
			net.rubyeye.xmemcached.MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}
	
}
