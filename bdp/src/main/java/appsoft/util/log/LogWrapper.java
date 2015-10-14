package appsoft.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import appsoft.util.StrUtil;

/**
 * Slf4j的Logger包装类
 * @author Looly
 *
 */
public class LogWrapper implements Logger {
	private Logger logger;
	
	public LogWrapper(Class<?> clazz) {
		this.logger = LoggerFactory.getLogger(clazz);
	}
	
	public LogWrapper(String name) {
		this.logger = LoggerFactory.getLogger(name);
	}
	
	public LogWrapper(Logger logger) {
		this.logger = logger;
	}

	
	public String getName() {
		return logger.getName();
	}

	
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	
	public void trace(String msg) {
		logger.trace(msg);
		
	}

	
	public void trace(String format, Object arg) {
		logger.trace(format, arg);
		
	}

	
	public void trace(String format, Object arg1, Object arg2) {
		logger.trace(format, arg1, arg2);
	}

	
	public void trace(String format, Object... arguments) {
		logger.trace(format, arguments);
		
	}

	
	public void trace(String msg, Throwable t) {
		logger.trace(msg, t);
	}

	
	public boolean isTraceEnabled(Marker marker) {
		return logger.isTraceEnabled(marker);
	}

	
	public void trace(Marker marker, String msg) {
		logger.trace(marker, msg);
		
	}

	
	public void trace(Marker marker, String format, Object arg) {
		logger.trace(marker, format, arg);
	}

	
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		logger.trace(marker, format, arg1, arg2);
	}

	
	public void trace(Marker marker, String format, Object... argArray) {
		logger.trace(marker, format, argArray);
	}

	
	public void trace(Marker marker, String msg, Throwable t) {
		logger.trace(marker, msg, t);
	}

	
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	
	public void debug(String msg) {
		logger.debug(msg);
	}

	
	public void debug(String format, Object arg) {
		logger.debug(format, arg);
	}

	
	public void debug(String format, Object arg1, Object arg2) {
		logger.debug(format, arg1, arg2);
	}

	
	public void debug(String format, Object... arguments) {
		logger.debug(format, arguments);
	}

	
	public void debug(String msg, Throwable t) {
		logger.debug(msg, t);
	}

	
	public boolean isDebugEnabled(Marker marker) {
		return logger.isDebugEnabled(marker);
	}

	
	public void debug(Marker marker, String msg) {
		logger.debug(marker, msg);
	}

	
	public void debug(Marker marker, String format, Object arg) {
		logger.debug(marker, format, arg);
	}

	
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		logger.debug(marker, format, arg1, arg2);
	}

	
	public void debug(Marker marker, String format, Object... arguments) {
		logger.debug(marker, format, arguments);
	}

	
	public void debug(Marker marker, String msg, Throwable t) {
		logger.debug(marker, msg, t);
	}

	
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	
	public void info(String msg) {
		logger.info(msg);
	}

	
	public void info(String format, Object arg) {
		logger.info(format, arg);
	}

	
	public void info(String format, Object arg1, Object arg2) {
		logger.info(format, arg1, arg2);
	}

	
	public void info(String format, Object... arguments) {
		logger.info(format, arguments);
	}

	
	public void info(String msg, Throwable t) {
		logger.info(msg, t);
	}

	
	public boolean isInfoEnabled(Marker marker) {
		return logger.isInfoEnabled(marker);
	}

	
	public void info(Marker marker, String msg) {
		logger.info(marker, msg);
	}

	
	public void info(Marker marker, String format, Object arg) {
		logger.info(marker, format, arg);
	}

	
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		logger.info(marker, format, arg1, arg2);
	}

	
	public void info(Marker marker, String format, Object... arguments) {
		logger.info(marker, format, arguments);
	}

	
	public void info(Marker marker, String msg, Throwable t) {
		logger.info(marker, msg, t);
	}

	
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	
	public void warn(String msg) {
		logger.warn(msg);
	}

	
	public void warn(String format, Object arg) {
		logger.warn(format, arg);
	}

	
	public void warn(String format, Object... arguments) {
		logger.warn(format, arguments);
	}

	
	public void warn(String format, Object arg1, Object arg2) {
		logger.warn(format, arg1, arg2);
	}

	
	public void warn(String msg, Throwable t) {
		logger.warn(msg, t);
	}

	
	public boolean isWarnEnabled(Marker marker) {
		return isWarnEnabled(marker);
	}

	
	public void warn(Marker marker, String msg) {
		logger.warn(marker, msg);
	}

	
	public void warn(Marker marker, String format, Object arg) {
		logger.warn(marker, format, arg);
	}

	
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		logger.warn(marker, format, arg1, arg2);
	}

	
	public void warn(Marker marker, String format, Object... arguments) {
		logger.warn(marker, format, arguments);
	}

	
	public void warn(Marker marker, String msg, Throwable t) {
		logger.warn(marker, msg, t);
	}

	
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	
	public void error(String msg) {
		logger.error(msg);
	}

	
	public void error(String format, Object arg) {
		logger.error(format, arg);
	}

	
	public void error(String format, Object arg1, Object arg2) {
		logger.error(format, arg1, arg2);
	}

	
	public void error(String format, Object... arguments) {
		logger.error(format, arguments);
	}

	
	public void error(String msg, Throwable t) {
		logger.error(msg, t);
	}

	
	public boolean isErrorEnabled(Marker marker) {
		return logger.isErrorEnabled(marker);
	}

	
	public void error(Marker marker, String msg) {
		logger.error(marker, msg);
	}

	
	public void error(Marker marker, String format, Object arg) {
		logger.error(marker, format, arg);
	}

	
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		logger.error(marker, format, arg1, arg2);
	}

	
	public void error(Marker marker, String format, Object... arguments) {
		logger.error(marker, format, arguments);
	}

	
	public void error(Marker marker, String msg, Throwable t) {
		logger.error(marker, msg, t);
	}

	//------------------------ Added method start
	/**
	 * Warn等级日志<br>
	 * @param e 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public void warn(Throwable e, String format, Object... arguments) {
		warn(StrUtil.format(format, arguments), e);
	}
	
	/**
	 * Error等级日志<br>
	 * @param e 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public void error(Throwable e, String format, Object... arguments) {
		error(StrUtil.format(format, arguments), e);
	}
	//------------------------ Added method end
}
