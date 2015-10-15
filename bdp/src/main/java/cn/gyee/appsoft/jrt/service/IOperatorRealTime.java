/**
 * <p>文件名:	IOperatorRealTime.java</p>
 * <p>版权:  </p>
 * <p>公司:	company Co., Ltd.</p>
 * <p>项目名：gyee.jedna</p>
 * <p>作者：	刘厦(liusha.information@gmail.com)</p>
 */
package cn.gyee.appsoft.jrt.service;

import java.util.List;

import cn.gyee.appsoft.jrt.model.PointData;
//import cn.gyee.appsoft.jrt.model.PointInfo;

/**
 * <p>
 * 		IOperatorRealTime
 * </p>
 * <p>
 * 		说明：操作实时库
 * </p>
 * 
 * @author 刘厦(liusha.information@gmail.com)
 * @version 0.0.0
 * <table style="border:1px solid gray;">
 *   <tr>
 *     <th width="100px">版本号</th>
 * 	   <th width="100px">动作</th>
 *     <th width="100px">修改人</th>
 *     <th width="100px">修改时间</th>
 *   </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *   <tr>
 *      <td>0.0.0</td>
 *      <td>创建类</td>
 *      <td>刘厦</td>
 *      <td>2013-12-9下午4:45:48</td>
 *   </tr>
 *   <tr>
 *      <td></td>
 *      <td></td>
 *      <td></td>
 *      <td></td>
 *   </tr>
 * </table>
 */
public interface IOperatorRealTime {
	
	/**
	 * 未用延后
	 * 将测点信息添加到内存中 
	 * 
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @param refInterval 刷新频率
	 */
	public void addPointMemory(String fullPointName, int refInterval);
	
	/**
	 * 获取给定测点实时数据
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @return
	 */
	public PointData getRealTimeData(String fullPointName);
	
	/**
	 * 获取给定多测点实时数据
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @return
	 */
	public List<PointData> getRealTimeData(List<String> fullPointNames);
	
	/**
	 * 获取给定测点一段时间内的历史原始值
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @param beginTime 开始时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public List<PointData> getHistoryRawData(String fullPointName, String beginTime, String endTime);
	
	/**
	 * 获取给定测点一段时间内的历史数据，按照间隔的快照值
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @param beginTime 开始时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param period 获取数据时间间隔
	 * @return
	 */
	public List<PointData> getHistorySnapData(String fullPointName, String beginTime, String endTime,int period);
	
	/**
	 * 单点某时刻值
	 * @param fullPointName
	 * @param beginTime
	 * @param endTime
	 * @param period
	 * @return
	 * @
	 */
	public PointData getHistorySnapData(String fullPointName, String hisTime);
	
	/**
	 * 断面获取给定时刻多点的历史数据快照值
	 * @param fullPointName
	 * @param hisTime
	 * @return
	 * @
	 */
	public List<PointData> getHistoryMatrixSnapData(List<String> fullPointName, String hisTime);
	
	/**
	 * 获取给定测点一段时间内的历史数据，按照间隔的平均值
	 * 
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @param beginTime 开始时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param period 获取数据时间间隔
	 * @return
	 */
	public List<PointData> getAvgHistoryData(String fullPointName, String beginTime, String endTime,int period);
	
	/**
	 * 获取给定测点一段时间内的平均值
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @param beginTime 开始时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @return 
	 */
	public PointData getAvgHistoryData(String fullPointName, String beginTime, String endTime);
	
	/**
	 * 获取给定测点一段时间内的历史数据，按照间隔的最大值
	 * 
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @param beginTime 开始时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param period 获取数据时间间隔
	 * @return
	 */
	public List<PointData> getMaxHistoryData(String fullPointName, String beginTime, String endTime,int period);
	
	/**
	 * 获取给定测点一段时间内的最大值
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @param beginTime 开始时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @return 
	 */
	public PointData getMaxHistoryData(String fullPointName, String beginTime, String endTime);
	
	/**
	 * 获取给定测点一段时间内的历史数据，按照间隔的最小值
	 * 
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @param beginTime 开始时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param period 获取数据时间间隔
	 * @return
	 */
	public List<PointData> getMinHistoryData(String fullPointName, String beginTime, String endTime,int period);
	
	/**
	 * 获取给定测点一段时间内的最小值
	 * @param fullPointName 测点全名，站点名.服务名.点号
	 * @param beginTime 开始时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @return 
	 */
	public PointData getMinHistoryData(String fullPointName, String beginTime, String endTime);
	
	/**
	 * 暂不用
	 * 获取所有实时服务名称
	 * @return
	 */
	public List<String> getRealTimeServices();
	
	/**
	 * 暂不用
	 * 通过给定测点,获取长ID
	 * @param shortId
	 * @return
	 */
	public String getLongId(String fullPointName);
	
	/**
	 * 暂不用
	 * 向给定实时服务创建多个测点
	 * @param serviceIp 实时服务Ip
	 * @param servicePort 实时服务端口
	 * @param pis 测点信息集合
	 */
	//public void createPoint(String serviceIp, String servicePort,List<PointInfo> pis);
	
	/**
	 * 暂不用
	 * 向给定实时服务创建单个测点
	 * @param serviceIp 实时服务Ip
	 * @param servicePort 实时服务端口
	 * @param pi 测点信息集合
	 */
	//public void createPoint(String serviceIp, String servicePort,PointInfo pi);
	
	/**
	 * 暂不用
	 *	向给定实时服务对应的多个点写入数据
	 * @param serviceIp 实时服务Ip
	 * @param servicePort 实时服务端口
	 * @param pis 测点信息集合
	 */
	//public void writePoint(String serviceIp, String servicePort,List<PointData> pds);
	
	/**
	 * 暂不用
	 * 向给定实时服务对应的单点写入数据
	 * @param serviceIp 实时服务Ip
	 * @param servicePort 实时服务端口
	 * @param pis 测点信息集合
	 */
	//public void writePoint(String serviceIp, String servicePort,PointData pd);
	
	/**
	 * 更新指定点的历史数据，历史数据不存在时插入修改历史数据，存在时直接修改历史
	 * @param fullPointName
	 * @param pds
	 * @
	 */
	public Integer putHistoryData(String fullPointName, List<PointData> pds) ;
	
	/**
	 * 更新指定点的历史数据，历史数据不存在时插入修改历史数据，存在时直接修改历史
	 * @param fullPointName
	 * @param pds
	 * @
	 */
	public Integer putHistoryData(String fullPointName, PointData point) ;

}
