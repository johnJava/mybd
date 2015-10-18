package appsoft.util;
/**
 * 聚合函数类型
 * @author wangliang
 */
public enum AggregateType {
	/**
	 * 求平均值
	 */
	AVG,
	/**
	 * 求和
	 */
	SUM,
	/**
	 * 求最大值
	 */
	MAX,
	/**
	 * 求最小值
	 */
	MIN,
	/**
	 * 计数条数
	 */
	COUNT,
	/**
	 * 求差
	 */
	SUB,
	/**
	 * 只同步，不计算
	 */
	SYN
	;
}
