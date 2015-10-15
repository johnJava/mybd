package appsoft.db.hb.service;

import java.util.List;
import java.util.Map;

import appsoft.db.hb.AggregateExtInfo;
import appsoft.db.hb.HBRow;
import appsoft.db.hb.core.Nullable;

/**
 * AggregateService.
 * 
 * <pre>
 * Provides aggregate function on hbase table.
 * </pre>
 * 
 * @author liang.wang
 */
public interface AggregateService {

    /** 
     * 计数器
     * @return 计数值
     */
    public long count();
    /**
     * 计数与求和器
     * @return
     */
    public long[] countAndSum();
	/**
	 * 获取给定测点一段时间内的历史数据，按照间隔的平均值
	 * @param startRowKey 开始行键
	 * @param endRowKey 结束行键
	 * @param period 获取数据时间间隔
	 * @return 平均值数组
	 */
	public long[] getAvgs(String startRowKey, String endRowKey,int period);
	
	/**
	 * 获取给定测点一段时间内的平均值
	 * @param startRowKey 开始行键
	 * @param endRowKey 结束行键
	 * @return 平均值
	 */
	public long getAvg(String startRowKey, String endRowKey);
	
	/**
	 * 获取给定测点一段时间内，按照间隔的最大值
	 * @param startRowKey 开始行键
	 * @param endRowKey 结束行键
	 * @param period 获取数据时间间隔
	 * @return 最大值集合
	 */
	public List<HBRow> getMaxRows(String startRowKey, String endRowKey,int period);
	
	/**
	 * 获取给定测点一段时间内的最大值
	 * @param startRowKey 开始行键
	 * @param endRowKey 结束行键
	 * @return 最大值
	 */
	public HBRow getMaxRow(String startRowKey, String endRowKey);
	
	/**
	 * 获取给定测点一段时间内，按照间隔的最小值
	 * @param startRowKey 开始行键
	 * @param endRowKey 结束行键
	 * @param period 获取数据时间间隔
	 * @return 最小值集合
	 */
	public List<HBRow> getMinRows(String startRowKey, String endRowKey,int period);
	
	/**
	 * 获取给定测点一段时间内的最小值
	 * @param startRowKey 开始行键
	 * @param endRowKey 结束行键
	 * @return 最小值
	 */
	public HBRow getMinRow(String startRowKey, String endRowKey);
}



