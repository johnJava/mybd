package appsoft.db.hb.service;

import appsoft.db.hb.HBRow;


/**
 * 基础服务
 * <pre>
 * 提供写入服务
 * </pre>
 * @author 王亮
 * */
public interface WriterService {
	
	
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
	
}
