package appsoft.db.hb.service;

import java.io.IOException;
import java.util.List;

import appsoft.db.hb.service.QueryExtInfo;
import appsoft.db.hb.core.Nullable;

import appsoft.db.hb.HBRow;


/**
 * 基础服务
 * <pre>
 * 提供查询服务
 * </pre>
 * @author 王亮
 * */
public interface QueryService {
	
	
	/**
	 * 获取给定测点一段时间内的行记录
	 * @param startRowKey 开始行键
	 * @param endRowKey 结束行键
	 * @return 行记录集合
	 */
	public List<HBRow> getRows(String startRowKey, String endRowKey,@Nullable QueryExtInfo queryextinfo)throws IOException;
	
	/**
	 * 根据行键获取行记录
	 * @param rowkey 指定行键
	 * @return 行记录
	 */
	public HBRow getRow(String rowkey)throws IOException;
	
	/**
	 * 根据行键集合获取行记录集合
	 * @param rowkeys 指定行键
	 * @return 行记录集合
	 */
	public List<HBRow> getRows(List<String> rowkeys)throws IOException;
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
