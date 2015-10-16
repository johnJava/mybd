package appsoft.util.exceptions;

import appsoft.util.StrUtil;

/**
 * 未初始化异常
 * @author wangliang
 */
public class BDException extends RuntimeException{
	private static final long serialVersionUID = 8247610319171014183L;

	public BDException(Throwable e) {
		super(e.getMessage(), e);
	}
	
	public BDException(String message) {
		super(message);
	}
	
	public BDException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public BDException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public BDException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
