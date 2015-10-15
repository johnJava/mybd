package appsoft.db.hb.service;

import java.util.List;

import appsoft.db.hb.HBRow;


/**
 * 基础服务
 * <pre>
 * 提供基础服务
 * </pre>
 * @author 王亮
 * */
public interface BasicService {
	
	
	/**
	 * 添加一条记录
	 * @return
	 */
	public HBRow addRow();
	/**
	 * 指定行键添加一条记录
	 * @param rowkey
	 * @return
	 */
	public HBRow addRow(String rowkey);
	
	/**
	 * 获取给定测点一段时间内的行记录
	 * @param startRowKey 开始行键
	 * @param endRowKey 结束行键
	 * @return 行记录集合
	 */
	public List<HBRow> getRows(String startRowKey, String endRowKey);
	
	/**
	 * 获取给定测点一段时间内，按照间隔的行记录
	 * @param startRowKey 开始行键
	 * @param endRowKey 结束行键
	 * @param period 获取数据时间间隔
	 * @return 行记录集合
	 */
	public List<HBRow> getRows(String startRowKey, String endRowKey,int period);
	
	/**
	 * 根据行键获取行记录
	 * @param rowkey 指定行键
	 * @return 行记录
	 */
	public HBRow getRow(String rowkey);
	
	/**
	 * 根据行键集合获取行记录集合
	 * @param rowkeys 指定行键
	 * @return 行记录集合
	 */
	public List<HBRow> getRows(List<String> rowkeys);
	
}
