package appsoft.wpcache.bean;

import java.io.Serializable;

import appsoft.wpcache.service.MemClient;


/**
 * 缓存实体
 * @author sean
 * @param <T>
 */
public class MemCache<T extends Object> implements Serializable{
	private static final long serialVersionUID = -4552478213376248120L;
	private int EXPIRE_NUM = 1;
	private String key;
	private T value;

	public MemCache(String key, T value) {
		this(Expire.FOREVER, key, value);
	}

	public MemCache(Expire expire, String key, T value) {
		this(expire, 1, key, value);
	}

	public MemCache(Expire expire, int num, String key, T value) {
		this.EXPIRE_NUM = MemClient.getExpireNumber(num, expire);
		this.key = key;
		this.value = value;
	}

	public int getExpire() {
		return EXPIRE_NUM;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
